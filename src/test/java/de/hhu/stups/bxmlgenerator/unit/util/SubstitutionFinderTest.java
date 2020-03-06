package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubstitutionFinderTest implements AbstractFinder {

    @Test
    public void test_AAssignSubstitution() {
        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();


        String result = find(aAssignSubstitution);


        assertEquals("assignment_sub", result);
    }

    @Test
    public void test_ASelectSubstitution() {
        ASelectSubstitution aSelectSubstitution = new ASelectSubstitution();


        String result = find(aSelectSubstitution);


        assertEquals("select", result);
    }

    @Test
    public void test_AParallelSubstitution() {
        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();

        String result = find(aParallelSubstitution);

        assertEquals("nary_sub", result);
    }

    @Test
    public void test_ASkipSubstitution() {
        ASkipSubstitution aSkipSubstitution = new ASkipSubstitution();


        String result = find(aSkipSubstitution);


        assertEquals("skip", result);
    }

    @Test
    public void test_AAssertionSubstitution() {
        AAssertionSubstitution aAssertionSubstitution = new AAssertionSubstitution();


        String result = find(aAssertionSubstitution);


        assertEquals("assert_sub", result);
    }

    @Test
    public void test_APreconditionSubstitution() {
        APreconditionSubstitution aPreconditionSubstitution = new APreconditionSubstitution();


        String result = find(aPreconditionSubstitution);


        assertEquals("pre_sub", result);
    }


}
