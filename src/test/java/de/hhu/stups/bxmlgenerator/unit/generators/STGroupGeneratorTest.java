package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.generators.STGroupGenerator;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import de.prob.typechecker.btypes.BoolType;
import de.prob.typechecker.btypes.IntegerType;
import de.prob.typechecker.btypes.SetType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class STGroupGeneratorTest {

    @Test
    public void test_generateAbstractVariable_1_Variable() throws BCompoundException {


        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        AVariablesMachineClause aVariablesMachineClause = new AVariablesMachineClause();

        AIdentifierExpression aIdentifierExpression = generateIdentifierExpression("a", typechecker, IntegerType.getInstance());

        aVariablesMachineClause.setIdentifiers(List.of(aIdentifierExpression));

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker,  aVariablesMachineClause);

        String result = stGroupGeneratorStub.generateCurrent();

        assertEquals("<Abstract_Variables>\n" +
                "    <Id value='a' typref='1618932450'/>\n" +
                "</Abstract_Variables>", result);
    }

    @Test
    public void test_generateAbstractVariable_2_Variable() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        AVariablesMachineClause aVariablesMachineClause = new AVariablesMachineClause();

        AIdentifierExpression aIdentifierExpression = generateIdentifierExpression("a", typechecker, IntegerType.getInstance());

        AIdentifierExpression aIdentifierExpression2 = generateIdentifierExpression("a", typechecker, IntegerType.getInstance());


        aVariablesMachineClause.setIdentifiers(List.of(aIdentifierExpression, aIdentifierExpression2));

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker,  aVariablesMachineClause);

        String result = stGroupGeneratorStub.generateCurrent();

        assertEquals("<Abstract_Variables>\n" +
                "    <Id value='a' typref='1618932450'/>\n" +
                "    <Id value='a' typref='1618932450'/>\n" +
                "</Abstract_Variables>", result);

    }

    @Test
    public void test_generate_1_Invariant() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        AInvariantMachineClause aInvariantMachineClause = new AInvariantMachineClause();

        ALessPredicate aLessPredicate = generateLessPredicate("a", 3, typechecker, IntegerType.getInstance());

        aInvariantMachineClause.setPredicates(aLessPredicate);

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker, aInvariantMachineClause);

        String result = stGroupGeneratorStub.generateCurrent();

        assertEquals("<Invariant>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='a' typref='1618932450'/>\n" +
                "        <Integer_Literal value='3' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "</Invariant>", result);
    }

    @Test
    public void test_generate_3_Invariant() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x, y, z\n"
                + "INVARIANT x : NAT & y : NAT & z : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        AInvariantMachineClause aInvariantMachineClause = new AInvariantMachineClause();

        ALessPredicate aLessPredicate = generateLessPredicate("a", 3, typechecker, IntegerType.getInstance());

        ALessPredicate aLessPredicate2 = generateLessPredicate("b", 3, typechecker, IntegerType.getInstance());

        ALessPredicate aLessPredicate3 = generateLessPredicate("c", 3, typechecker, IntegerType.getInstance());


        aInvariantMachineClause.setPredicates(generateConjunctPredicate(aLessPredicate3, generateConjunctPredicate(aLessPredicate, aLessPredicate2)));

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker, aInvariantMachineClause);

        String result = stGroupGeneratorStub.generateCurrent();

        assertEquals("<Invariant>\n" +
                "    <Nary_Pred op='&amp;'>\n" +
                "        <Exp_Comparison op='&lt;'>\n" +
                "            <Id value='c' typref='1618932450'/>\n" +
                "            <Integer_Literal value='3' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "        <Exp_Comparison op='&lt;'>\n" +
                "            <Id value='a' typref='1618932450'/>\n" +
                "            <Integer_Literal value='3' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "        <Exp_Comparison op='&lt;'>\n" +
                "            <Id value='b' typref='1618932450'/>\n" +
                "            <Integer_Literal value='3' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "    </Nary_Pred>\n" +
                "</Invariant>", result);
    }


    @Test
    public void test_MachineGeneration() throws BCompoundException {
        String machine = "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x, y:=2 ,1\n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        STGroupGenerator stGroupGenerator = new STGroupGeneratorStub(new HashMap<>(), c, start);

        String result = stGroupGenerator.generateCurrent();

        assertEquals("<Machine name='' type='abstraction'>\n" +
                "    <Abstract_Variables>\n" +
                "        <Id value='x' typref='1618932450'/>\n" +
                "        <Id value='y' typref='1618932450'/>\n" +
                "    </Abstract_Variables>\n" +
                "    <Invariant>\n" +
                "        <Nary_Pred op='&amp;'>\n" +
                "            <Exp_Comparison op=':'>\n" +
                "                <Id value='x' typref='1618932450'/>\n" +
                "                <Id value='NAT' typref='631359557'/>\n" +
                "            </Exp_Comparison>\n" +
                "            <Exp_Comparison op=':'>\n" +
                "                <Id value='y' typref='1618932450'/>\n" +
                "                <Id value='NAT' typref='631359557'/>\n" +
                "            </Exp_Comparison>\n" +
                "        </Nary_Pred>\n" +
                "    </Invariant>\n" +
                "    <Initialisation>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='x' typref='1618932450'/>\n" +
                "                <Id value='y' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='2' typref='1618932450'/>\n" +
                "                <Integer_Literal value='1' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Initialisation>\n" +
                "</Machine>", result);
    }

    @Test
    public void test_caseAIdentifierExpression() throws BCompoundException {
        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        typechecker.setType(aIdentifierExpression, IntegerType.getInstance());

        String result = new STGroupGeneratorStub(typechecker, aIdentifierExpression).generateCurrent();

        assertEquals("<Id value='x' typref='1618932450'/>", result);

    }

    @Test
    public void test_caseAIntervalExpression() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AIntervalExpression aIntervalExpression = new AIntervalExpression();

        AIntegerExpression left = generateIntegerExpression(1, customTypeChecker);
        AIntegerExpression right = generateIntegerExpression(2,customTypeChecker );

        aIntervalExpression.setLeftBorder(left);
        aIntervalExpression.setRightBorder(right);

        customTypeChecker.setType(aIntervalExpression, new SetType(IntegerType.getInstance()));

        String result = new STGroupGeneratorStub(customTypeChecker, aIntervalExpression).generateCurrent();

        assertEquals("<Binary_Exp op='..' typref='631359557'>\n" +
                "    <Integer_Literal value='1' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Binary_Exp>", result);
    }

    @Test
    public void test_caseAIntegerExpression() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AIntegerExpression integerExpression = generateIntegerExpression(1, customTypeChecker);

        String result = new STGroupGeneratorStub(customTypeChecker, integerExpression).generateCurrent();

        assertEquals("<Integer_Literal value='1' typref='1618932450'/>", result);
    }

    @Test
    public void test_caseANatSetExpression() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        ANatSetExpression aNatSetExpression = new ANatSetExpression();

        customTypeChecker.setType(aNatSetExpression, IntegerType.getInstance());

        String result = new STGroupGeneratorStub(customTypeChecker, aNatSetExpression).generateCurrent();

        assertEquals("<Id value='NAT' typref='1618932450'/>", result);
    }

    @Test
    public void test_caseAConjunctPredicate_simple() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);


        ALessPredicate aLessPredicate = generateLessPredicate("test", 4, customTypeChecker, IntegerType.getInstance());

        ALessPredicate aLessPredicate2 = generateLessPredicate("test2", 4, customTypeChecker, IntegerType.getInstance());

        AConjunctPredicate aConjunctPredicate = generateConjunctPredicate(aLessPredicate, aLessPredicate2);

        String result = new STGroupGeneratorStub(customTypeChecker, aConjunctPredicate).generateCurrent();
        assertEquals("<Nary_Pred op='&amp;'>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='test' typref='1618932450'/>\n" +
                "        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='test2' typref='1618932450'/>\n" +
                "        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "</Nary_Pred>", result);
    }

    @Test
    public void test_caseAConjunctPredicate_complex() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        ALessPredicate aLessPredicate = generateLessPredicate("test", 4, customTypeChecker, IntegerType.getInstance());

        ALessPredicate aLessPredicate2 = generateLessPredicate("test2", 4, customTypeChecker, IntegerType.getInstance());

        ALessPredicate aLessPredicate3 = generateLessPredicate("test3", 4, customTypeChecker, IntegerType.getInstance());

        AConjunctPredicate aConjunctPredicate = generateConjunctPredicate(aLessPredicate, aLessPredicate2);

        AConjunctPredicate aConjunctPredicate2 = generateConjunctPredicate(aConjunctPredicate, aLessPredicate3);

        String result = new STGroupGeneratorStub(customTypeChecker, aConjunctPredicate2).generateCurrent();
        assertEquals("<Nary_Pred op='&amp;'>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='test' typref='1618932450'/>\n" +
                "        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='test2' typref='1618932450'/>\n" +
                "        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "    <Exp_Comparison op='&lt;'>\n" +
                "        <Id value='test3' typref='1618932450'/>\n" +
                "        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    </Exp_Comparison>\n" +
                "</Nary_Pred>", result);
    }

    @Test
    public void test_caseAGreaterEqualPredicate() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AIdentifierExpression aIdentifierExpression = generateIdentifierExpression("test",  customTypeChecker, IntegerType.getInstance());
        customTypeChecker.setType(aIdentifierExpression, IntegerType.getInstance());

        AIntegerExpression aIntegerExpression = generateIntegerExpression(2, customTypeChecker);
        customTypeChecker.setType(aIntegerExpression, IntegerType.getInstance());

        AGreaterEqualPredicate aGreaterEqualPredicate = new AGreaterEqualPredicate();

        aGreaterEqualPredicate.setLeft(aIdentifierExpression);
        aGreaterEqualPredicate.setRight(aIntegerExpression);

        customTypeChecker.setType(aGreaterEqualPredicate, IntegerType.getInstance());

        String result = new STGroupGeneratorStub(customTypeChecker, aGreaterEqualPredicate).generateCurrent();

        assertEquals("<Exp_Comparison op='&gt;='>\n" +
                "    <Id value='test' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_caseALessPredicate() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        ALessPredicate aLessPredicate = generateLessPredicate("test", 2, customTypeChecker, IntegerType.getInstance());

        String result = new STGroupGeneratorStub(customTypeChecker, aLessPredicate).generateCurrent();

        assertEquals("<Exp_Comparison op='&lt;'>\n" +
                "    <Id value='test' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_caseAMemberPredicate() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AIdentifierExpression aIdentifierExpression = generateIdentifierExpression("test", customTypeChecker, IntegerType.getInstance());

        AIntegerExpression aIntegerExpression = generateIntegerExpression(2, customTypeChecker);

        AMemberPredicate aMemberPredicate = new AMemberPredicate();

        aMemberPredicate.setLeft(aIdentifierExpression);
        aMemberPredicate.setRight(aIntegerExpression);

        customTypeChecker.setType(aMemberPredicate, IntegerType.getInstance());

        String result = new STGroupGeneratorStub(customTypeChecker, aMemberPredicate).generateCurrent();

        assertEquals("<Exp_Comparison op=':'>\n" +
                "    <Id value='test' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_caseAInitialisationMachineClause() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();
        aAssignSubstitution.setLhsExpression(List.of(generateIdentifierExpression("a", customTypeChecker, IntegerType.getInstance())));
        aAssignSubstitution.setRhsExpressions(List.of(generateIntegerExpression(3, customTypeChecker)));

        AInitialisationMachineClause aInitialisationMachineClause = new AInitialisationMachineClause();
        aInitialisationMachineClause.setSubstitutions(aAssignSubstitution);

        String result = new STGroupGeneratorStub(customTypeChecker, aInitialisationMachineClause).generateCurrent();

        assertEquals("<Initialisation>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='a' typref='1618932450'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Integer_Literal value='3' typref='1618932450'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Initialisation>", result);
    }


    @Test
    public void test_caseAAssignSubstitution_1_Substitution() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();
        aAssignSubstitution.setLhsExpression(List.of(generateIdentifierExpression("a", customTypeChecker, IntegerType.getInstance())));
        aAssignSubstitution.setRhsExpressions(List.of(generateIntegerExpression(3, customTypeChecker)));

        String result = new STGroupGeneratorStub(customTypeChecker, aAssignSubstitution).generateCurrent();

        assertEquals("<Assignement_Sub>\n" +
                "    <Variables>\n" +
                "        <Id value='a' typref='1618932450'/>\n" +
                "    </Variables>\n" +
                "    <Values>\n" +
                "        <Integer_Literal value='3' typref='1618932450'/>\n" +
                "    </Values>\n" +
                "</Assignement_Sub>", result);
    }

    @Test
    public void test_caseAAssignSubstitution_2_Substitution() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();
        aAssignSubstitution.setLhsExpression(
                List.of(generateIdentifierExpression("b", customTypeChecker, IntegerType.getInstance()),
                        generateIdentifierExpression("a", customTypeChecker, IntegerType.getInstance())));
        aAssignSubstitution.setRhsExpressions(List.of(
                generateIntegerExpression(5, customTypeChecker),
                generateIntegerExpression(3, customTypeChecker)));

        String result = new STGroupGeneratorStub(customTypeChecker, aAssignSubstitution).generateCurrent();

        assertEquals("<Assignement_Sub>\n" +
                "    <Variables>\n" +
                "        <Id value='b' typref='1618932450'/>\n" +
                "        <Id value='a' typref='1618932450'/>\n" +
                "    </Variables>\n" +
                "    <Values>\n" +
                "        <Integer_Literal value='5' typref='1618932450'/>\n" +
                "        <Integer_Literal value='3' typref='1618932450'/>\n" +
                "    </Values>\n" +
                "</Assignement_Sub>", result);
    }


    public ALessPredicate generateLessPredicate(String left, int right, Typechecker typechecker, BType typeLeft){
        ALessPredicate aLessPredicate = new ALessPredicate();

        aLessPredicate.setLeft(generateIdentifierExpression(left, typechecker, typeLeft ));
        aLessPredicate.setRight(generateIntegerExpression(right, typechecker));

        typechecker.setType(aLessPredicate, IntegerType.getInstance());

        return aLessPredicate;
    }


    public AIntegerExpression generateIntegerExpression(int value, Typechecker typechecker){
        AIntegerExpression integerExpression = new AIntegerExpression();
        integerExpression.setLiteral(new TIntegerLiteral(String.valueOf(value)));
        typechecker.setType(integerExpression, IntegerType.getInstance());
        return integerExpression;
    }

    public AIdentifierExpression generateIdentifierExpression(String id, Typechecker typechecker, BType type){
        AIdentifierExpression aIdentifierExpression = new AIdentifierExpression();
        aIdentifierExpression.setIdentifier(List.of(new TIdentifierLiteral(id)));
        typechecker.setType(aIdentifierExpression, type);
        return aIdentifierExpression;
    }

    public AConjunctPredicate generateConjunctPredicate(PPredicate left, PPredicate right){
        AConjunctPredicate aConjunctPredicate = new AConjunctPredicate(left, right);
        aConjunctPredicate.setLeft(left);
        aConjunctPredicate.setRight(right);

        return aConjunctPredicate;
    }



    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public String machineWithInterval() {
        return "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : 1..2 & y : NAT  & y >= 4\n" + "INITIALISATION x := 1 ; y:=2\n" + "END";
    }

    public String hashTestMachine() {
        return "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";
    }

    @Test
    public void test_hashFunction_1() throws BCompoundException {
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Hash was already taken! INTEGER is not BOOL");

        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(new HashMap<>(), c, aIdentifierExpression);
        BType type = IntegerType.getInstance();
        int hash = stGroupGeneratorStub.generateHash(type);
        BType crashType = BoolType.getInstance();
        stGroupGeneratorStub.getNodeType().put(hash, crashType);
        stGroupGeneratorStub.generateHash(type);
    }

    @Test
    public void test_hashFunction_2() throws BCompoundException {
        HashMap<Integer, BType> dummy = new HashMap<>();
        exception.expect(IndexOutOfBoundsException.class);
        exception.expectMessage("Hash was already taken! INTEGER is not BOOL");

        BType type = BoolType.getInstance();
        BType crashType = IntegerType.getInstance();
        int hash = Math.abs(crashType.toString().hashCode());

        dummy.put(hash, type);

        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(dummy, c, aIdentifierExpression);
        stGroupGeneratorStub.generateHash(crashType);
    }

    @Test
    public void test_hashFunction_3() throws BCompoundException {

        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        BType type = BoolType.getInstance();
        int result = new STGroupGeneratorStub(new HashMap<>(), c, aIdentifierExpression).generateHash(type);

        assertEquals(Math.abs(type.toString().hashCode()), result);
    }

    @Test
    public void test_hashFunction_4() throws BCompoundException {
        HashMap<Integer, BType> dummy = new HashMap<>();
        BType setType = new SetType(IntegerType.getInstance());

        int hash = Math.abs(setType.toString().hashCode());

        dummy.put(hash, setType);

        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        STGroupGeneratorStub stGroupGeneratorStub =
                new STGroupGeneratorStub(dummy, c, aIdentifierExpression);

        int result = stGroupGeneratorStub.generateHash(setType);

        assertEquals(hash, result);

    }
}
