package de.hhu.stups.bxmlgenerator.unit.stubs.expr;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.types.BType;

public class ExprOperatorNodeStub extends ExpressionOperatorNode {



    public ExprOperatorNodeStub(ExpressionOperator operator, BType type) {
        super(new SourceCodePosition(), operator);
        super.setType(type);
    }
}
