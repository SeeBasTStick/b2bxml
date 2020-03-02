package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.antlr.BXMLBodyGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubs.expr.IdentifierExprNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.highLevel.DeclarationNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateOperatorNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateOperatorWithExprArgsNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.substitution.AssignSubstituteNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.substitution.ConditionSubstitutionNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.expr.ExprOperatorNodeStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BoolType;
import de.prob.parser.ast.types.IntegerType;
import de.prob.parser.ast.types.SetType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BXMLBodyGeneratorTest {

    StubGenerator stubGenerator;

    protected static class StubGenerator extends BXMLBodyGenerator {

        public StubGenerator(Map<Integer, BType> nodeType, STGroup currentGroup) {
            super(nodeType,  currentGroup);
        }
    }


    @Before
    public void prepare(){
      prepare(new HashMap<>());
    }

    public void prepare(HashMap<Integer, BType> nodeType){
        stubGenerator = new StubGenerator(nodeType,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }


    /*
     * Node that the hash in typref is always the same due to the fact that node.getType().toString().hash() always
     * produces the same result.
     */

    @Test
    public void test_visitExprNode_IdentifierExprNode() {
        IdentifierExprNode identifierExprNode = new IdentifierExprNodeStub( "test", BoolType.getInstance());

        String result = stubGenerator.visitExprNode(identifierExprNode, null);

        assertEquals("<Id value='test' typref='2044650'/>", result);
    }

    @Test
    public void test_visitExprNode_NumberNode(){
        NumberNode numberNode = new NumberNode(new SourceCodePosition(), BigInteger.ONE);
        BType type = BoolType.getInstance();
        numberNode.setType(type);
        assertEquals(stubGenerator.visitExprNode(numberNode, null), "<Integer_Literal value='1' typref='2044650'/>");
    }

    @Test
    public void test_visitExpressionOperatorNode_INTERVAL()
    {
        assertEquals(stubGenerator.visitExprNode(
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.INTERVAL, BoolType.getInstance()), null),
                "<Binary_Exp op='..' typref='2044650'>\n</Binary_Exp>");
    }

    @Test
    public void test_visitExprNode_ExpressionOperatorNode_Plus() {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.PLUS, BoolType.getInstance());

        String result = stubGenerator.visitExprNode(expressionOperatorNode, null);

        assertEquals("<Binary_Exp op='+' typref='2044650'>\n</Binary_Exp>", result);
    }

    @Test
    public void test_visitExprNode_visitExpressionOperatorNode_Minus()
    {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.MINUS, BoolType.getInstance());
        assertEquals(stubGenerator.visitExprNode(expressionOperatorNode, null),
                "<Binary_Exp op='-' typref='2044650'>\n</Binary_Exp>");

    }

    @Test
    public void test_visitExprNode_visitExpressionOperatorNode_NAT()
    {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.NAT, BoolType.getInstance());

        String result = stubGenerator.visitExprNode(expressionOperatorNode, null);

        assertEquals( "<Id value='NAT' typref='631359557'/>", result);
    }

    @Test
    public void test_visitExprNode_visitExpressionOperatorNode_BOOL()
    {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.BOOL, BoolType.getInstance());

        String result = stubGenerator.visitExprNode(expressionOperatorNode, null);

        assertEquals("<Id value='BOOL' typref='2044650'/>", result);
    }

    @Test
    public void test_visitExprNode_visitExpressionOperatorNode_TRUE()
    {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.TRUE, BoolType.getInstance());

        String result = stubGenerator.visitExprNode(expressionOperatorNode, null);

        assertEquals("<Boolean_Literal value='TRUE' typref='2044650'/>", result);

    }

    @Test
    public void test_visitExprNode_visitExpressionOperatorNode_FALSE()
    {
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.FALSE, BoolType.getInstance());

        String result = stubGenerator.visitExprNode(expressionOperatorNode, null);

        assertEquals("<Boolean_Literal value='FALSE' typref='2044650'/>", result);

    }

    @Test
    public void test_visitIfOrSelectSubstitutionsNode_SELECT(){

        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.MINUS, BoolType.getInstance());

        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNodeStub(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));

        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstituteNodeStub();

        IfOrSelectSubstitutionsNode ifOrSelectSubstitutionsNode = new IfOrSelectSubstitutionsNode(new SourceCodePosition(),
                IfOrSelectSubstitutionsNode.Operator.SELECT, List.of(predicateNode), List.of(assignSubstitutionNode),
                null);

        assertEquals("<Select>\n" +
                "    <When_Clauses>\n" +
                "        <When>\n" +
                "            <Condition>\n" +
                "                <Exp_Comparison op=':'>\n" +
                "                    <Binary_Exp op='-' typref='2044650'>\n" +
                "                    </Binary_Exp>\n" +
                "                </Exp_Comparison>\n" +
                "            </Condition>\n" +
                "            <Then>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='ii' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='42' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Then>\n" +
                "        </When>\n" +
                "    </When_Clauses>\n" +
                "</Select>", stubGenerator.visitSubstitutionNode(ifOrSelectSubstitutionsNode, null));

    }

    @Test
    public void test_visitSubstitutionNode_ListSubstitutionNode_Parallel(){
        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstituteNodeStub();
        ListSubstitutionNode listSubstitutionNode = new ListSubstitutionNode(new SourceCodePosition(),
                ListSubstitutionNode.ListOperator.Parallel, List.of(assignSubstitutionNode));

        assertEquals("<Nary_Sub op='||'>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='ii' typref='1618932450'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Integer_Literal value='42' typref='1618932450'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Nary_Sub>", stubGenerator.visitSubstitutionNode(listSubstitutionNode, null));
    }

    @Test
    public void test_visitSubstitutionNode_ListSubstitutionNode_Sequential(){
        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstituteNodeStub();
        ListSubstitutionNode listSubstitutionNode = new ListSubstitutionNode(new SourceCodePosition(),
                ListSubstitutionNode.ListOperator.Sequential, List.of(assignSubstitutionNode));

        assertEquals("<Nary_Sub op=';'>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='ii' typref='1618932450'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Integer_Literal value='42' typref='1618932450'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Nary_Sub>", stubGenerator.visitSubstitutionNode(listSubstitutionNode, null));
    }

    @Test
    public void test_visitSubstitutionNode_AssignSubstitutionNode(){

        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstituteNodeStub();

        assertEquals("<Assignement_Sub>\n" +
                "    <Variables>\n" +
                "        <Id value='ii' typref='1618932450'/>\n" +
                "    </Variables>\n" +
                "    <Values>\n" +
                "        <Integer_Literal value='42' typref='1618932450'/>\n" +
                "    </Values>\n" +
                "</Assignement_Sub>", stubGenerator.visitSubstitutionNode(assignSubstitutionNode, null));
    }

    @Test
    public void test_visitSubstitutionNode_SkipSubstitutionNode(){

        SkipSubstitutionNode assignSubstitutionNode = new SkipSubstitutionNode(new SourceCodePosition());

        assertEquals("<Skip/>", stubGenerator.visitSubstitutionNode(assignSubstitutionNode, null));
    }


    @Test
    public void test_visitPredicateNode_PredicateOperatorNode(){

        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.MINUS, BoolType.getInstance());

        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));

        PredicateOperatorNode predicateOperatorNode = new PredicateOperatorNode(new SourceCodePosition(),
                PredicateOperatorNode.PredicateOperator.AND, List.of(predicateNode));

        String result =  stubGenerator.visitPredicateNode(predicateOperatorNode, null);

        assertEquals("", result);

    }

    @Test
    public void test_visitPredicateNode_PredicateOperatorWithExprArgsNode(){
        BType type = BoolType.getInstance();

        ExpressionOperatorNode expressionOperatorNode =
        new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.MINUS, BoolType.getInstance());

        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNodeStub(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));
        predicateNode.setType(type);

        String result = stubGenerator.visitPredicateNode(predicateNode, null);

        assertEquals("<Exp_Comparison op=':'>\n" +
                "    <Binary_Exp op='-' typref='2044650'>\n" +
                "    </Binary_Exp>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_visitPredicateOperatorNode(){

        PredicateOperatorNode predicateOperatorNode = new PredicateOperatorNodeStub(
                PredicateOperatorNode.PredicateOperator.AND,
                List.of(new PredicateOperatorNodeStub(
                        PredicateOperatorNode.PredicateOperator.AND, List.of(new PredicateNodeStub())),
                        new PredicateOperatorNodeStub(
                                PredicateOperatorNode.PredicateOperator.AND, List.of(new PredicateNodeStub()))));

        String result = stubGenerator.visitPredicateNode(predicateOperatorNode, null);

        assertEquals("<Nary_Pred op='&amp;'>\n" +
                "</Nary_Pred>",result);
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_LESS(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.LESS), "&lt;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_GREATER(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.GREATER), "&gt;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_EQUAL(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.EQUAL), "=");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_NOT_EQUAL(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.NOT_EQUAL), "&neq;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_ELEMENT_OF(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF), ":");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_LESS_EQUAL(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.LESS_EQUAL), "&lt;=");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_GREATER_EQUAL(){
        assertEquals(stubGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.GREATER_EQUAL), "&gt;=");
    }

    @Test
    public void test_processDeclarationNode(){
        assertEquals("<Id value='test' typref='2044650'/>",
                stubGenerator.processDeclarationNode(new DeclarationNodeStub(BoolType.getInstance())));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_hashFunction_1(){
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Hash was already taken! BOOL is not INTEGER");
        BType type = BoolType.getInstance();
        int hash = stubGenerator.generateHash(type);
        BType crashType = IntegerType.getInstance();
        stubGenerator.getNodeTyp().put(hash, crashType);
        stubGenerator.generateHash(type);

    }

    @Test
    public void test_hashFunction_2(){
        HashMap<Integer, BType> dummy = new HashMap<>();
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Hash was already taken! INTEGER is not BOOL");
        BType type = BoolType.getInstance();
        BType crashType = IntegerType.getInstance();
        int hash = Math.abs(crashType.toString().hashCode());

        dummy.put(hash, type);
        prepare(dummy);
        stubGenerator.generateHash(crashType);
    }

    @Test
    public void test_hashFunction_3(){
        BType type = BoolType.getInstance();
        assertEquals(Math.abs(type.toString().hashCode()), stubGenerator.generateHash(type));
    }

    @Test
    public void test_hashFunction_4(){
        HashMap<Integer, BType> dummy = new HashMap<>();
        BType type = new SetType(IntegerType.getInstance());
        BType crashType = new SetType(IntegerType.getInstance());
        int hash = Math.abs(crashType.toString().hashCode());

        dummy.put(hash, type);
        prepare(dummy);
        stubGenerator.generateHash(crashType);

        assertEquals(Math.abs(type.toString().hashCode()), stubGenerator.generateHash(type));

    }

    @Test
    public void test_visitConditionSubstitutionNode_Assert(){
        ConditionSubstitutionNode conditionSubstitutionNode =
                new ConditionSubstitutionNodeStub(ConditionSubstitutionNode.Kind.ASSERT);

        String result = stubGenerator.visitConditionSubstitutionNode(conditionSubstitutionNode, null);

        assertEquals("<Assert_Sub>\n" +
                "    <Guard>\n" +
                "    </Guard>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='ii' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='42' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</Assert_Sub>", result);
    }

    @Test
    public void test_visitConditionSubstitutionNode_Precondition(){
        ConditionSubstitutionNode conditionSubstitutionNode =
                new ConditionSubstitutionNodeStub(ConditionSubstitutionNode.Kind.PRECONDITION);

        String result = stubGenerator.visitConditionSubstitutionNode(conditionSubstitutionNode, null);

        assertEquals("<PRE_Sub>\n" +
                "    <Precondition>\n" +
                "    </Precondition>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='ii' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='42' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</PRE_Sub>", result);
    }


}
