package de.hhu.stups.bxmlgenerator.generators;


import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.*;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class STGroupGenerator extends DepthFirstAdapter implements AbstractFinder {

    private ST currentGroup;

    private HashMap<Integer, BType> nodeType;

    private Typechecker typechecker;

    private Node startNode;

    private STGroupFile stGroupFile;

    private TypeInfoGenerator typeInfoGenerator;

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
        if(startNode instanceof Start)
        {
            typeInfoGenerator = new TypeInfoGenerator(stGroupFile, nodeType);
            TemplateHandler.add(currentGroup, "type_info",typeInfoGenerator.generateTypeInfo());
        }
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
    public void caseAAbstractMachineParseUnit(AAbstractMachineParseUnit node)
    {
        TemplateHandler.add(currentGroup, "kind", "abstraction");


        if(node.getVariant() != null)
        {
            node.getVariant().apply(this);
        }
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }

        visitMachineClause(node.getMachineClauses());
    }

    @Override
    public void caseAImplementationMachineParseUnit(AImplementationMachineParseUnit node)
    {
        TemplateHandler.add(currentGroup, "kind", "implementation");

        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        if(node.getRefMachine() != null)
        {
            node.getRefMachine().apply(this);
        }

        visitMachineClause(node.getMachineClauses());
    }

    @Override
    public void caseARefinementMachineParseUnit(ARefinementMachineParseUnit node) {
        TemplateHandler.add(currentGroup, "kind", "refinement");

        if (node.getHeader() != null) {
            node.getHeader().apply(this);
        }
        if (node.getRefMachine() != null) {
            node.getRefMachine().apply(this);
        }

        visitMachineClause(node.getMachineClauses());

    }

    public void visitMachineClause(List<PMachineClause> list){
        for (PMachineClause machineClause : list) {
            String templateTarget = find(machineClause);
            TemplateHandler.add(getCurrentGroup(), templateTarget,
                    new STGroupGenerator(
                            getStGroupFile(),
                            getStGroupFile().getInstanceOf(templateTarget),
                            getNodeType(),
                            getTypechecker(),
                            machineClause).generateCurrent());
        }
    }

    @Override
    public void caseAMachineHeader(AMachineHeader node)
    {

        TemplateHandler.add(getCurrentGroup(), "machine", node.getName().get(0).toString().replace(" ", ""));

        List<PExpression> pExpressionList = new ArrayList<>(node.getParameters());
        for(PExpression e : pExpressionList)
        {
            e.apply(this);
        }
    }

    @Override
    public void caseAVariablesMachineClause(AVariablesMachineClause node)
    {
        if(node.getIdentifiers() != null) {
            List<PExpression> ids = node.getIdentifiers();

            List<String> result = visitMultipleNodes(ids);

            TemplateHandler.add(getCurrentGroup(), "ids", result);
        }
    }

    @Override
    public void caseAInvariantMachineClause(AInvariantMachineClause node)
    {
        PPredicate predicate = node.getPredicates();

        STGroupGenerator stGroupGenerator = new STGroupGenerator(getStGroupFile(),
                getStGroupFile().getInstanceOf(find(predicate)),
                getNodeType(), getTypechecker(), predicate);

        TemplateHandler.add(getCurrentGroup(), "body",  stGroupGenerator.generateCurrent());
    }

    @Override
    public void caseAInitialisationMachineClause(AInitialisationMachineClause node)
    {
        if(node.getSubstitutions() != null)
        {
            PSubstitution substitution = node.getSubstitutions();

            STGroupGenerator initialisationBodyGenerator = makeGenerator(substitution, find(substitution));

            TemplateHandler.add(currentGroup, "body", initialisationBodyGenerator.generateCurrent());
        }
    }

    @Override
    public void caseAOperationsMachineClause(AOperationsMachineClause node) {

        List<POperation> operations = node.getOperations();

        List<String> operationsExpanded = operations.stream().map(
                operation -> makeGenerator(operation, "operation").generateCurrent())
                .collect(Collectors.toList());

        TemplateHandler.add(currentGroup, "operation", operationsExpanded);
    }

    @Override
    public void caseAOperation(AOperation node)
    {
        String operationName = node.getOpName().get(0).toString().replace(" ", "");
        TemplateHandler.add(currentGroup, "name", operationName);

        List<PExpression> returnValues = node.getReturnValues();
        if(!returnValues.isEmpty()){
            List<String> expandedReturnValues = visitMultipleNodes(returnValues);
            ST returnValueTemplate = stGroupFile.getInstanceOf("output_parameters");
            TemplateHandler.add(returnValueTemplate, "body", expandedReturnValues);

            String filledReturnValuesTemplate = returnValueTemplate.render();
            TemplateHandler.add(currentGroup, "output_parameters", filledReturnValuesTemplate);
        }

        /*
        List<PExpression> parameterValues = node.getParameters();
        if(!parameterValues.isEmpty()){
            List<String> expandedParameterValues = visitMultipleNodes(returnValues);
            ST parameterValueTemplate = stGroupFile.getInstanceOf("input_parameters");
            TemplateHandler.add(parameterValueTemplate, "body", expandedParameterValues);

            String filledReturnValuesTemplate = parameterValueTemplate.render();
            TemplateHandler.add(currentGroup, "input_parameter", filledReturnValuesTemplate);
        }
        */

        if(node.getOperationBody() instanceof APreconditionSubstitution){


            node.getOperationBody().apply(this);

            PSubstitution substitution =  ((APreconditionSubstitution) node.getOperationBody()).getSubstitution();
            String unfoldedSubstitution = makeGenerator(substitution, find(substitution) ).generateCurrent();

            TemplateHandler.add(currentGroup, "body", unfoldedSubstitution);

            }else{
                String unfoldedSubstitution = makeGenerator(node.getOperationBody(), find(node.getOperationBody()) ).generateCurrent();
                TemplateHandler.add(currentGroup, "body", unfoldedSubstitution);
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
    public void caseAAddExpression(AAddExpression node)
    {
        TemplateHandler.add(currentGroup, "op", "+");

        BType bType = typechecker.getType(node);
        int nodeHash = generateHash(bType);
        nodeType.put(nodeHash, bType);

        TemplateHandler.add(currentGroup, "typref", nodeHash);

        PExpression left = node.getLeft();
        PExpression right = node.getRight();


        List<String> evaluatedChildren = visitRightAndLeftExpression(left, right);

        TemplateHandler.add(currentGroup, "body", evaluatedChildren);
    }

    @Override
    public void caseAMinusOrSetSubtractExpression(AMinusOrSetSubtractExpression node)
    {
        TemplateHandler.add(currentGroup, "op", "-");

        BType bType = typechecker.getType(node);
        int nodeHash = generateHash(bType);
        nodeType.put(nodeHash, bType);

        TemplateHandler.add(currentGroup, "typref", nodeHash);

        PExpression left = node.getLeft();
        PExpression right = node.getRight();


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
                .map(predicateNode -> new Pair<>(predicateNode, find(predicateNode)))
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
    public void caseAGreaterPredicate(AGreaterPredicate node)
    {
        TemplateHandler.add(currentGroup, "op", "&gt;");

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
        List<PExpression> leftSide = node.getLhsExpression();

        List<String> leftSideResult = visitMultipleNodes(leftSide);

        TemplateHandler.add(currentGroup, "body1", leftSideResult);

        List<PExpression> rightSide = node.getRhsExpressions();

        List<String> rightSideResult = visitMultipleNodes(rightSide);

        TemplateHandler.add(currentGroup, "body2", rightSideResult);
    }

    @Override
    public void caseAParallelSubstitution(AParallelSubstitution node)
    {
            List<PSubstitution> substitutions = node.getSubstitutions();
            List<String> unfoldedSubstitutions = visitMultipleNodes(substitutions);
            TemplateHandler.add(currentGroup, "op" , "||");
            TemplateHandler.add(currentGroup, "statements", unfoldedSubstitutions);
    }


    @Override
    public void caseAPreconditionSubstitution(APreconditionSubstitution node)
    {
        if(currentGroup.toString().equals("/operation()")){

            ST precondition = stGroupFile.getInstanceOf("precondition");
            String expandedPrecondition = makeGenerator(node.getPredicate(), find(node.getPredicate())).generateCurrent();
            System.out.println(expandedPrecondition);
            TemplateHandler.add(precondition, "body", expandedPrecondition);

            TemplateHandler.add(currentGroup, "precondition", precondition.render());

        }
    }

    @Override
    public void caseASelectSubstitution(ASelectSubstitution node)
    {
        String conditions = makeGenerator(node.getCondition(), find(node.getCondition())).generateCurrent();
        TemplateHandler.add(currentGroup, "conditions", conditions);

        String then = makeGenerator(node.getThen(), find(node.getThen())).generateCurrent();
        TemplateHandler.add(currentGroup, "then", then);


        List<PSubstitution> copy = node.getWhenSubstitutions();
        for(PSubstitution e : copy)
        {
                System.out.println(e.getClass());
        }


        /*
        if(node.getElse() != null)
        {
            String elseC = makeGenerator(node.getElse(), "elseC").generateCurrent();
            TemplateHandler.add(currentGroup, "elseC", elseC);
        }
        */

    }



    private List<String> visitMultipleNodes(List<? extends Node> nodes){
        return nodes.stream()
                .map(node -> new Pair<>(node, find(node)))
                .map(pair -> new STGroupGenerator(stGroupFile, stGroupFile.getInstanceOf(pair.getValue()),
                        nodeType, typechecker, pair.getKey()).generateCurrent())
                .collect(Collectors.toList());
    }

    public List<String> visitRightAndLeftExpression(PExpression left, PExpression right){

        String leftPredicate = find(left);
        String rightPredicate = find(right);

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

    private STGroupGenerator makeGenerator(Node startNode, String templateTarget){
        return new STGroupGenerator(stGroupFile, stGroupFile.getInstanceOf(templateTarget),
                nodeType, typechecker, startNode);
    }


}
