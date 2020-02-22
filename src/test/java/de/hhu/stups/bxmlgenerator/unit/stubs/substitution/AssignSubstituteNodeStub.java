package de.hhu.stups.bxmlgenerator.unit.stubs.substitution;

import de.hhu.stups.bxmlgenerator.unit.stubs.expr.IdentifierExprNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.expr.NumberNodeStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.types.IntegerType;

import java.util.List;

public class AssignSubstituteNodeStub extends AssignSubstitutionNode {

    public AssignSubstituteNodeStub() {
        super(new SourceCodePosition(),
                List.of(new IdentifierExprNodeStub("ii", IntegerType.getInstance())),
                List.of(new NumberNodeStub(42)));
    }
}
