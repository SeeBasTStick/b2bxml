package de.hhu.stups.bxmlgenerator.unitSablecc;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.be4.classicalb.core.parser.node.Start;
import de.hhu.stups.bxmlgenerator.sablecc.AbstractVariableGenerator;
import de.hhu.stups.bxmlgenerator.sablecc.MachineGenerator;
import de.hhu.stups.bxmlgenerator.sablecc.SubGenerator;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractVariableGeneratorTest {


    @Test
    public void test_generateAbstractVariable_1_Variable() throws BCompoundException {

        String machine = "MACHINE test \n" + "VARIABLES x\n"
                + "INVARIANT x : NAT \n" + "INITIALISATION x := 1 \n" + "END";

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        MachineGeneratorStub machineGeneratorStub = new MachineGeneratorStub(c, start);

        List<String> result = machineGeneratorStub.subTreeGenerator.generateSubExpression();

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

        List<String> result = machineGeneratorStub.subTreeGenerator.generateSubExpression();

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

    class MachineGeneratorStub extends MachineGenerator {

        protected SubGenerator subTreeGenerator;

        public MachineGeneratorStub(MachineContext ctx, Start startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new HashMap<>(),
                    new Typechecker(ctx)
                    , startNode);
        }

        @Override
        public String generateAllExpression(){
            getStartNode().apply(this);
            return subTreeGenerator.generateAllExpression();
        }

        @Override
        public void caseAVariablesMachineClause(AVariablesMachineClause node) {
            subTreeGenerator = new AbstractVariableGeneratorStub(getNodeType(), getTypechecker(), node);
        }
    }

    class AbstractVariableGeneratorStub extends AbstractVariableGenerator {

        public AbstractVariableGeneratorStub(HashMap<Integer, BType> nodeType, Typechecker typeChecker, AVariablesMachineClause startNode) {
            super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                    new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg").getInstanceOf("abstract_variable"),
                    nodeType,
                    typeChecker,
                    startNode);
        }

    }
}
