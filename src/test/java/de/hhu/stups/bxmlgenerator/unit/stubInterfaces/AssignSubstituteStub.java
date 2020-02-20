package de.hhu.stups.bxmlgenerator.unit.stubInterfaces;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;

import java.util.List;

public class AssignSubstituteStub extends AssignSubstitutionNode {
    public AssignSubstituteStub() {
        super(new SourceCodePosition(), List.of(new ExprNodeStub()), List.of(new ExprNodeStub()));
    }
}
