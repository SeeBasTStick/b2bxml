package de.hhu.stups.bxmlgenerator;

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

}