package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.Node;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.IfOrSelectSubstitutionsNode;
import de.prob.parser.ast.nodes.substitution.ListSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;


import java.util.Map;
import java.util.stream.Collectors;

public abstract class BXMLBodyGenerator {

    private Map<Integer, BType> nodeType;

    private NameHandler nameHandler;

    private STGroup currentGroup;

    public BXMLBodyGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup)
    {
        this.nodeType = nodeType;
        this.currentGroup = currentGroup;
        this.nameHandler = nameHandler;
    }

    public Map<Integer, BType> getNodeTyp(){
        return nodeType;
    }

    public NameHandler getNameHandler(){
        return nameHandler;
    }

    public STGroup getSTGroup(){ return currentGroup; }

    public String exceptionThrower(Node node){
        try {
            throw new Exception(node.getClass() + " is not implemented yet");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String processExprNode(ExprNode node){
        String result;
        switch (node.getClass().getSimpleName()){
            case "IdentifierExprNode":
                IdentifierExprNode identifierExprNode = (IdentifierExprNode) node;
                ST id = currentGroup.getInstanceOf("id");
                TemplateHandler.add(id, "val", nameHandler.handleIdentifier(identifierExprNode.getName(),
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

                TemplateHandler.add(id, "typref", generateHash(identifierExprNode.getType()));

                nodeType.put(generateHash(identifierExprNode.getType()), identifierExprNode.getType());
                result = id.render();
                break;
            case "ExpressionOperatorNode":
                ExpressionOperatorNode expressionOperatorNode = (ExpressionOperatorNode) node;
                result = processExpressionOperatorNode(expressionOperatorNode);
                break;
            case "NumberNode":
                NumberNode numberNode = (NumberNode) node;
                ST integer_literal = currentGroup.getInstanceOf("integer_literal");
                TemplateHandler.add(integer_literal, "val", nameHandler.handleIdentifier(numberNode.getValue().toString(),
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

                TemplateHandler.add(integer_literal, "typref", generateHash(numberNode.getType()));

                nodeType.put(generateHash(numberNode.getType()), numberNode.getType());
                result = integer_literal.render();
                break;
            default:
                result = "";
                try {
                    throw new Exception(node.getClass() + " not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    protected String processExpressionOperatorNode(ExpressionOperatorNode node){

        String result = "";
        ST binary_exp = currentGroup.getInstanceOf("binary_exp");

        switch (node.getOperator()){
            case INTERVAL:
                TemplateHandler.add(binary_exp, "op", nameHandler.handleIdentifier("..",
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));
                break;

            case MINUS:
                TemplateHandler.add(binary_exp, "op", nameHandler.handleIdentifier("-",
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));
                break;

            case PLUS:
                TemplateHandler.add(binary_exp, "op", nameHandler.handleIdentifier("+",
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));
                break;

            default:
                try {
                    throw new Exception(node.getOperator() + " not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        TemplateHandler.add(binary_exp, "typref", generateHash(node.getType()));

        TemplateHandler.add(binary_exp, "body",  node.getExpressionNodes().stream()
                .map(this::processExprNode)
                .collect(Collectors.toList()));

        System.out.println("node " + node + " has type " + node.getType().toString() + "with hash " + generateHash(node.getType()));
        nodeType.put(generateHash(node.getType()), node.getType());
        result = binary_exp.render();

        return result;
    }

    protected String processSubstitutionNode(SubstitutionNode node) {

        String result;

        switch (node.getClass().getSimpleName()){
            case "IfOrSelectSubstitutionsNode":
                IfOrSelectSubstitutionsNode ifOrSelectSubstitutionsNode = (IfOrSelectSubstitutionsNode) node;
                if(ifOrSelectSubstitutionsNode.getOperator() == IfOrSelectSubstitutionsNode.Operator.IF)
                {
                    result = exceptionThrower(ifOrSelectSubstitutionsNode);
                }
                else{

                    ST select = currentGroup.getInstanceOf("select");
                    TemplateHandler.add(select, "conditions", ifOrSelectSubstitutionsNode.getConditions().stream()
                            .map(this::processPredicateNode)
                            .collect(Collectors.toList()));


                    TemplateHandler.add(select, "then", ifOrSelectSubstitutionsNode.getSubstitutions().stream()
                            .map(this::processSubstitutionNode)
                            .collect(Collectors.toList()));

                    result = select.render();
                }
                break;
            case "ListSubstitutionNode":
                ListSubstitutionNode listSubstitutionNode = (ListSubstitutionNode) node;
                ST nary_sub = currentGroup.getInstanceOf("nary_sub");

                if(listSubstitutionNode.getOperator() == ListSubstitutionNode.ListOperator.Parallel)
                {
                    TemplateHandler.add(nary_sub, "op", "||");
                }else{
                    TemplateHandler.add(nary_sub, "op", ";");

                }
                TemplateHandler.add(nary_sub, "statements", listSubstitutionNode.getSubstitutions().stream()
                        .map(this::processSubstitutionNode)
                        .collect(Collectors.toList()));
                result = nary_sub.render();
                break;
            case "AssignSubstitutionNode":
                AssignSubstitutionNode assignSubstitutionNode = (AssignSubstitutionNode) node;
                ST assignSub = currentGroup.getInstanceOf("assignment_sub");
                TemplateHandler.add(assignSub, "body1", assignSubstitutionNode.getLeftSide().stream()
                        .map(this::processExprNode)
                        .collect(Collectors.toList()));

                TemplateHandler.add(assignSub, "body2", assignSubstitutionNode.getRightSide().stream()
                        .map(this::processExprNode)
                        .collect(Collectors.toList()));

                return assignSub.render();
            default:
                result = exceptionThrower(node);

        }
        return result;

    }

    protected String processPredicateNode(PredicateNode node)
    {
        String result ;
        //System.out.println("genPre "+ node);
        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorWithExprArgsNode":
                PredicateOperatorWithExprArgsNode predicateOperatorWithExprArgsNode = (PredicateOperatorWithExprArgsNode) node;
                ST expComparision = currentGroup.getInstanceOf("exp_comparision");
                TemplateHandler.add(expComparision, "op",
                        processOperatorPredOperatorExprArgs(predicateOperatorWithExprArgsNode.getOperator()));
                TemplateHandler.add(expComparision, "statements",
                        predicateOperatorWithExprArgsNode.getExpressionNodes().stream()
                                .map(this::processExprNode)
                                .collect(Collectors.toList()));

                result = expComparision.render();
                break;
            case "PredicateOperatorNode":
                PredicateOperatorNode predicateOperatorNode = (PredicateOperatorNode) node;
                ST invariant = currentGroup.getInstanceOf("invariant");
                TemplateHandler.add(invariant, "body", processPredicateOperatorNode(predicateOperatorNode));
                result = invariant.render();
                // do stuff

                break;
            default:
                result = exceptionThrower(node);

        }
        return result;
    }

    private String processPredicateOperatorNode(PredicateOperatorNode node)
    {
        String result = "";
        if(node.getPredicateArguments().size() == 1){
            result = "";
            //ToDo ExprComparision and more
        }
        else{
            //System.out.println(node);
            ST nary_pred = currentGroup.getInstanceOf("nary_pred");
            TemplateHandler.add(nary_pred, "op", generateOperatorNAry(node));
            TemplateHandler.add(nary_pred, "statements", node.getPredicateArguments().stream()
                    .map(this::processPredicateNode)
                    .collect(Collectors.toList()));
            result = nary_pred.render();

        }

        return result;
    }


    private String generateOperatorNAry(PredicateOperatorNode node)
    {
        return "&amp;";
    }


    private String processOperatorPredOperatorExprArgs(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator){
        String result;
        switch (operator){
            case LESS:
                result ="&lt;";
                break;
            case GREATER:
                result ="&gt;";
                break;
            case EQUAL:
                result="&eq;";
                break;
            case NOT_EQUAL:
                result="&neq;";
                break;
            case GREATER_EQUAL:
                result="&gt;=";
                break;
            case LESS_EQUAL:
                result="&lt;=";
                break;
            case ELEMENT_OF:
                result=":";
                break;
            default:
                result = "";
                try {
                    throw new Exception(operator + " not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    protected String processDeclarationNode(DeclarationNode node)
    {
        ST id = currentGroup.getInstanceOf("id");
        TemplateHandler.add(id, "val", nameHandler.handleIdentifier(node.getName(),
                NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

        TemplateHandler.add(id, "typref", generateHash(node.getType()));

        nodeType.put(generateHash(node.getType()), node.getType());
        return id.render();
    }

    private int generateHash(BType type)
    {
        return Math.abs(type.toString().hashCode());
    }

}
