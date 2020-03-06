package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.be4.eventbalg.core.parser.node.PPrecondition;
import de.hhu.stups.bxmlgenerator.generators.STGroupGenerator;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
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

public class STGroupGeneratorTest implements AbstractFinder, STGroupGeneratorTestUtils {

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

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker,  aVariablesMachineClause, find(aVariablesMachineClause));

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

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker,  aVariablesMachineClause, find(aVariablesMachineClause));

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

        ALessPredicate aLessPredicate = generateALessPredicate(
                generateIdentifierExpression("a", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(3, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());
        aInvariantMachineClause.setPredicates(aLessPredicate);

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker, aInvariantMachineClause, find(aInvariantMachineClause));

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

        ALessPredicate aLessPredicate = generateALessPredicate(
                generateIdentifierExpression("a", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(3, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        ALessPredicate aLessPredicate2 =generateALessPredicate(
                generateIdentifierExpression("b", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(3, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        ALessPredicate aLessPredicate3 = generateALessPredicate(
                generateIdentifierExpression("c", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(3, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        aInvariantMachineClause.setPredicates(generateConjunctPredicate(aLessPredicate3, generateConjunctPredicate(aLessPredicate, aLessPredicate2)));

        STGroupGeneratorStub stGroupGeneratorStub = new STGroupGeneratorStub(typechecker, aInvariantMachineClause, find(aInvariantMachineClause));

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

        assertEquals("<Machine name='test' type='abstraction'>\n" +
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
                "    <TypeInfos>\n" +
                "        <Type id='631359557'>\n" +
                "            <Unary_Exp op='POW'>\n" +
                "                <Id value='INTEGER'/>\n" +
                "            </Unary_Exp>\n" +
                "        </Type>\n" +
                "        <Type id='1618932450'>\n" +
                "            <Id value='INTEGER'/>\n" +
                "        </Type>\n" +
                "    </TypeInfos>\n" +
                "</Machine>", result);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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

    @Test
    public void test_caseAOperation() throws BCompoundException {


        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();
        aParallelSubstitution.setSubstitutions(
                List.of(generateAAssignSubstitution("a", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker),
                        generateAAssignSubstitution("b", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker)));


        AOperation aOperation = new AOperation();
        aOperation.setOpName(List.of(new TIdentifierLiteral("a")));
        aOperation.setOperationBody(aParallelSubstitution);

        String result = new STGroupGeneratorStub(customTypeChecker, aOperation, "operation").generateCurrent();

        assertEquals("<Operation name='a'>\n" +
                "    <Body>\n" +
                "        <Nary_Sub op='||'>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='a' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='b' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Nary_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", result);
    }

    @Test
    public void test_caseAOperation_with_OutputParameters() throws BCompoundException {

        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AOperation aOperation = dummyOperation(customTypeChecker);

        String result = new STGroupGeneratorStub(customTypeChecker, aOperation, "operation").generateCurrent();

        assertEquals("<Operation name='a'>\n" +
                "    <Output_Parameters>\n" +
                "        <Id value='a' typref='1618932450'/>\n" +
                "    </Output_Parameters>\n" +
                "    <Body>\n" +
                "        <Nary_Sub op='||'>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='a' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='b' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Nary_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", result);
    }

    @Test
    public void test_caseAOperation_with_PreCondition() throws BCompoundException {

        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();
        aParallelSubstitution.setSubstitutions(
                List.of(generateAAssignSubstitution("a", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker),
                        generateAAssignSubstitution("b", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker)));

        APreconditionSubstitution aPreconditionSubstitution = new APreconditionSubstitution();

        aPreconditionSubstitution.setPredicate(generateALessPredicate(generateIntegerExpression(1, customTypeChecker),
                generateIntegerExpression(2, customTypeChecker),
                customTypeChecker,
                IntegerType.getInstance(),
                IntegerType.getInstance()));

        aPreconditionSubstitution.setSubstitution(aParallelSubstitution);



        AOperation aOperation = new AOperation();
        aOperation.setOpName(List.of(new TIdentifierLiteral("a")));
        aOperation.setOperationBody(aPreconditionSubstitution);

        String result = new STGroupGeneratorStub(customTypeChecker, aOperation, "operation").generateCurrent();

        assertEquals("<Operation name='a'>\n" +
                "    <Precondition>\n" +
                "        <Exp_Comparison op='&lt;'>\n" +
                "            <Integer_Literal value='1' typref='1618932450'/>\n" +
                "            <Integer_Literal value='2' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "    </Precondition>\n" +
                "    <Body>\n" +
                "        <Nary_Sub op='||'>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='a' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='b' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Nary_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", result);
    }

    @Test
    public void test_caseAOperationsMachineClause() throws BCompoundException {

        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

       AOperationsMachineClause aOperationsMachineClause = new AOperationsMachineClause();
       aOperationsMachineClause.setOperations(List.of(dummyOperation(customTypeChecker), dummyOperation(customTypeChecker)));

        String result = new STGroupGeneratorStub(customTypeChecker, aOperationsMachineClause, "operations").generateCurrent();

        assertEquals("<Operations>\n" +
                "    <Operation name='a'>\n" +
                "        <Output_Parameters>\n" +
                "            <Id value='a' typref='1618932450'/>\n" +
                "        </Output_Parameters>\n" +
                "        <Body>\n" +
                "            <Nary_Sub op='||'>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='a' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='b' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Nary_Sub>\n" +
                "        </Body>\n" +
                "    </Operation>\n" +
                "    <Operation name='a'>\n" +
                "        <Output_Parameters>\n" +
                "            <Id value='a' typref='1618932450'/>\n" +
                "        </Output_Parameters>\n" +
                "        <Body>\n" +
                "            <Nary_Sub op='||'>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='a' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='b' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Nary_Sub>\n" +
                "        </Body>\n" +
                "    </Operation>\n" +
                "</Operations>", result);
    }


    public AOperation dummyOperation(Typechecker customTypeChecker){
        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();
        aParallelSubstitution.setSubstitutions(
                List.of(generateAAssignSubstitution("a", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker),
                        generateAAssignSubstitution("b", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker) , IntegerType.getInstance(), customTypeChecker)));


        AOperation aOperation = new AOperation();
        aOperation.setOpName(List.of(new TIdentifierLiteral("a")));
        aOperation.setOperationBody(aParallelSubstitution);

        aOperation.setReturnValues(List.of(generateIdentifierExpression("a", customTypeChecker, IntegerType.getInstance())));

        return aOperation;
    }


}

