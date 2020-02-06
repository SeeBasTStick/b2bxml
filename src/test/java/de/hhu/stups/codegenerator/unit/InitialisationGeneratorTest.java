package de.hhu.stups.codegenerator.unit;

import de.hhu.stups.codegenerator.generators.InitialisationGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class InitialisationGeneratorTest extends DummyNodeGenerator{

    InitialisationGenerator initialisationGenerator;

    @Before
    public void prepare(){
        initialisationGenerator = new InitialisationGenerator(new HashMap<>(),
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateAbstractVariables(){

        assertEquals("<Initialisation>\n" +
                        "    <Assignement_Sub>\n" +
                        "        <Variables>\n" +
                        "            <Id value='test' typref='2044650'/>\n" +
                        "        </Variables>\n" +
                        "        <Values>\n" +
                        "            <Id value='test2' typref='2044650'/>\n" +
                        "        </Values>\n" +
                        "    </Assignement_Sub>\n" +
                        "</Initialisation>",
                initialisationGenerator.generateInitialisation(dummy_AssignSubstitutionNodeGenerator()));
    }
}
