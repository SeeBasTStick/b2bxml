package de.hhu.stups.codegenerator.unit;

import de.hhu.stups.codegenerator.generators.BXMLBodyGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.IfOrSelectSubstitutionsNode;
import de.prob.parser.ast.nodes.substitution.ListSubstitutionNode;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BoolType;
import de.prob.parser.ast.types.IntegerType;
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

    DummyGenerator dummyGenerator;

    protected static class DummyGenerator extends BXMLBodyGenerator {

        public DummyGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
            super(nodeType, nameHandler, currentGroup);
        }
    }


    private IdentifierExprNode dummy_IdentifierExprNodeGenerator(BType type, String name ){
        IdentifierExprNode result = new IdentifierExprNode(new SourceCodePosition(), name, true);
        result.setType(type);
        return result;
    }

    private ExpressionOperatorNode dummy_ExpressionOperatorNodeGenerator(
            BType type, ExpressionOperatorNode.ExpressionOperator operator ){
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(), operator);
        expressionOperatorNode.setType(type);
        return expressionOperatorNode;
    }

    private ExprNode dummy_ExprNodeGenerator(
            BType type, ExpressionOperatorNode.ExpressionOperator operator ){
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(), operator);
        expressionOperatorNode.setType(type);
        return expressionOperatorNode;
    }

    @Before
    public void prepare(){
        dummyGenerator = new DummyGenerator(new HashMap<Integer, BType>(),
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    /*
     * Node that the hash in typref is always the same due to the fact that node.getType().toString().hash() always
     * produces the same result.
     */

    @Test
    public void test_processExprNode_IdentifierExprNode() {
        IdentifierExprNode identifierExprNode =
                new IdentifierExprNode(new SourceCodePosition(), "test", true);
        BType type = BoolType.getInstance();
        identifierExprNode.setType(type);
        assertEquals(dummyGenerator.processExprNode(identifierExprNode), "<Id value='test' typref='2044650'/>");
    }

    @Test
    public void test_processExprNode_ExpressionOperatorNode() {
        /*The original is only a dispatcher (see code) we will do only a test to see if dispatching works, the rest
         * will be checked down further
         */
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.PLUS);
        BType type = BoolType.getInstance();
        expressionOperatorNode.setType(type);
        assertEquals(dummyGenerator.processExprNode(expressionOperatorNode),
                "<Binary_Exp op='+' typref='2044650'>\n</Binary_Exp>");
    }

    @Test
    public void test_processExprNode_NumberNode(){
        NumberNode numberNode = new NumberNode(new SourceCodePosition(), BigInteger.ONE);
        BType type = BoolType.getInstance();
        numberNode.setType(type);
        assertEquals(dummyGenerator.processExprNode(numberNode), "<Integer_Literal value='1' typref='2044650'/>");
    }

    @Test
    public void test_processExpressionOperatorNode_INTERVAL()
    {

        assertEquals(dummyGenerator.processExpressionOperatorNode(
                dummy_ExpressionOperatorNodeGenerator(BoolType.getInstance(),
                        ExpressionOperatorNode.ExpressionOperator.INTERVAL)),
                "<Binary_Exp op='..' typref='2044650'>\n</Binary_Exp>");
    }

    @Test
    public void test_processExpressionOperatorNode_Plus()
    {
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.PLUS);
        BType type = BoolType.getInstance();
        expressionOperatorNode.setType(type);
        assertEquals(dummyGenerator.processExpressionOperatorNode(expressionOperatorNode),
                "<Binary_Exp op='+' typref='2044650'>\n</Binary_Exp>");

    }

    @Test
    public void test_processExpressionOperatorNode_Minus()
    {
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.MINUS);
        BType type = BoolType.getInstance();
        expressionOperatorNode.setType(type);
        assertEquals(dummyGenerator.processExpressionOperatorNode(expressionOperatorNode),
                "<Binary_Exp op='-' typref='2044650'>\n</Binary_Exp>");

    }

    @Test
    public void test_processSubstitutionNode_IfOrSelectSubstitutionsNode_SELECT(){
        BType type = BoolType.getInstance();
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.MINUS);
        expressionOperatorNode.setType(type);
        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));

        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstitutionNode(new SourceCodePosition(),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test")),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test2")));

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
                "                        <Id value='test' typref='2044650'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Id value='test2' typref='2044650'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Then>\n" +
                "        </When>\n" +
                "    </When_Clauses>\n" +
                "</Select>", dummyGenerator.processSubstitutionNode(ifOrSelectSubstitutionsNode));

    }

    @Test
    public void test_processSubstitutionNode_ListSubstitutionNode_Parallel(){
        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstitutionNode(new SourceCodePosition(),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test")),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test2")));
        ListSubstitutionNode listSubstitutionNode = new ListSubstitutionNode(new SourceCodePosition(),
                ListSubstitutionNode.ListOperator.Parallel, List.of(assignSubstitutionNode));

        assertEquals("<Nary_Sub op='||'>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='test' typref='2044650'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Id value='test2' typref='2044650'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Nary_Sub>", dummyGenerator.processSubstitutionNode(listSubstitutionNode));
    }

    @Test
    public void test_processSubstitutionNode_ListSubstitutionNode_Sequentiel(){
        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstitutionNode(new SourceCodePosition(),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test")),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test2")));
        ListSubstitutionNode listSubstitutionNode = new ListSubstitutionNode(new SourceCodePosition(),
                ListSubstitutionNode.ListOperator.Sequential, List.of(assignSubstitutionNode));

        assertEquals("<Nary_Sub op=';'>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='test' typref='2044650'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Id value='test2' typref='2044650'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Nary_Sub>", dummyGenerator.processSubstitutionNode(listSubstitutionNode));
    }

    @Test
    public void test_processSubstitutionNode_AssignSubstitutionNode(){

        AssignSubstitutionNode assignSubstitutionNode = new AssignSubstitutionNode(new SourceCodePosition(),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test")),
                List.of(dummy_IdentifierExprNodeGenerator(BoolType.getInstance(), "test2")));

        assertEquals("<Assignement_Sub>\n" +
                "    <Variables>\n" +
                "        <Id value='test' typref='2044650'/>\n" +
                "    </Variables>\n" +
                "    <Values>\n" +
                "        <Id value='test2' typref='2044650'/>\n" +
                "    </Values>\n" +
                "</Assignement_Sub>", dummyGenerator.processSubstitutionNode(assignSubstitutionNode));
    }

    @Test
    public void test_processPredicateNode_PredicateOperatorNode(){
        BType type = BoolType.getInstance();
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.MINUS);
        expressionOperatorNode.setType(type);
        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));

        PredicateOperatorNode predicateOperatorNode = new PredicateOperatorNode(new SourceCodePosition(),
                PredicateOperatorNode.PredicateOperator.AND, List.of(predicateNode));

        assertEquals("<Invariant>\n" +
                "</Invariant>", dummyGenerator.processPredicateNode(predicateOperatorNode));

    }

    @Test
    public void test_processPredicateNode_PredicateOperatorWithExprArgsNode(){
        BType type = BoolType.getInstance();
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.MINUS);
        expressionOperatorNode.setType(type);
        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));
        predicateNode.setType(type);
        assertEquals("<Exp_Comparison op=':'>\n" +
                "    <Binary_Exp op='-' typref='2044650'>\n" +
                "    </Binary_Exp>\n" +
                "</Exp_Comparison>", dummyGenerator.processPredicateNode(predicateNode));
    }

    @Test
    public void test_processPredicateOperatorNode_PredicateOperatorNode(){
        BType type = BoolType.getInstance();
        ExpressionOperatorNode expressionOperatorNode = new ExpressionOperatorNode(new SourceCodePosition(),
                ExpressionOperatorNode.ExpressionOperator.MINUS);
        expressionOperatorNode.setType(type);
        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));
        predicateNode.setType(type);

        PredicateOperatorNode predicateOperatorNode = new PredicateOperatorNode(new SourceCodePosition(),
                PredicateOperatorNode.PredicateOperator.AND, List.of(predicateNode, predicateNode));

        String result =  dummyGenerator.processPredicateOperatorNode(predicateOperatorNode);
        assertEquals("<Nary_Pred op='&amp;'>\n" +
                "    <Exp_Comparison op=':'>\n" +
                "        <Binary_Exp op='-' typref='2044650'>\n" +
                "        </Binary_Exp>\n" +
                "    </Exp_Comparison>\n" +
                "    <Exp_Comparison op=':'>\n" +
                "        <Binary_Exp op='-' typref='2044650'>\n" +
                "        </Binary_Exp>\n" +
                "    </Exp_Comparison>\n" +
                "</Nary_Pred>",result);
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_LESS(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.LESS), "&lt;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_GREATER(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.GREATER), "&gt;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_EQUAL(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.EQUAL), "&eq;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_NOT_EQUAL(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.NOT_EQUAL), "&neq;");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_ELEMENT_OF(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF), ":");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_LESS_EQUAL(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.LESS_EQUAL), "&lt;=");
    }

    @Test
    public void test_processOperatorPredOperatorExprArgs_GREATER_EQUAL(){
        assertEquals(dummyGenerator.processOperatorPredOperatorExprArgs(
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.GREATER_EQUAL), "&gt;=");
    }

    @Test
    public void test_processDeclarationNode(){
        DeclarationNode declarationNode = new DeclarationNode(new SourceCodePosition(), "test",
                DeclarationNode.Kind.CONSTANT, new MachineNode(new SourceCodePosition()));
        BType type = BoolType.getInstance();
        declarationNode.setType(type);
        assertEquals("<Id value='test' typref='2044650'/>", dummyGenerator.processDeclarationNode(declarationNode));
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void test_hashFunction(){
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Hash was already taken! BOOL is not INTEGER");
        BType type = BoolType.getInstance();
        int hash = dummyGenerator.generateHash(type);
        BType crashType = IntegerType.getInstance();
        dummyGenerator.getNodeTyp().put(hash, crashType);
        dummyGenerator.generateHash(type);

    }
}
