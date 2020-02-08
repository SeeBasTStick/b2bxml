package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.TypeInfoGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.IntegerType;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TypeInfoGeneratorTest {

    private TypeInfoGenerator typeInfoGenerator;

    @Test
    public void test_generateTypeInfo(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), IntegerType.getInstance());

        typeInfoGenerator  = new TypeInfoGenerator(nodeTypes,
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        assertEquals("<Type id='1618932450'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Type>", typeInfoGenerator.generateTyp(Math.abs(IntegerType.getInstance().toString().hashCode())));
    }

    @Test
    public void test_generateTypeInfos(){

        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), IntegerType.getInstance());

        typeInfoGenerator  = new TypeInfoGenerator(nodeTypes,
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        assertEquals("<TypeInfos>\n" +
                "    <Type id='1618932450'>\n" +
                "        <Id value='INTEGER'/>\n" +
                "    </Type>\n" +
                "</TypeInfos>", typeInfoGenerator.generateTypeInfo());
    }
}

