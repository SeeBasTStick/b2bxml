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

        String result;
        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorNode":
                PredicateOperatorNode predicateOperatorNode = (PredicateOperatorNode) node;
                ST invariant = super.getSTGroup().getInstanceOf("invariant");
                TemplateHandler.add(invariant, "body", processPredicateOperatorNode(predicateOperatorNode));
                result = invariant.render();
                // do stuff

                break;
            default:
                result = "";
                try {
                    throw new Exception(node.getClass().getSimpleName()+ " not yet implemented");
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            System.out.println(node);
            ST nary_pred = super.getSTGroup().getInstanceOf("nary_pred");
            TemplateHandler.add(nary_pred, "op", generateOperatorNAry(node));
            TemplateHandler.add(nary_pred, "statements", node.getPredicateArguments().stream()
                    .map(this::generatePredicateNode)
                    .collect(Collectors.toList()));
            result = nary_pred.render();

        }

        return result;
    }

    private String generatePredicateNode(PredicateNode node)
    {
        String result ;
        System.out.println("genPre "+ node);
        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorWithExprArgsNode":
                PredicateOperatorWithExprArgsNode predicateOperatorWithExprArgsNode = (PredicateOperatorWithExprArgsNode) node;
                ST expComparision = super.getSTGroup().getInstanceOf("exp_comparision");
                TemplateHandler.add(expComparision, "op",
                        generateOperatorPredOperatorExprArgs(predicateOperatorWithExprArgsNode.getOperator()));
                TemplateHandler.add(expComparision, "statements",
                        predicateOperatorWithExprArgsNode.getExpressionNodes().stream()
                                .map(this::processExprNode)
                                .collect(Collectors.toList()));

                result = expComparision.render();
                break;
            default:
                result = "";
                try {
                    throw new Exception(node.getClass().getSimpleName()+ " not yet implemented");
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        return result;
    }

    private String processExprNode(ExprNode node){
        System.out.println("proExpr " + node);
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

                TemplateHandler.add(binary_exp, "body",  node.getExpressionNodes().stream()
                        .map(this::processExprNode)
                        .collect(Collectors.toList()));

                super.getNodeTyp().put(node.getType().hashCode(), node.getType());
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

    private String generateOperatorNAry(PredicateOperatorNode node)
    {
        return "&amp;";
    }


}
