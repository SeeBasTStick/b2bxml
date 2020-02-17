package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.expression.*;
import de.prob.parser.ast.nodes.ltl.LTLBPredicateNode;
import de.prob.parser.ast.nodes.ltl.LTLInfixOperatorNode;
import de.prob.parser.ast.nodes.ltl.LTLKeywordNode;
import de.prob.parser.ast.nodes.ltl.LTLPrefixOperatorNode;
import de.prob.parser.ast.nodes.predicate.*;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.visitors.AbstractVisitor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsGenerator extends BXMLBodyGenerator implements AbstractVisitor<String, Object> {

    ST operation;

    public OperationsGenerator(Map<Integer, BType> nodeType,  STGroup currentGroup) {
        super(nodeType,  currentGroup);
    }

    public String generateOperations(List<OperationNode> nodeList){
        ST operations = super.getSTGroup().getInstanceOf("operations");
        TemplateHandler.add(operations, "operation", nodeList.stream()
                .map(this::generateOperation)
                .collect(Collectors.toList()));
        return operations.render();
    }

    /*
     * node.getSub...() -> conditionSubstitutionNode ist + operator = pre
     * test if atelier b can do assert
     */

    public String generateOperation(OperationNode node){

        operation = super.getSTGroup().getInstanceOf("operation");

        TemplateHandler.add(operation, "name", node.getName());

        if(!node.getOutputParams().isEmpty())
        {
            ST outputParameters = super.getSTGroup().getInstanceOf("output_parameters");
            TemplateHandler.add(outputParameters, "body", node.getOutputParams().stream()
                    .map(this::processDeclarationNode)
                    .collect(Collectors.toList()));

            TemplateHandler.add(operation, "output_parameters", outputParameters.render());
        }

        if(!node.getParams().isEmpty()) {
            System.out.println(node.getParams() + " operation not implemented yet");
        }


        /*
         * Case distinction ASSERT und PRE
         */
        return visitSubstitutionNode(node.getSubstitution(), null);
    }

    @Override
    public String visitExprOperatorNode(ExpressionOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIdentifierExprNode(IdentifierExprNode node, Object expected) {
        return null;
    }

    @Override
    public String visitCastPredicateExpressionNode(CastPredicateExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitNumberNode(NumberNode node, Object expected) {
        return null;
    }

    @Override
    public String visitQuantifiedExpressionNode(QuantifiedExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitSetComprehensionNode(SetComprehensionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLambdaNode(LambdaNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLetExpressionNode(LetExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIfExpressionNode(IfExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitStringNode(StringNode node, Object expected) {
        return null;
    }

    @Override
    public String visitRecordNode(RecordNode node, Object expected) {
        return null;
    }

    @Override
    public String visitStructNode(StructNode node, Object expected) {
        return null;
    }

    @Override
    public String visitRecordFieldAccessNode(RecordFieldAccessNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLPrefixOperatorNode(LTLPrefixOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLKeywordNode(LTLKeywordNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLInfixOperatorNode(LTLInfixOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLBPredicateNode(LTLBPredicateNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIdentifierPredicateNode(IdentifierPredicateNode node, Object expected) {
        return null;
    }

    @Override
    public String visitPredicateOperatorNode(PredicateOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitPredicateOperatorWithExprArgs(PredicateOperatorWithExprArgsNode node, Object expected) {
        return null;
    }

    @Override
    public String visitQuantifiedPredicateNode(QuantifiedPredicateNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLetPredicateNode(LetPredicateNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIfPredicateNode(IfPredicateNode node, Object expected) {
        return null;
    }

    @Override
    public String visitVarSubstitutionNode(VarSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitWhileSubstitutionNode(WhileSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitListSubstitutionNode(ListSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitIfOrSelectSubstitutionsNode(IfOrSelectSubstitutionsNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitAssignSubstitutionNode(AssignSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitSkipSubstitutionNode(SkipSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitConditionSubstitutionNode(ConditionSubstitutionNode node, Object expected) {
        if(node.getKind() == ConditionSubstitutionNode.Kind.PRECONDITION){
            ST precondition = super.getSTGroup().getInstanceOf("precondition");
            TemplateHandler.add(precondition, "body", processPredicateNode(node.getCondition()));
            TemplateHandler.add(operation, "precondition" , precondition.render());
        }
        return handleBody(node.getSubstitution());
    }

    @Override
    public String visitAnySubstitution(AnySubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitLetSubstitution(LetSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitBecomesElementOfSubstitutionNode(BecomesElementOfSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitBecomesSuchThatSubstitutionNode(BecomesSuchThatSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitSubstitutionIdentifierCallNode(OperationCallSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    @Override
    public String visitChoiceSubstitutionNode(ChoiceSubstitutionNode node, Object expected) {
        return handleBody(node);
    }

    public String handleBody(SubstitutionNode node){
        TemplateHandler.add(operation, "body" , processSubstitutionNode(node));
        return operation.render();
    }
}
