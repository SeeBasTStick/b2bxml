package de.hhu.stups.bxmlgenerator.unit.generators;

import static org.junit.Assert.assertEquals;

public class InvariantGeneratorTest {

    /*
    @Test
    public void test_generate_1_Invariant() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        String result = machineGeneratorStub.generateAllExpression();

        assertEquals("<Invariant>\n" +
                "    <Exp_Comparison op=':'>\n" +
                "        <Id value='x' typref='1618932450'/>\n" +
                "        <Id value='NAT' typref='631359557'/>\n" +
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

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        String result = machineGeneratorStub.generateAllExpression();

        assertEquals("<Invariant>\n" +
                "    <Nary_Pred op='&amp;'>\n" +
                "        <Exp_Comparison op=':'>\n" +
                "            <Id value='x' typref='1618932450'/>\n" +
                "            <Id value='NAT' typref='631359557'/>\n" +
                "        </Exp_Comparison>\n" +
                "        <Exp_Comparison op=':'>\n" +
                "            <Id value='y' typref='1618932450'/>\n" +
                "            <Id value='NAT' typref='631359557'/>\n" +
                "        </Exp_Comparison>\n" +
                "        <Exp_Comparison op=':'>\n" +
                "            <Id value='z' typref='1618932450'/>\n" +
                "            <Id value='NAT' typref='631359557'/>\n" +
                "        </Exp_Comparison>\n" +
                "    </Nary_Pred>\n" +
                "</Invariant>", result);
    }

    class MachineGeneratorStub extends MachineGenerator {

        protected SubGenerator subTreeGenerator;

        public MachineGeneratorStub(MachineContext ctx, Start startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new HashMap<>(),
                    new Typechecker(ctx)
                    , startNode);
        }

        @Override
        public String generateAllExpression() {
            getStartNode().apply(this);
            return subTreeGenerator.generateAllExpression();
        }

        @Override
        public List<String> generateSubExpression() {
            getStartNode().apply(this);
            return subTreeGenerator.generateSubExpression();
        }

        @Override
        public void caseAInvariantMachineClause(AInvariantMachineClause node) {
            subTreeGenerator = new InvariantGeneratorStub(getNodeType(), getTypechecker(), node);
        }
    }

    class InvariantGeneratorStub extends InvariantGenerator {

        public InvariantGeneratorStub(HashMap<Integer, BType> nodeType, Typechecker typechecker, AInvariantMachineClause startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg").getInstanceOf("invariant"),
                    nodeType,
                    typechecker,
                    startNode);
        }

    }
    */

}
