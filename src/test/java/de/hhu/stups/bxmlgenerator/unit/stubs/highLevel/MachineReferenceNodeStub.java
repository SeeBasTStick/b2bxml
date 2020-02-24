package de.hhu.stups.bxmlgenerator.unit.stubs.highLevel;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.MachineReferenceNode;

public class MachineReferenceNodeStub extends MachineReferenceNode {
    public MachineReferenceNodeStub(String machineName, Kind kind) {
        super(new SourceCodePosition(), machineName, kind, "", false);
    }
}
