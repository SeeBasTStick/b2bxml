package de.hhu.stups.bxmlgenerator.unit.stubs.substitution;

import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateOperatorNodeStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.substitution.ConditionSubstitutionNode;

import java.util.List;

public class ConditionSubstitutionNodeStub extends ConditionSubstitutionNode {
    public ConditionSubstitutionNodeStub( ConditionSubstitutionNode.Kind kind) {
        super( new SourceCodePosition(), kind,
                new PredicateOperatorNodeStub(PredicateOperatorNode.PredicateOperator.AND,
                        List.of(new PredicateNodeStub())), new AssignSubstituteNodeStub());
    }
}
