package de.hhu.stups.bxmlgenerator.unit.stubInterfaces.highLevel;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.types.BType;

public class DeclarationNodeStub extends DeclarationNode {
    public DeclarationNodeStub(BType type) {
        super(new SourceCodePosition(), "test", Kind.CONSTANT, new MachineNode(new SourceCodePosition()));
        super.setType(type);
    }
}
