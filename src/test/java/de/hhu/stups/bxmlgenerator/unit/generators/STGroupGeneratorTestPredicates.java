package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.IntegerType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class STGroupGeneratorTestPredicates implements STGroupGeneratorTestUtils, AbstractFinder {

    @Test
    public void test_caseALessPredicate() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        ALessPredicate aLessPredicate = generateALessPredicate(
                generateIdentifierExpression("test", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(2, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());
        String result = new STGroupGeneratorStub(typechecker, aLessPredicate, find(aLessPredicate)).generateCurrent();

        assertEquals("<Exp_Comparison op='&lt;'>\n" +
                "    <Id value='test' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_caseAGreaterPredicate() throws BCompoundException {
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);

        AGreaterPredicate aGreaterPredicate = generateAGreaterPredicate(
                generateIdentifierExpression("test", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(2, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());
        String result = new STGroupGeneratorStub(typechecker, aGreaterPredicate, find(aGreaterPredicate)).generateCurrent();


        assertEquals("<Exp_Comparison op='&gt;'>\n" +
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

        String result = new STGroupGeneratorStub(customTypeChecker, aMemberPredicate, find(aMemberPredicate)).generateCurrent();

        assertEquals("<Exp_Comparison op=':'>\n" +
                "    <Id value='test' typref='1618932450'/>\n" +
                "    <Integer_Literal value='2' typref='1618932450'/>\n" +
                "</Exp_Comparison>", result);
    }

    @Test
    public void test_caseAConjunctPredicate_simple() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker typechecker = new Typechecker(c);


        ALessPredicate aLessPredicate = generateALessPredicate(
                generateIdentifierExpression("test", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(4, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        ALessPredicate aLessPredicate2 = generateALessPredicate(
                generateIdentifierExpression("test2", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(4, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());
        AConjunctPredicate aConjunctPredicate = generateConjunctPredicate(aLessPredicate, aLessPredicate2);

        String result = new STGroupGeneratorStub(typechecker, aConjunctPredicate, find(aConjunctPredicate)).generateCurrent();
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

        Typechecker typechecker = new Typechecker(c);
        ALessPredicate aLessPredicate = generateALessPredicate(
                generateIdentifierExpression("test", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(4, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        ALessPredicate aLessPredicate2 = generateALessPredicate(
                generateIdentifierExpression("test2", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(4, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        ALessPredicate aLessPredicate3 = generateALessPredicate(
                generateIdentifierExpression("test3", typechecker, IntegerType.getInstance()),
                generateIntegerExpression(4, typechecker), typechecker, IntegerType.getInstance(), IntegerType.getInstance());

        AConjunctPredicate aConjunctPredicate = generateConjunctPredicate(aLessPredicate, aLessPredicate2);

        AConjunctPredicate aConjunctPredicate2 = generateConjunctPredicate(aConjunctPredicate, aLessPredicate3);

        String result = new STGroupGeneratorStub(typechecker, aConjunctPredicate2, find(aConjunctPredicate)).generateCurrent();
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

        String result = new STGroupGeneratorStub(customTypeChecker, aGreaterEqualPredicate, find(aGreaterEqualPredicate)).generateCurrent();

        assertEquals("<Exp_Comparison op='&gt;='>\n" +
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

        String result = new STGroupGeneratorStub(customTypeChecker, aInitialisationMachineClause, find(aInitialisationMachineClause)).generateCurrent();

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


}
