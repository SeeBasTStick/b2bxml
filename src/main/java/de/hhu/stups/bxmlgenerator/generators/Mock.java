package de.hhu.stups.bxmlgenerator.generators;

import de.prob.parser.ast.nodes.expression.*;
import de.prob.parser.ast.nodes.ltl.LTLBPredicateNode;
import de.prob.parser.ast.nodes.ltl.LTLInfixOperatorNode;
import de.prob.parser.ast.nodes.ltl.LTLKeywordNode;
import de.prob.parser.ast.nodes.ltl.LTLPrefixOperatorNode;
import de.prob.parser.ast.nodes.predicate.*;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.visitors.AbstractVisitor;

public class Mock implements AbstractVisitor<String, Object> {
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
        return null;
    }

    @Override
    public String visitWhileSubstitutionNode(WhileSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitListSubstitutionNode(ListSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIfOrSelectSubstitutionsNode(IfOrSelectSubstitutionsNode node, Object expected) {
        return null;
    }

    @Override
    public String visitAssignSubstitutionNode(AssignSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitSkipSubstitutionNode(SkipSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitConditionSubstitutionNode(ConditionSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitAnySubstitution(AnySubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLetSubstitution(LetSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitBecomesElementOfSubstitutionNode(BecomesElementOfSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitBecomesSuchThatSubstitutionNode(BecomesSuchThatSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitSubstitutionIdentifierCallNode(OperationCallSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitChoiceSubstitutionNode(ChoiceSubstitutionNode node, Object expected) {
        return null;
    }
}
