package de.hhu.stups.bxmlgenerator.unit.stubs.highLevel;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;

import java.util.List;

public class OperationNodeStub extends OperationNode {
    public OperationNodeStub(String name, List<DeclarationNode> outputParamNodes, SubstitutionNode substitution, List<DeclarationNode> paramNodes) {
        super(new SourceCodePosition(), name, outputParamNodes, substitution, paramNodes);
    }
}
