package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.ReferenceGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubs.highLevel.MachineReferenceNodeStub;
import de.prob.parser.ast.nodes.MachineReferenceNode;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReferenceGeneratorTest {

    ReferenceGenerator referenceGenerator;

    @Before
    public void prepare(){
        referenceGenerator = new ReferenceGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }


    @Test
    public void test_generateReference(){
        MachineReferenceNodeStub machineReferenceNodeStub =
                new MachineReferenceNodeStub("test", MachineReferenceNode.Kind.EXTENDED);

        String result = referenceGenerator.generateReference(machineReferenceNodeStub);

        assertEquals("<Extends>\n" +
                "    <Referenced_Machine>\n" +
                "        <Name>test</Name>\n" +
                "    </Referenced_Machine>\n" +
                "</Extends>", result);
    }

    @Test
    public void test_generateReferences(){
        MachineReferenceNodeStub machineReferenceNodeStub1 =
                new MachineReferenceNodeStub("test", MachineReferenceNode.Kind.EXTENDED);

        MachineReferenceNodeStub machineReferenceNodeStub2 =
                new MachineReferenceNodeStub("test", MachineReferenceNode.Kind.EXTENDED);

        List<MachineReferenceNode> inputList = new  ArrayList<>();

        inputList.add(machineReferenceNodeStub1);
        inputList.add(machineReferenceNodeStub2);

        List<String> result = referenceGenerator.generateReferences(inputList);

        for(String res : result){
            assertEquals("<Extends>\n" +
                    "    <Referenced_Machine>\n" +
                    "        <Name>test</Name>\n" +
                    "    </Referenced_Machine>\n" +
                    "</Extends>", res);
        }
    }


}
