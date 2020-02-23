package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.OperationsGenerator;
import org.junit.Before;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

public class ReferenceGeneratorTest {

    OperationsGenerator operationsGenerator;

    @Before
    public void prepare(){
        operationsGenerator = new OperationsGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

}
