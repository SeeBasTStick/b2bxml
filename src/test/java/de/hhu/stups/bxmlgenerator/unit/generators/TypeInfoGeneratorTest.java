package de.hhu.stups.bxmlgenerator.unit.generators;

import com.sun.jdi.BooleanType;
import de.hhu.stups.bxmlgenerator.generators.TypeInfoGenerator;
import de.prob.typechecker.btypes.BType;
import de.prob.typechecker.btypes.BoolType;
import de.prob.typechecker.btypes.IntegerType;
import de.prob.typechecker.btypes.SetType;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TypeInfoGeneratorTest {




    @Test
    public void test_generateTyp_Integer(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = IntegerType.getInstance();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), input);

        TypeInfoGenerator typeInfoGenerator  = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

        String result = typeInfoGenerator.generateTyp(Math.abs(input.toString().hashCode()));

        assertEquals("<Type id='1618932450'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Type>", result);
    }

    @Test
    public void test_generateTyp_Boolean(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = BoolType.getInstance();

        nodeTypes.put(Math.abs(BoolType.getInstance().toString().hashCode()), input);


        TypeInfoGenerator typeInfoGenerator  = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

        String result = typeInfoGenerator.generateTyp(Math.abs(input.toString().hashCode()));

        assertEquals("<Type id='2044650'>\n" +
                "    <Id value='BOOL'/>\n" +
                "</Type>", result);
    }

    @Test
    public void test_generateTyp_Set(){
        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        BType input = IntegerType.getInstance();

        nodeTypes.put(Math.abs(input.toString().hashCode()), input);

        TypeInfoGenerator typeInfoGenerator  = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

        String result = typeInfoGenerator.generateTyp(Math.abs(input.toString().hashCode()));

        assertEquals("<Type id='1618932450'>\n" +
                "    <Id value='INTEGER'/>\n" +
                "</Type>", result);
    }





    @Test
    public void test_generateTypeInfo_Integer(){

        HashMap<Integer, BType> nodeTypes = new HashMap<>();

        nodeTypes.put(Math.abs(IntegerType.getInstance().toString().hashCode()), IntegerType.getInstance());

        TypeInfoGenerator typeInfoGenerator = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

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

        TypeInfoGenerator typeInfoGenerator = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

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

        TypeInfoGenerator typeInfoGenerator = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

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

        TypeInfoGenerator typeInfoGenerator = new TypeInfoGenerator(
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                nodeTypes);

        String result = typeInfoGenerator.generateTypeInformation(input);

        assertEquals("<Id value='INTEGER'/>", result);
    }

}
