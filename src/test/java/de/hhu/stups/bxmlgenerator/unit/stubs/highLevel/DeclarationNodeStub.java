package de.hhu.stups.bxmlgenerator.unit.stubs.highLevel;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.types.BType;

public class DeclarationNodeStub extends DeclarationNode {
    public DeclarationNodeStub(BType type) {
        super(new SourceCodePosition(), "test", Kind.CONSTANT, new MachineNodeStub());
        super.setType(type);
    }
}
