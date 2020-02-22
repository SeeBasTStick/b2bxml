package de.hhu.stups.bxmlgenerator.unit.stubs.highLevel;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.MachineNode;

public class MachineNodeStub extends MachineNode {

    public MachineNodeStub() {
        super(new SourceCodePosition());
        super.setName("TEST");
    }
}
