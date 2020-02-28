package de.hhu.stups.bxmlgenerator.integration;

import org.junit.Test;


public class TestBXML {

    @Test
    public void test_Examples() throws Exception {
        String name = "Mega.mch";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Lift() throws Exception {
        String name = "Lift.mch";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_InnerOuterPre() throws Exception{
        String name = "Inner_Outer_PRE_Test.mch";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Assert_Test() throws Exception{
        String name = "Assert_Test.mch";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Simple_Extends_Test() throws Exception {
        String name = "references/extends/MachineB.mch";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Simple_Refines_Test() throws Exception{
        String name = "references/refines/refinement/Lift2.ref";
        BXMLTestUtils.calculateDifference(name);
    }

    @Test
    public void test_Simple_IMPLEMENTATION_Test() throws Exception{
        String name = "references/refines/implementation/RefinementMachine.imp";
        BXMLTestUtils.calculateDifference(name);
    }
}