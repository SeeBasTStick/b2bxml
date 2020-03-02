package de.hhu.stups.bxmlgenerator.unitSablecc;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.Node;
import de.be4.classicalb.core.parser.node.Start;
import de.hhu.stups.bxmlgenerator.sablecc.STGroupGenerator;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import de.prob.typechecker.btypes.BoolType;
import de.prob.typechecker.btypes.IntegerType;
import de.prob.typechecker.btypes.SetType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class STGroupGeneratorTest {


    @Test
    public void test_caseAIdentifierExpression() throws BCompoundException {
        String machine = hashTestMachine();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();


        AIdentifierExpression aIdentifierExpression = (AIdentifierExpression) c.getVariables().get("x");

        String result = new STGroupGeneratorStub(new HashMap<>(), c, aIdentifierExpression).generateCurrent();

        assertEquals("<Id value='x' typref='1618932450'/>", result);

    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

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

    class STGroupGeneratorStub extends STGroupGenerator {

        public STGroupGeneratorStub(HashMap<Integer, BType> nodeType, MachineContext ctx, Node startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg").getInstanceOf("id"),
                    nodeType,
                    new Typechecker(ctx),
                    startNode);
        }


    }

}
