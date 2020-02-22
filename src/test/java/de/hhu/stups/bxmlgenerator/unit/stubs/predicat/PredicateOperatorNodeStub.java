package de.hhu.stups.bxmlgenerator.unit.stubs.predicat;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;

import java.util.List;

public class PredicateOperatorNodeStub extends PredicateOperatorNode {
    public PredicateOperatorNodeStub(PredicateOperator operator, List<PredicateNode> predicateArguments) {
        super(new SourceCodePosition(), operator, predicateArguments);
    }
}
