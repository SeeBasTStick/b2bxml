package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AAssignSubstitution;
import de.be4.classicalb.core.parser.node.AGreaterPredicate;
import de.be4.classicalb.core.parser.node.AParallelSubstitution;
import de.be4.classicalb.core.parser.node.ASelectSubstitution;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubstitutionFinderTest implements AbstractFinder {

    @Test
    public void test_AAssignSubstitution(){
        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();


        String result = find(aAssignSubstitution);


        assertEquals("assignment_sub", result);
    }

    @Test
    public void test_ASelectSubstitution(){
        ASelectSubstitution aSelectSubstitution = new ASelectSubstitution();


        String result = find(aSelectSubstitution);


        assertEquals("select", result);
    }

    @Test
    public void test_AParallelSubstitution(){
        AParallelSubstitution aParallelSubstitution = new AParallelSubstitution();


        String result = find(aParallelSubstitution);


        assertEquals("nary_sub", result);
    }

}
