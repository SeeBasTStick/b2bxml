package de.hhu.stups.codegenerator.analyzers;

import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.nodes.expression.*;
import de.prob.parser.ast.nodes.ltl.LTLBPredicateNode;
import de.prob.parser.ast.nodes.ltl.LTLInfixOperatorNode;
import de.prob.parser.ast.nodes.ltl.LTLKeywordNode;
import de.prob.parser.ast.nodes.ltl.LTLPrefixOperatorNode;
import de.prob.parser.ast.nodes.predicate.*;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BasicType;
import de.prob.parser.ast.visitors.AbstractVisitor;


import java.util.HashMap;
import java.util.Map;

public class VariableTypAnalyzer implements AbstractVisitor<Void, Void> {

    private Map<BType, Integer> variableType = new HashMap<>();
    private Map<String, BType> nameType = new HashMap<>();


    public void visitMachineNode(MachineNode node) {

        node.getVariables().forEach(this::putInMap);



        node.getConstants().forEach(this::putInMap);

        if(node.getInitialisation() != null) {
            visitSubstitutionNode(node.getInitialisation(), null);
        }

        if(node.getProperties() != null) {
            visitPredicateNode(node.getProperties(), null);
        }

        if(node.getInvariant() != null) {
   //         System.out.println("Moving to Invariant: " + node.getInvariant());
            visitPredicateNode(node.getInvariant(), null);
        }

       // node.getValues().forEach(substitution -> visitSubstitutionNode(substitution, null));

   //     System.out.println(nameType);
     //   System.out.println(variableType);
    }

    private void putInMap(DeclarationNode node){
        if(node.getType() instanceof BasicType) {
            nameType.put(node.getName(), node.getType());
            if(!variableType.containsKey(node.getType()))
            {
                variableType.put(node.getType(), node.getType().hashCode());
            }
      //      System.out.println(node);
        }
    }


    @Override
    public Void visitIdentifierExprNode(IdentifierExprNode node, Void expected) {
        nameType.put(node.getName(), node.getType());
        if(!variableType.containsKey(node.getType()))
        {
            variableType.put(node.getType(), node.getType().hashCode());
        }

        return null;
    }

    @Override
    public Void visitExprNode(ExprNode node, Void expected) {
     //   System.out.println(node);

        return null;
    }

    @Override
    public Void visitExprOperatorNode(ExpressionOperatorNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitCastPredicateExpressionNode(CastPredicateExpressionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLBPredicateNode(LTLBPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIdentifierPredicateNode(IdentifierPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitPredicateOperatorNode(PredicateOperatorNode node, Void expected) {
       node.getPredicateArguments().forEach(predicateNode -> visitPredicateNode(predicateNode, null));
       return null;
    }

    @Override
    public Void visitQuantifiedPredicateNode(QuantifiedPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLetPredicateNode(LetPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIfPredicateNode(IfPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitPredicateOperatorWithExprArgs(PredicateOperatorWithExprArgsNode node, Void expected) {
     //   System.out.println("node expression" + node.getExpressionNodes() + "node oper "+    node.getOperator() + " node type " + node.getType());
        node.getExpressionNodes().forEach(exprNode -> visitExprNode(exprNode, null));
        return null;
    }


    @Override
    public Void visitQuantifiedExpressionNode(QuantifiedExpressionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLetExpressionNode(LetExpressionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIfExpressionNode(IfExpressionNode node, Void expected) {
        return null;
    }


    @Override
    public Void visitNumberNode(NumberNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitSetComprehensionNode(SetComprehensionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLambdaNode(LambdaNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitStringNode(StringNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitRecordNode(RecordNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitStructNode(StructNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitRecordFieldAccessNode(RecordFieldAccessNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLPrefixOperatorNode(LTLPrefixOperatorNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLKeywordNode(LTLKeywordNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLInfixOperatorNode(LTLInfixOperatorNode node, Void expected) {
        return null;
    }


    @Override
    public Void visitVarSubstitutionNode(VarSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitWhileSubstitutionNode(WhileSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitListSubstitutionNode(ListSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIfOrSelectSubstitutionsNode(IfOrSelectSubstitutionsNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitAssignSubstitutionNode(AssignSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitSkipSubstitutionNode(SkipSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitConditionSubstitutionNode(ConditionSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitAnySubstitution(AnySubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLetSubstitution(LetSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitBecomesElementOfSubstitutionNode(BecomesElementOfSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitBecomesSuchThatSubstitutionNode(BecomesSuchThatSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitSubstitutionIdentifierCallNode(OperationCallSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitChoiceSubstitutionNode(ChoiceSubstitutionNode node, Void expected) {
        return null;
    }
}
