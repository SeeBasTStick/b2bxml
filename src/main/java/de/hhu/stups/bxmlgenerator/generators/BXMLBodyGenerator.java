package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.Node;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.expression.*;
import de.prob.parser.ast.nodes.ltl.*;
import de.prob.parser.ast.nodes.predicate.*;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.IntegerType;
import de.prob.parser.ast.types.SetType;
import de.prob.parser.ast.visitors.AbstractVisitor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;


import java.util.Map;
import java.util.stream.Collectors;

public abstract class BXMLBodyGenerator implements AbstractVisitor<String, Object> {

    public Map<Integer, BType> nodeType;


    public STGroup currentGroup;

    public BXMLBodyGenerator(Map<Integer, BType> nodeType,  STGroup currentGroup)
    {
        this.nodeType = nodeType;
        this.currentGroup = currentGroup;
    }

    public Map<Integer, BType> getNodeTyp(){
        return nodeType;
    }


    public STGroup getSTGroup(){ return currentGroup; }

    public String exceptionThrower(Node node){
        try {
            throw new Exception(node.getClass() + " is not implemented yet");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateOperatorNAry(PredicateOperatorNode node)
    {
        return "&amp;";
    }


    public String processOperatorPredOperatorExprArgs(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator){
        String result;
        switch (operator){
            case LESS:
                result ="&lt;";
                break;
            case GREATER:
                result ="&gt;";
                break;
            case EQUAL:
                result="&eq;";
                break;
            case NOT_EQUAL:
                result="&neq;";
                break;
            case GREATER_EQUAL:
                result="&gt;=";
                break;
            case LESS_EQUAL:
                result="&lt;=";
                break;
            case ELEMENT_OF:
                result=":";
                break;
            default:
                result = "";
                try {
                    throw new Exception(operator + " not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    public String processDeclarationNode(DeclarationNode node)
    {
        ST id = currentGroup.getInstanceOf("id");
        TemplateHandler.add(id, "val", node.getName());

        TemplateHandler.add(id, "typref", generateHash(node.getType()));

        nodeType.put(generateHash(node.getType()), node.getType());
        return id.render();
    }

    public int generateHash(BType type)
    {
        int hash = Math.abs(type.toString().hashCode());
        //Need strings here due to the fact that BTypes might be different instances...
        if(!nodeType.containsKey(hash) || nodeType.containsKey(hash) && nodeType.get(hash).toString().equals(type.toString()))
        {
            return hash;
        }
        else{
            throw new IndexOutOfBoundsException("Hash was already taken! " +type.toString() + " is not " + nodeType.get(hash));
        }
    }

    /*
     * Expr
     */
    @Override
    public String visitExprOperatorNode(ExpressionOperatorNode node, Object expected){
        String result;
        ST st;

        switch (node.getOperator()){
            case INTERVAL:
                st  = currentGroup.getInstanceOf("binary_exp");
                TemplateHandler.add(st, "op", "..");
                TemplateHandler.add(st, "typref", generateHash(node.getType()));
                TemplateHandler.add(st, "body",  node.getExpressionNodes().stream()
                        .map(workNode -> visitExprNode(workNode, null))
                        .collect(Collectors.toList()));
                break;

            case MINUS:
                st  = currentGroup.getInstanceOf("binary_exp");
                TemplateHandler.add(st, "op", "-");
                TemplateHandler.add(st, "typref", generateHash(node.getType()));
                TemplateHandler.add(st, "body",  node.getExpressionNodes().stream()
                        .map(workNode -> visitExprNode(workNode, null))
                        .collect(Collectors.toList()));
                break;

            case PLUS:
                st  = currentGroup.getInstanceOf("binary_exp");
                TemplateHandler.add(st, "op", "+");
                TemplateHandler.add(st, "typref", generateHash(node.getType()));
                TemplateHandler.add(st, "body",  node.getExpressionNodes().stream()
                        .map(workNode -> visitExprNode(workNode, null))
                        .collect(Collectors.toList()));
                break;

            case NAT:
                st = currentGroup.getInstanceOf("id");
                TemplateHandler.add(st, "val", "NAT");
                TemplateHandler.add(st, "typref", generateHash(new SetType(IntegerType.getInstance())));
                break;

            default:
                st  = currentGroup.getInstanceOf("st");
                try {
                    throw new Exception(node.getOperator() + " not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        nodeType.put(generateHash(node.getType()), node.getType());
        result = st.render();

        return result;
    }

    @Override
    public String visitIdentifierExprNode(IdentifierExprNode node, Object expected) {
        ST id = currentGroup.getInstanceOf("id");
        TemplateHandler.add(id, "val", node.getName());

        TemplateHandler.add(id, "typref", generateHash(node.getType()));

        nodeType.put(generateHash(node.getType()), node.getType());
        return id.render();
    }

    @Override
    public String visitCastPredicateExpressionNode(CastPredicateExpressionNode node, Object expected) {
        return null;
    }

    /*
     * Stuff
     */
    @Override
    public String visitNumberNode(NumberNode node, Object expected) {

        ST integer_literal = currentGroup.getInstanceOf("integer_literal");
        TemplateHandler.add(integer_literal, "val", node.getValue().toString());

        TemplateHandler.add(integer_literal, "typref", generateHash(node.getType()));

        nodeType.put(generateHash(node.getType()), node.getType());
        return integer_literal.render();

    }

    @Override
    public String visitQuantifiedExpressionNode(QuantifiedExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitSetComprehensionNode(SetComprehensionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLambdaNode(LambdaNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLetExpressionNode(LetExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitIfExpressionNode(IfExpressionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitStringNode(StringNode node, Object expected) {
        return null;
    }

    @Override
    public String visitRecordNode(RecordNode node, Object expected) {
        return null;
    }

    @Override
    public String visitStructNode(StructNode node, Object expected) {
        return null;
    }

    @Override
    public String visitRecordFieldAccessNode(RecordFieldAccessNode node, Object expected) {
        return null;
    }

    /*
     * Predicates
     */
    @Override
    public String visitIdentifierPredicateNode(IdentifierPredicateNode node, Object expected) {
        return "";
    }

    @Override
    public String visitPredicateOperatorNode(PredicateOperatorNode node, Object expected) {
        String result ;
        if(node.getPredicateArguments().size() == 1){
            result = "";
            //ToDo ExprComparision and more
        }
        else{
            ST nary_pred = currentGroup.getInstanceOf("nary_pred");
            TemplateHandler.add(nary_pred, "op", generateOperatorNAry(node));
            TemplateHandler.add(nary_pred, "statements", node.getPredicateArguments().stream()
                    .map(predicateNode -> visitPredicateNode(predicateNode, null))
                    .collect(Collectors.toList()));
            result = nary_pred.render();
        }

        return result;
    }

    @Override
    public String visitPredicateOperatorWithExprArgs(PredicateOperatorWithExprArgsNode node, Object expected) {
        ST expComparision = currentGroup.getInstanceOf("exp_comparision");
        TemplateHandler.add(expComparision, "op",
                processOperatorPredOperatorExprArgs(node.getOperator()));
        TemplateHandler.add(expComparision, "statements",
                node.getExpressionNodes().stream()
                        .map(workNode -> visitExprNode(workNode, null))
                        .collect(Collectors.toList()));
        return expComparision.render();
    }

    @Override
    public String visitQuantifiedPredicateNode(QuantifiedPredicateNode node, Object expected) {
        return "";
    }

    @Override
    public String visitLetPredicateNode(LetPredicateNode node, Object expected) {
        return "";
    }

    @Override
    public String visitIfPredicateNode(IfPredicateNode node, Object expected) {
        return "";
    }

    /*
     * Substitutions
     */
    @Override
    public String visitVarSubstitutionNode(VarSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitWhileSubstitutionNode(WhileSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitListSubstitutionNode(ListSubstitutionNode node, Object expected) {

        ST nary_sub = currentGroup.getInstanceOf("nary_sub");

        if(node.getOperator() == ListSubstitutionNode.ListOperator.Parallel)
        {
            TemplateHandler.add(nary_sub, "op", "||");
        }else{
            TemplateHandler.add(nary_sub, "op", ";");

        }
        TemplateHandler.add(nary_sub, "statements", node.getSubstitutions().stream()
                .map(subNode -> visitSubstitutionNode(subNode, null))
                .collect(Collectors.toList()));
        return nary_sub.render();
    }

    @Override
    public String visitIfOrSelectSubstitutionsNode(IfOrSelectSubstitutionsNode node, Object expected) {
        String result;
        if(node.getOperator() == IfOrSelectSubstitutionsNode.Operator.IF)
        {
            result = exceptionThrower(node);
        }
        else{

            ST select = currentGroup.getInstanceOf("select");
            TemplateHandler.add(select, "conditions", node.getConditions().stream()
                    .map(predicateNode -> visitPredicateNode(predicateNode, null))
                    .collect(Collectors.toList()));


            TemplateHandler.add(select, "then", node.getSubstitutions().stream()
                    .map(subNode -> visitSubstitutionNode(subNode, null))
                    .collect(Collectors.toList()));

            result = select.render();
        }
        return result;
    }

    @Override
    public String visitAssignSubstitutionNode(AssignSubstitutionNode node, Object expected) {
        ST assignSub = currentGroup.getInstanceOf("assignment_sub");
        TemplateHandler.add(assignSub, "body1", node.getLeftSide().stream()
                .map(workNode -> visitExprNode(workNode, null))
                .collect(Collectors.toList()));

        TemplateHandler.add(assignSub, "body2", node.getRightSide().stream()
                .map(workNode -> visitExprNode(workNode, null))
                .collect(Collectors.toList()));

        return assignSub.render();

    }

    @Override
    public String visitSkipSubstitutionNode(SkipSubstitutionNode node, Object expected) {
        ST skipSub = currentGroup.getInstanceOf("skip");
        TemplateHandler.add(skipSub, "", null);
        return skipSub.render();
    }

    @Override
    public String visitConditionSubstitutionNode(ConditionSubstitutionNode node, Object expected) {
        if(node.getKind() == ConditionSubstitutionNode.Kind.ASSERT){
            ST assertSub = currentGroup.getInstanceOf("assert_sub");
            TemplateHandler.add(assertSub, "guard", visitPredicateNode(node.getCondition(), null));
            TemplateHandler.add(assertSub, "body", visitSubstitutionNode(node.getSubstitution(), null));
            return assertSub.render();
        }
        else{
            /*
             * PRE_sub is a undocumented feature of the bxml standard of atelierB - 20.02.2020
             */
            ST preSub = currentGroup.getInstanceOf("pre_sub");
            TemplateHandler.add(preSub, "precondition", visitPredicateNode(node.getCondition(), null));
            TemplateHandler.add(preSub, "body", visitSubstitutionNode(node.getSubstitution(), null));
            return preSub.render();
        }
    }

    @Override
    public String visitAnySubstitution(AnySubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLetSubstitution(LetSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitBecomesElementOfSubstitutionNode(BecomesElementOfSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitBecomesSuchThatSubstitutionNode(BecomesSuchThatSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitSubstitutionIdentifierCallNode(OperationCallSubstitutionNode node, Object expected) {
        return null;
    }

    @Override
    public String visitChoiceSubstitutionNode(ChoiceSubstitutionNode node, Object expected) {
        return null;
    }

    /*
     * Not used
     */
    @Override
    public String visitNode(Node node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLNode(LTLNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLPrefixOperatorNode(LTLPrefixOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLKeywordNode(LTLKeywordNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLInfixOperatorNode(LTLInfixOperatorNode node, Object expected) {
        return null;
    }

    @Override
    public String visitLTLBPredicateNode(LTLBPredicateNode node, Object expected) {
        return null;
    }
}
