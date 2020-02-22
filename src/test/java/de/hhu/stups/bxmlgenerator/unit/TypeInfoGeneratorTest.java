package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.TypeInfoGenerator;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.IntegerType;
import de.prob.parser.ast.types.SetType;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TypeInfoGeneratorTest {

    private TypeInfoGenerator typeInfoGenerator;


    @Test
    public void test_generateTyp_Integer(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = IntegerType.getInstance();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), input);

        typeInfoGenerator  = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTyp(Math.abs(input.toString().hashCode()));

        assertEquals("<Type id='1618932450'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Type>", result);
    }

    @Test
    public void test_generateTyp_Set(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = IntegerType.getInstance();

        nodeTypes.put(Math.abs(input.toString().hashCode()), input);

        typeInfoGenerator  = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTyp(Math.abs(input.toString().hashCode()));

        assertEquals("<Type id='1618932450'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Type>", result);
    }



    @Test
    public void test_generateTypeInfo_Integer(){

        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), IntegerType.getInstance());

        typeInfoGenerator = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTypeInfo();

        assertEquals("<TypeInfos>\n" +
                "    <Type id='1618932450'>\n" +
                "        <Id value='INTEGER'/>\n" +
                "    </Type>\n" +
                "</TypeInfos>", result);
    }

    @Test
    public void test_generateTypeInfo_Set(){

        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = new SetType(IntegerType.getInstance());

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), input);

        typeInfoGenerator = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTypeInfo();

        assertEquals("<TypeInfos>\n" +
                "    <Type id='1618932450'>\n" +
                "        <Unary_Exp op='POW'>\n" +
                "            <Id value='INTEGER'/>\n" +
                "        </Unary_Exp>\n" +
                "    </Type>\n" +
                "</TypeInfos>", result);
    }

    @Test
    public void test_generateTypInformation_Set(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = new SetType(IntegerType.getInstance());

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), input);

        typeInfoGenerator = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTypeInformation(input);

        assertEquals("<Unary_Exp op='POW'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Unary_Exp>", result);
    }

    @Test
    public void test_generateTypeInformation_Integer(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = IntegerType.getInstance();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), input);

        typeInfoGenerator = new TypeInfoGenerator(nodeTypes,
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));

        String result = typeInfoGenerator.generateTypeInformation(input);

        assertEquals("<Id value='INTEGER'/>", result);
    }
}

