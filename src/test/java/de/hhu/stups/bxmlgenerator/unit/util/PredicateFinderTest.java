package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AConjunctPredicate;
import de.be4.classicalb.core.parser.node.AGreaterEqualPredicate;
import de.be4.classicalb.core.parser.node.ALessPredicate;
import de.be4.classicalb.core.parser.node.AMemberPredicate;
import de.hhu.stups.bxmlgenerator.util.PredicateFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PredicateFinderTest {

    @Test
    public void test_exp_comparision_aLessPredicate(){
        ALessPredicate aLessPredicate = new ALessPredicate();

        String result = PredicateFinder.findPredicate(aLessPredicate);

        assertEquals("exp_comparision", result);

    }

    @Test
    public void test_exp_comparision_aGreaterEqualPredicate(){
        AGreaterEqualPredicate aGreaterEqualPredicate = new AGreaterEqualPredicate();

        String result = PredicateFinder.findPredicate(aGreaterEqualPredicate);


        assertEquals("exp_comparision", result);

    }

    @Test
    public void test_exp_comparision_aMemberPredicate(){
        AMemberPredicate aMemberPredicate = new AMemberPredicate();

        String result = PredicateFinder.findPredicate(aMemberPredicate);


        assertEquals("exp_comparision", result);

    }

    @Test
    public void test_nary_pred_aConjunctPredicate(){
        AConjunctPredicate aConjunctPredicate = new AConjunctPredicate();

        String result = PredicateFinder.findPredicate(aConjunctPredicate);

        assertEquals("nary_pred", result);
    }
}
