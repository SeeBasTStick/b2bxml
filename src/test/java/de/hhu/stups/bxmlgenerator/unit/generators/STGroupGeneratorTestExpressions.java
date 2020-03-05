package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.BParser;
import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.IntegerType;
import de.prob.typechecker.btypes.SetType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class STGroupGeneratorTestExpressions implements STGroupGeneratorTestUtils, AbstractFinder {

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

        String result = new STGroupGeneratorStub(typechecker, aIdentifierExpression, find(aIdentifierExpression)).generateCurrent();

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

        String result = new STGroupGeneratorStub(customTypeChecker, aIntervalExpression, find(aIntervalExpression)).generateCurrent();

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

        String result = new STGroupGeneratorStub(customTypeChecker, integerExpression, find(integerExpression)).generateCurrent();

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

        String result = new STGroupGeneratorStub(customTypeChecker, aNatSetExpression, find(aNatSetExpression)).generateCurrent();

        assertEquals("<Id value='NAT' typref='1618932450'/>", result);
    }

    @Test
    public void test_caseAAddExpression() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AAddExpression aAddExpression = new AAddExpression();

        customTypeChecker.setType(aAddExpression, IntegerType.getInstance());

        aAddExpression.setLeft(generateIntegerExpression(4, customTypeChecker));
        aAddExpression.setRight(generateIntegerExpression(4, customTypeChecker));

        String result = new STGroupGeneratorStub(customTypeChecker, aAddExpression, find(aAddExpression)).generateCurrent();

        assertEquals("<Binary_Exp op='+' typref='1618932450'>\n" +
                "    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "</Binary_Exp>", result);
    }

    @Test
    public void test_caseAMinusOrSetSubtract() throws BCompoundException{
        String machine =  machineWithInterval();

        BParser parser = new BParser("Test");
        Start start = parser.parse(machine, false);
        MachineContext c = new MachineContext(null, start);
        c.analyseMachine();

        Typechecker customTypeChecker = new Typechecker(c);

        AMinusOrSetSubtractExpression aMinusOrSetSubtractExpression = new AMinusOrSetSubtractExpression();

        customTypeChecker.setType(aMinusOrSetSubtractExpression, IntegerType.getInstance());

        aMinusOrSetSubtractExpression.setLeft(generateIntegerExpression(4, customTypeChecker));

        aMinusOrSetSubtractExpression.setRight(generateIntegerExpression(4, customTypeChecker));

        String result = new STGroupGeneratorStub(customTypeChecker, aMinusOrSetSubtractExpression, find(aMinusOrSetSubtractExpression)).generateCurrent();

        assertEquals("<Binary_Exp op='-' typref='1618932450'>\n" +
                "    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "    <Integer_Literal value='4' typref='1618932450'/>\n" +
                "</Binary_Exp>", result);
    }
}
