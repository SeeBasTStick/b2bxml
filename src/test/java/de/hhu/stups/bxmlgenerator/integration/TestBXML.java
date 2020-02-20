package de.hhu.stups.bxmlgenerator.integration;

import org.junit.Test;


public class TestBXML {

    @Test
    public void testExamples() throws Exception {
        String name = "Mega";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void testLift() throws Exception {
        String name = "Lift";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void testInnerOuterPre() throws Exception{
        String name = "Inner_Outer_PRE_Test";
        BXMLTestUtils.calculateDifference(name);
    }

}