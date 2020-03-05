package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import de.hhu.stups.bxmlgenerator.util.ExpressionFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionFinderTest implements AbstractFinder {

    @Test
    public void test_id_aIdentifierExpression(){
        AIdentifierExpression aIdentifierExpression = new AIdentifierExpression();

        String result = find(aIdentifierExpression);

        assertEquals("id", result);
    }

    @Test
    public void test_id_aNatSetExpression(){
        ANatSetExpression aNatSetExpression = new ANatSetExpression();

        String result = find(aNatSetExpression);

        assertEquals("id", result);
    }

    @Test
    public void test_binary_exp_aIntervalExpression(){
        AIntervalExpression aIntervalExpression = new AIntervalExpression();

        String result = find(aIntervalExpression);

        assertEquals("binary_exp", result);
    }

    @Test
    public void test_integer_literal_aIntegerExpression(){
        AIntegerExpression aIntegerExpression = new AIntegerExpression();

        String result = find(aIntegerExpression);

        assertEquals("integer_literal", result);
    }

    @Test
    public void test_AAddExpression(){
        AAddExpression aAddExpression = new AAddExpression();

        String result = find(aAddExpression);

        assertEquals("binary_exp", result);
    }

    @Test
    public void test_A(){
        AMinusOrSetSubtractExpression aMinusOrSetSubtractExpression = new AMinusOrSetSubtractExpression();

        String result = find(aMinusOrSetSubtractExpression);

        assertEquals("binary_exp", result);
    }


}
