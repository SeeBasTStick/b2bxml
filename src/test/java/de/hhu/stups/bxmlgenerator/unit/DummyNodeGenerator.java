package de.hhu.stups.bxmlgenerator.unit;

import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BoolType;

import java.util.List;

public abstract class DummyNodeGenerator {

    protected IdentifierExprNode dummy_IdentifierExprNodeGenerator(BType type, String name ){
        IdentifierExprNode result = new IdentifierExprNode(new SourceCodePosition(), name, true);
        result.setType(type);
        return result;
    }

    protected ExpressionOperatorNode dummy_ExpressionOperatorNodeGenerator(
            BType type, ExpressionOperatorNode.ExpressionOperator operator ){
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(), operator);
        expressionOperatorNode.setType(type);
        return expressionOperatorNode;
    }

    protected AssignSubstitutionNode dummy_AssignSubstitutionNodeGenerator(){
        return new AssignSubstitutionNode(new SourceCodePosition(),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test")),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test2")));
    }

    protected DeclarationNode dummy_DeclarationNodeGenerator(BType type){
        DeclarationNode declarationNode = new DeclarationNode(new SourceCodePosition(), "test",
                DeclarationNode.Kind.CONSTANT, new MachineNode(new SourceCodePosition()));
        declarationNode.setType(type);
        return declarationNode;
    }

}
