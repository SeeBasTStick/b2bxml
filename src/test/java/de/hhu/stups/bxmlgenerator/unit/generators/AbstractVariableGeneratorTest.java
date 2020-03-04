package de.hhu.stups.bxmlgenerator.unit.generators;

import static org.junit.Assert.assertEquals;

public class AbstractVariableGeneratorTest {

/*
    @Test
    public void test_generateAbstractVariable_1_Variable() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

         machineGeneratorStub = new MachineGeneratorStub(c, start);

        List<String> result = machineGeneratorStub.generateSubExpression();

        assertEquals("<Id value='x' typref='1618932450'/>", result.get(0));
    }

    @Test
    public void test_generateAbstractVariable_2_Variable() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        List<String> result = machineGeneratorStub.generateSubExpression();

        assertEquals("<Id value='x' typref='1618932450'/>", result.get(0));
        assertEquals("<Id value='y' typref='1618932450'/>", result.get(1));

    }

    @Test
    public void test_generateAbstractVariables() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        String result = machineGeneratorStub.generateAllExpression();

        assertEquals("<Abstract_Variables>\n" +
                "    <Id value='x' typref='1618932450'/>\n" +
                "    <Id value='y' typref='1618932450'/>\n" +
                "</Abstract_Variables>", result);

    }
*/


}
