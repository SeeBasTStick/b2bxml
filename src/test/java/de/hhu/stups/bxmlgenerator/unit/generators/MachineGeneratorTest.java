package de.hhu.stups.bxmlgenerator.unit.generators;

import static org.junit.Assert.assertEquals;

public class MachineGeneratorTest {
/*
    @Test
    public void test_AbstractVariableGeneration() throws BCompoundException {
        String machine = "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        String result = machineGeneratorStub.generateSubExpression().get(0);

        assertEquals("<Abstract_Variables>\n" +
                "    <Id value='x' typref='1618932450'/>\n" +
                "    <Id value='y' typref='1618932450'/>\n" +
                "</Abstract_Variables>", result);
    }

    @Test
    public void test_MachineGeneration() throws BCompoundException {
        String machine = "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        String result = machineGeneratorStub.generateAllExpression();

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
                "</Machine>", result);
    }

    class MachineGeneratorStub extends MachineGenerator {

        protected SubGenerator subTreeGenerator;

        public MachineGeneratorStub(MachineContext ctx, Start startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new HashMap<>(),
                    new Typechecker(ctx)
                    , startNode);
        }

    }
    */

}
