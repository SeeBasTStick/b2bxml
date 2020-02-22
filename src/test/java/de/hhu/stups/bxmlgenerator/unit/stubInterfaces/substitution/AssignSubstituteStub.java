package de.hhu.stups.bxmlgenerator.unit.stubInterfaces.substitution;

import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.expr.IdentifierExprNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.expr.NumberNodeStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.types.BoolType;
import de.prob.parser.ast.types.IntegerType;

import java.util.List;

public class AssignSubstituteStub extends AssignSubstitutionNode {

    public AssignSubstituteStub() {
        super(new SourceCodePosition(),
                List.of(new IdentifierExprNodeStub("ii", IntegerType.getInstance())),
                List.of(new NumberNodeStub(42)));
    }
}
