package de.hhu.stups.bxmlgenerator.sablecc;


import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.ExpressionFinder;
import de.hhu.stups.bxmlgenerator.util.Pair;
import de.hhu.stups.bxmlgenerator.util.PredicateFinder;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class STGroupGenerator extends DepthFirstAdapter {

    private ST currentGroup;

    private HashMap<Integer, BType> nodeType;

    private Typechecker typechecker;

    private Node startNode;

    private STGroupFile stGroupFile;

    public STGroupGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer, BType> nodeType, Typechecker typeChecker, Node startNode){
        this.currentGroup = group;
        this.nodeType = nodeType;
        this.typechecker = typeChecker;
        this.startNode = startNode;
        this.stGroupFile = stGroupFile;
    }

    public ST getCurrentGroup(){
        return currentGroup;
    }

    public HashMap<Integer, BType> getNodeType(){
        return nodeType;
    }

    public Typechecker getTypechecker(){
        return typechecker;
    }

    public STGroupFile getStGroupFile(){
        return stGroupFile;
    }

    public Node getStartNode(){
        return startNode;
    }


    public String generateCurrent(){
        startNode.apply(this);
        return currentGroup.render();
    }

    /**
     * @param type BType which is converted to integer hash
     * @return
     *
     * Gets a btype and returns it´s String hash representation while checking if the hash is already taken by an other
     * type.
     */
    public int generateHash(BType type) {
        int hash = Math.abs(type.toString().hashCode());
        //Need strings here due to the fact that BTypes might be different instances...
        if (!nodeType.containsKey(hash) || nodeType.get(hash).toString().equals(type.toString())) {
            return hash;
        } else {
            throw new IndexOutOfBoundsException("Hash was already taken! " + type.toString() + " is not " + nodeType.get(hash));
        }
    }

    @Override
    public void caseAIdentifierExpression(AIdentifierExpression node)
    {
        TemplateHandler.add(currentGroup, "val", node.toString().replace(" ", ""));
        BType type = typechecker.getType(node);
        int nodeHash = generateHash(type);
        nodeType.put(nodeHash, type);
        TemplateHandler.add(currentGroup, "typref", nodeHash);
    }

    @Override
    public void caseAIntervalExpression(AIntervalExpression node)
    {
        TemplateHandler.add(currentGroup, "op", "..");

        BType bType = typechecker.getType(node);
        int nodeHash = generateHash(bType);
        nodeType.put(nodeHash, bType);

        TemplateHandler.add(currentGroup, "typref", nodeHash);

        PExpression left = node.getLeftBorder();
        PExpression right = node.getRightBorder();


        List<String> evaluatedChildren = visitRightAndLeftExpression(left, right);

        TemplateHandler.add(currentGroup, "body", evaluatedChildren);
    }

    @Override
    public void caseAIntegerExpression(AIntegerExpression node)
    {
        String value = node.getLiteral().toString().replace(" ", "");
        TemplateHandler.add(currentGroup, "val", value);
        BType bType = typechecker.getType(node);
        int nodeHash = generateHash(bType);
        TemplateHandler.add(currentGroup, "typref", nodeHash);
    }

    @Override
    public void caseANatSetExpression(ANatSetExpression node)
    {
        TemplateHandler.add(currentGroup, "val", "NAT");
        BType type = typechecker.getType(node);
        int nodeHash = generateHash(type);
        nodeType.put(nodeHash, type);
        TemplateHandler.add(currentGroup, "typref", nodeHash);
    }

    @Override
    public void caseAConjunctPredicate(AConjunctPredicate node)
    {
        TemplateHandler.add(currentGroup, "op", "&amp;");

        List<PPredicate> expandedConjunction = unfoldConjunction(node);

        List<String> result = expandedConjunction.stream()
                .map(predicateNode -> new Pair<>(predicateNode, PredicateFinder.findPredicate(predicateNode)))
                .map(pair -> new STGroupGenerator(stGroupFile,
            stGroupFile.getInstanceOf(pair.getValue()), nodeType, typechecker, pair.getKey()).generateCurrent())
                .collect(Collectors.toList());

        TemplateHandler.add(currentGroup, "statements", result);
    }

    @Override
    public void caseAGreaterEqualPredicate(AGreaterEqualPredicate node)
    {
        TemplateHandler.add(currentGroup, "op", "&gt;=");

        PExpression left = node.getLeft();
        PExpression right = node.getRight();

        List<String> evaluatedChildren = visitRightAndLeftExpression(left, right);

        TemplateHandler.add(currentGroup, "statements", evaluatedChildren);
    }

    @Override
    public void caseALessPredicate(ALessPredicate node)
    {
        TemplateHandler.add(currentGroup, "op", "&lt;");

        PExpression left = node.getLeft();
        PExpression right = node.getRight();

        List<String> evaluatedChildren = visitRightAndLeftExpression(left, right);

        TemplateHandler.add(currentGroup, "statements", evaluatedChildren);
    }

    @Override
    public void caseAMemberPredicate(AMemberPredicate node)
    {
        TemplateHandler.add(currentGroup, "op", ":");

        PExpression left = node.getLeft();
        PExpression right = node.getRight();

        List<String> evaluatedChildren = visitRightAndLeftExpression(left, right);

        TemplateHandler.add(currentGroup, "statements", evaluatedChildren);
    }

    @Override
    public void caseAAssignSubstitution(AAssignSubstitution node)
    {
        List<PExpression> leftSide = new ArrayList<>(node.getLhsExpression());

        List<String> leftSideResult = visitMultipleExpressions(leftSide);

        TemplateHandler.add(currentGroup, "body1", leftSideResult);

        List<PExpression> rightSide = new ArrayList<>(node.getRhsExpressions());

        List<String> rightSideResult = visitMultipleExpressions(rightSide);

        TemplateHandler.add(currentGroup, "body2", rightSideResult);
    }

    public List<String> visitMultipleExpressions(List<PExpression> list){
        return list.stream()
                .map(expression -> new Pair<>(expression, ExpressionFinder.findExpression(expression)))
                .map(pair -> new STGroupGenerator(stGroupFile, stGroupFile.getInstanceOf(pair.getValue()),
                        nodeType, typechecker, pair.getKey()).generateCurrent())
                .collect(Collectors.toList());
    }

    public List<String> visitRightAndLeftExpression(PExpression left, PExpression right){

        String leftPredicate = ExpressionFinder.findExpression(left);
        String rightPredicate = ExpressionFinder.findExpression(right);

        STGroupGenerator stGroupGeneratorLeft = new STGroupGenerator(stGroupFile,
                stGroupFile.getInstanceOf(leftPredicate), nodeType, typechecker, left);

        STGroupGenerator stGroupGeneratorRight = new STGroupGenerator(stGroupFile,
                stGroupFile.getInstanceOf(rightPredicate), nodeType, typechecker, right);

        String leftStatements = stGroupGeneratorLeft.generateCurrent();
        String rightStatements = stGroupGeneratorRight.generateCurrent();

        return List.of(leftStatements, rightStatements);
    }


    private static List<PPredicate> unfoldIfNested(STGroupGenerator stGroupGenerator, PPredicate node){
        List<PPredicate> result = new ArrayList<>();

        if(node instanceof AConjunctPredicate){
            result.addAll(stGroupGenerator.unfoldConjunction((AConjunctPredicate) node));
        }else{
            result.add(node);
        }
        return result;
    }
    /**
     * @param node the node to unfold
     * @return return a List with all direct children
     *
     * Multiple Conjunctions like \< a : INT & b : INT & c : INT>  are represented with multiple stacked CokunctionPRedicate
     * nodes. bxml format only uses the term nary_sub to describe any number of conjunctions
     */
    public List<PPredicate> unfoldConjunction(AConjunctPredicate node){
        List<PPredicate> result = new ArrayList<>();

        PPredicate left = node.getLeft();
        PPredicate right = node.getRight();

        result.addAll(unfoldIfNested(this, left));
        result.addAll(unfoldIfNested(this, right));

        return result;
    }


}
