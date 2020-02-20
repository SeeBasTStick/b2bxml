package de.hhu.stups.bxmlgenerator.unit.stubInterfaces;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.substitution.ConditionSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;

public class ConditionSubstitutionNodeStub extends ConditionSubstitutionNode {
    public ConditionSubstitutionNodeStub( ConditionSubstitutionNode.Kind kind) {
        super( new SourceCodePosition(), kind, new PredicateNodeStub(), new AssignSubstituteStub());
    }
}
