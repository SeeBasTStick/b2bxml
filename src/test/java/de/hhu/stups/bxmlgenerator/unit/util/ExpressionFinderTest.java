package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.AIntegerExpression;
import de.be4.classicalb.core.parser.node.AIntervalExpression;
import de.be4.classicalb.core.parser.node.ANatSetExpression;
import de.hhu.stups.bxmlgenerator.util.ExpressionFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionFinderTest implements ExpressionFinder{

    @Test
    public void test_id_aIdentifierExpression(){
        AIdentifierExpression aIdentifierExpression = new AIdentifierExpression();

        String result = findExpression(aIdentifierExpression);

        assertEquals("id", result);
    }

    @Test
    public void test_id_aNatSetExpression(){
        ANatSetExpression aNatSetExpression = new ANatSetExpression();

        String result = findExpression(aNatSetExpression);

        assertEquals("id", result);
    }

    @Test
    public void test_binary_exp_aIntervalExpression(){
        AIntervalExpression aIntervalExpression = new AIntervalExpression();

        String result = findExpression(aIntervalExpression);

        assertEquals("binary_exp", result);
    }

    @Test
    public void test_integer_literal_aIntegerExpression(){
        AIntegerExpression aIntegerExpression = new AIntegerExpression();

        String result = findExpression(aIntegerExpression);

        assertEquals("integer_literal", result);
    }

}
