package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.InitialisationGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.substitution.AssignSubstituteStub;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class InitialisationGeneratorTest {

    InitialisationGenerator initialisationGenerator;

    @Before
    public void prepare(){
        initialisationGenerator = new InitialisationGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateAbstractVariables(){

        assertEquals("<Initialisation>\n" +
                        "    <Assignement_Sub>\n" +
                        "        <Variables>\n" +
                        "            <Id value='ii' typref='1618932450'/>\n" +
                        "        </Variables>\n" +
                        "        <Values>\n" +
                        "            <Integer_Literal value='42' typref='1618932450'/>\n" +
                        "        </Values>\n" +
                        "    </Assignement_Sub>\n" +
                        "</Initialisation>",
                initialisationGenerator.generateInitialisation(new AssignSubstituteStub()));
    }
}
