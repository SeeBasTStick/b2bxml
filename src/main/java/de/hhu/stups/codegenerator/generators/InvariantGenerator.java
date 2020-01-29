package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Map;
import java.util.stream.Collectors;

public class InvariantGenerator extends BXMLBodyGenerator {
    public InvariantGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateInvariants(PredicateNode node){

        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorNode":
                PredicateOperatorNode predicateOperatorNode = (PredicateOperatorNode) node;
                processPredicateOperatorNode(predicateOperatorNode);
                // do stuff

                break;
            default:
                try {
                    throw new Exception(node.getClass().getSimpleName()+ " not yet implemented");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }


        return "";
    }

    private void processPredicateOperatorNode(PredicateOperatorNode node)
    {
        if(node.getPredicateArguments().size() == 1){
            //ToDo ExprComparision and more
        }
        else{
            ST nary_pred = super.getSTGroup().getInstanceOf("nary_pred");
            ST op = super.getSTGroup().getInstanceOf("op");
            TemplateHandler.add(op, "op", generateOperatorNAry(node));
            TemplateHandler.add(nary_pred, "exp_comparisons", node.getPredicateArguments().stream()
                    .map(this::generateInvariant)
                    .collect(Collectors.toList()));
        }
    }

    private String generateInvariant(PredicateNode node)
    {
        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorWithExprArgsNode":
                PredicateOperatorWithExprArgsNode predicateOperatorWithExprArgsNode = (PredicateOperatorWithExprArgsNode) node;
                ST op = super.getSTGroup().getInstanceOf("op");
                TemplateHandler.add(op, "op", generateOperatorPredOperatorExprArgs(predicateOperatorWithExprArgsNode.getOperator()));

                predicateOperatorWithExprArgsNode.getExpressionNodes().stream().map(this::processExprNode).collect(Collectors.toList());


                ST binary_exp = super.getSTGroup().getInstanceOf("binary_exp");
                ST integer_literal = super.getSTGroup().getInstanceOf("integer_literals");

        }
        return "";
    }

    private String processExprNode(ExprNode node){
        System.out.println(node.getClass());
        String result;

        switch (node.getClass().getSimpleName()){
            case "IdentifierExprNode":
                IdentifierExprNode identifierExprNode = (IdentifierExprNode) node;
                ST id = super.getSTGroup().getInstanceOf("id");
                TemplateHandler.add(id, "val", super.getNameHandler().handleIdentifier(identifierExprNode.getName(),
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

                TemplateHandler.add(id, "typref", identifierExprNode.getType().hashCode());

                super.getNodeTyp().put(identifierExprNode.getType().hashCode(), identifierExprNode.getType());
                result = id.render();
                break;
            case "ExpressionOperatorNode":
                ExpressionOperatorNode expressionOperatorNode = (ExpressionOperatorNode) node;
                result = processExpressionOperatorNode(expressionOperatorNode);
                break;
            case "NumberNode":
                NumberNode numberNode = (NumberNode) node;
                ST integer_literal = super.getSTGroup().getInstanceOf("integer_literal");
                TemplateHandler.add(integer_literal, "val", super.getNameHandler().handleIdentifier(numberNode.getValue().toString(),
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

                TemplateHandler.add(integer_literal, "typref", numberNode.getType().hashCode());

                super.getNodeTyp().put(numberNode.getType().hashCode(), numberNode.getType());
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

    private String processExpressionOperatorNode(ExpressionOperatorNode node){

        String result = "";

        switch (node.getOperator()){
            case INTERVAL:
                ST binary_exp = super.getSTGroup().getInstanceOf("binary_exp");
                TemplateHandler.add(binary_exp, "op", super.getNameHandler().handleIdentifier("..",
                        NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

                TemplateHandler.add(binary_exp, "typref", node.getType().hashCode());
                super.getNodeTyp().put(node.getType().hashCode(), node.getType());

                node.getExpressionNodes().stream().map(this::processExprNode).collect(Collectors.toList());

                result = binary_exp.render();
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

    private String generateOperatorPredOperatorExprArgs(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator){
        String result;
        switch (operator){
            case LESS:
                result ="&lt";
                break;
            case GREATER:
                result ="&gt";
                break;
            case EQUAL:
                result="&eq";
                break;
            case NOT_EQUAL:
                result="&neq";
                break;
            case GREATER_EQUAL:
                result="&geq";
                break;
            case LESS_EQUAL:
                result="&leq";
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

    private String generateOperatorNAry(PredicateOperatorNode node)
    {
        return "&amp;";
    }


}
