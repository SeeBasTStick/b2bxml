package de.hhu.stups.bxmlgenerator.unit.util;

import de.hhu.stups.bxmlgenerator.util.Pair;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class PairTest {

    @Test
    public void test_equals_notEqualBoth(){
        Pair<Integer, Integer> pair1 = new Pair<>(1,2);
        Pair<Integer, Integer> pair2 = new Pair<>(2,1);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_equals_notEqualKey(){
        Pair<Integer, Integer> pair1 = new Pair<>(3,2);
        Pair<Integer, Integer> pair2 = new Pair<>(2,2);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_equals_notEqualValue(){
        Pair<Integer, Integer> pair1 = new Pair<>(2,2);
        Pair<Integer, Integer> pair2 = new Pair<>(2,3);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_equals_equalsItself(){
        Pair<Integer, Integer> pair1 = new Pair<>(2,2);

        assertTrue(pair1.equals(pair1));
    }

    @Test
    public void test_equals_notEqualsOtherObject(){
        Pair<Integer, Integer> pair1 = new Pair<>(2,2);
        String test = "";

        assertFalse(pair1.equals(test));
    }

    @Test
    public void test_equals_keyIsNullAndPartnerKeyNot(){
        Pair<Integer, Integer> pair1 = new Pair<>(null,2);
        Pair<Integer, Integer> pair2 = new Pair<>(2,3);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_equals_keyIsNullAndPartnerKeyIsNull(){
        Pair<Integer, Integer> pair1 = new Pair<>(null,2);
        Pair<Integer, Integer> pair2 = new Pair<>(null,2);

        assertTrue(pair1.equals(pair2));
    }

    @Test
    public void test_equals_valueIsNullAndPartnerValueNot(){
        Pair<Integer, Integer> pair1 = new Pair<>(2,4);
        Pair<Integer, Integer> pair2 = new Pair<>(2,null);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_equals_valueIsNullAndPartnerValueIsNull(){
        Pair<Integer, Integer> pair1 = new Pair<>(2,null);
        Pair<Integer, Integer> pair2 = new Pair<>(2, null);

        assertEquals(pair1, pair2);
    }

    @Test
    public void test_equals_valueIsNullAndPartnerValueIsNot(){
        Pair<Integer, Integer> pair1 = new Pair<>(2, null);
        Pair<Integer, Integer> pair2 = new Pair<>(2, 4);

        assertFalse(pair1.equals(pair2));
    }

    @Test
    public void test_toString(){
        Pair<Integer, Integer> pair1 = new Pair<>(2, 3);
        assertEquals("2=3", pair1.toString());
    }

}
