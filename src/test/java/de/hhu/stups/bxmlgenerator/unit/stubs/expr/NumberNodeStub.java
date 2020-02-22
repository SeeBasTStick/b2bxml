package de.hhu.stups.bxmlgenerator.unit.stubs.expr;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.types.IntegerType;

import java.math.BigInteger;

public class NumberNodeStub extends NumberNode {
    public NumberNodeStub(int value) {
        super(new SourceCodePosition(), BigInteger.valueOf(value));
        super.setType(IntegerType.getInstance());
    }
}
