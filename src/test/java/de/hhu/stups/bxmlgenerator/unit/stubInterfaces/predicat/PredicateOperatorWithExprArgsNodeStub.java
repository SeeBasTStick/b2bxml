package de.hhu.stups.bxmlgenerator.unit.stubInterfaces.predicat;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;

import java.util.List;

public class PredicateOperatorWithExprArgsNodeStub extends PredicateOperatorWithExprArgsNode {
    public PredicateOperatorWithExprArgsNodeStub(PredOperatorExprArgs operator, List<ExprNode> expressionNodes) {
        super(new SourceCodePosition(), operator, expressionNodes);
    }

}
