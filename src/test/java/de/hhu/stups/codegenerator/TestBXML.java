package de.hhu.stups.codegenerator;

import org.junit.Test;


public class TestBXML {

    @Test
    public void testExamples() throws Exception {
        String name = "Mega";
        BXMLTestUtils.calculateDifference(name);
    }

}