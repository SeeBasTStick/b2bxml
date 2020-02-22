package de.hhu.stups.bxmlgenerator.integration;

import org.junit.Test;


public class TestBXML {

    @Test
    public void test_Examples() throws Exception {
        String name = "Mega";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Lift() throws Exception {
        String name = "Lift";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_InnerOuterPre() throws Exception{
        String name = "Inner_Outer_PRE_Test";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Assert_Test() throws Exception{
        String name = "Assert_Test";
        BXMLTestUtils.calculateDifference(name);
    }



}