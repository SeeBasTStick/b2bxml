package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.IntegerType;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class STGroupGeneratorTestSubstitutions implements STGroupGeneratorTestUtils, AbstractFinder {

    @Test
    public void test_caseASkipSubstitution() throws BCompoundException {

        Typechecker customTypeChecker = stubSetup();

        ASkipSubstitution aAssignSubstitution = new ASkipSubstitution();

        String result = new STGroupGeneratorStub(customTypeChecker, aAssignSubstitution, find(aAssignSubstitution)).generateCurrent();

        assertEquals("<Skip/>", result);
    }

    @Test
    public void test_caseAAssertionSubstitution() throws BCompoundException {

        Typechecker customTypeChecker = stubSetup();

        AAssertionSubstitution aAssertionSubstitution = new AAssertionSubstitution();

        aAssertionSubstitution.setPredicate(generateAGreaterPredicate(
                generateIntegerExpression(1, customTypeChecker),
                generateIntegerExpression(2, customTypeChecker),
                customTypeChecker, IntegerType.getInstance(), IntegerType.getInstance()));

        aAssertionSubstitution.setSubstitution(new ASkipSubstitution());

        String result = new STGroupGeneratorStub(customTypeChecker, aAssertionSubstitution, find(aAssertionSubstitution)).generateCurrent();

        assertEquals("<Assert_Sub>\n" +
                "    <Guard>\n" +
                "        <Exp_Comparison op='&gt;'>\n" +
                "            <Integer_Literal value='1' typref='1618932450'/>\n" +
                "            <Integer_Literal value='2' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "    </Guard>\n" +
                "    <Body>\n" +
                "        <Skip/>\n" +
                "    </Body>\n" +
                "</Assert_Sub>", result);

    }

    @Test
    public void test_APreconditionSubstitution() throws BCompoundException {
        Typechecker typechecker = stubSetup();

        APreconditionSubstitution aPreconditionSubstitution = new APreconditionSubstitution();

        aPreconditionSubstitution.setPredicate(generateAGreaterPredicate(
                generateIntegerExpression(1, typechecker),
                generateIntegerExpression(2, typechecker),
                typechecker, IntegerType.getInstance(), IntegerType.getInstance()));

        aPreconditionSubstitution.setSubstitution(new ASkipSubstitution());

        String result = new STGroupGeneratorStub(typechecker, aPreconditionSubstitution, find(aPreconditionSubstitution)).generateCurrent();

        assertEquals("<PRE_Sub>\n" +
                "    <Precondition>\n" +
                "        <Exp_Comparison op='&gt;'>\n" +
                "            <Integer_Literal value='1' typref='1618932450'/>\n" +
                "            <Integer_Literal value='2' typref='1618932450'/>\n" +
                "        </Exp_Comparison>\n" +
                "    </Precondition>\n" +
                "    <Body>\n" +
                "        <Skip/>\n" +
                "    </Body>\n" +
                "</PRE_Sub>", result);

    }

    @Test
    public void test_caseAAssignSubstitution_1_Substitution() throws BCompoundException {
        String machine = machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();
        aAssignSubstitution.setLhsExpression(List.of(generateIdentifierExpression("a", customTypeChecker, IntegerType.getInstance())));
        aAssignSubstitution.setRhsExpressions(List.of(generateIntegerExpression(3, customTypeChecker)));

        String result = new STGroupGeneratorStub(customTypeChecker, aAssignSubstitution, find(aAssignSubstitution)).generateCurrent();

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
        String machine = machineWithInterval();

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

        String result = new STGroupGeneratorStub(customTypeChecker, aAssignSubstitution, find(aAssignSubstitution)).generateCurrent();

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


    @Test
    public void test_caseAParallelSubstitution() throws BCompoundException {
        String machine = machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();
        aParallelSubstitution.setSubstitutions(
                List.of(generateAAssignSubstitution("a", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker), IntegerType.getInstance(), customTypeChecker),
                        generateAAssignSubstitution("b", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker), IntegerType.getInstance(), customTypeChecker)));


        String result = new STGroupGeneratorStub(customTypeChecker, aParallelSubstitution, find(aParallelSubstitution)).generateCurrent();

        assertEquals("<Nary_Sub op='||'>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='a' typref='1618932450'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Integer_Literal value='4' typref='1618932450'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "    <Assignement_Sub>\n" +
                "        <Variables>\n" +
                "            <Id value='b' typref='1618932450'/>\n" +
                "        </Variables>\n" +
                "        <Values>\n" +
                "            <Integer_Literal value='4' typref='1618932450'/>\n" +
                "        </Values>\n" +
                "    </Assignement_Sub>\n" +
                "</Nary_Sub>", result);
    }

    @Ignore
    @Test
    public void test_caseAPreconditionSubstitution() {

    }

    @Test
    public void test_caseASelectSubstitution() throws BCompoundException {

        String machine = machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        ASelectSubstitution aSelectSubstitution = new ASelectSubstitution();
        aSelectSubstitution.setCondition(generateALessPredicate(
                generateIntegerExpression(2, customTypeChecker),
                generateIntegerExpression(3, customTypeChecker),
                customTypeChecker,
                IntegerType.getInstance(), IntegerType.getInstance()));

        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();
        aParallelSubstitution.setSubstitutions(
                List.of(generateAAssignSubstitution("a", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker), IntegerType.getInstance(), customTypeChecker),
                        generateAAssignSubstitution("b", IntegerType.getInstance(), generateIntegerExpression(4, customTypeChecker), IntegerType.getInstance(), customTypeChecker)));

        aSelectSubstitution.setThen(aParallelSubstitution);


        String result = new STGroupGeneratorStub(customTypeChecker, aSelectSubstitution, find(aSelectSubstitution)).generateCurrent();

        assertEquals("<Select>\n" +
                "    <When_Clauses>\n" +
                "        <When>\n" +
                "            <Condition>\n" +
                "                <Exp_Comparison op='&lt;'>\n" +
                "                    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "                    <Integer_Literal value='3' typref='1618932450'/>\n" +
                "                </Exp_Comparison>\n" +
                "            </Condition>\n" +
                "            <Then>\n" +
                "                <Nary_Sub op='||'>\n" +
                "                    <Assignement_Sub>\n" +
                "                        <Variables>\n" +
                "                            <Id value='a' typref='1618932450'/>\n" +
                "                        </Variables>\n" +
                "                        <Values>\n" +
                "                            <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                        </Values>\n" +
                "                    </Assignement_Sub>\n" +
                "                    <Assignement_Sub>\n" +
                "                        <Variables>\n" +
                "                            <Id value='b' typref='1618932450'/>\n" +
                "                        </Variables>\n" +
                "                        <Values>\n" +
                "                            <Integer_Literal value='4' typref='1618932450'/>\n" +
                "                        </Values>\n" +
                "                    </Assignement_Sub>\n" +
                "                </Nary_Sub>\n" +
                "            </Then>\n" +
                "        </When>\n" +
                "    </When_Clauses>\n" +
                "</Select>", result);
    }


}
