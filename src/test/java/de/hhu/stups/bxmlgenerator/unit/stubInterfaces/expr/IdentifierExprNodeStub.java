package de.hhu.stups.bxmlgenerator.unit.stubInterfaces.expr;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.types.BType;

public class IdentifierExprNodeStub extends IdentifierExprNode {
    public IdentifierExprNodeStub(String name, BType type) {
        super(new SourceCodePosition(), name, true);
        super.setType(type);
    }
}
