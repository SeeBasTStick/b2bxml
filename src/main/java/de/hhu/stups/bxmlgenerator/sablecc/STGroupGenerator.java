package de.hhu.stups.bxmlgenerator.sablecc;


import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.Node;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;


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
        Integer nodeHash = generateHash(type);
        nodeType.put(nodeHash, type);
        TemplateHandler.add(currentGroup, "typref", nodeHash);
    }












}
