package de.hhu.stups.bxmlgenerator.unitSablecc;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.be4.classicalb.core.parser.node.Start;
import de.hhu.stups.bxmlgenerator.sablecc.MachineGenerator;
import de.hhu.stups.bxmlgenerator.sablecc.SubGenerator;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MachineGeneratorTest {

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
}
