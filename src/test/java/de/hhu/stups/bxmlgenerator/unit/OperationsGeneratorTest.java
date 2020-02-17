package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.OperationsGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.types.BoolType;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OperationsGeneratorTest extends DummyNodeGenerator {

    OperationsGenerator operationsGenerator;

    @Before
    public void prepare(){
        operationsGenerator = new OperationsGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateOperation_1(){
        OperationNode operationNode = new OperationNode(new SourceCodePosition(), "dec", new ArrayList<>(),
                dummy_AssignSubstitutionNodeGenerator(), new ArrayList<>());

        assertEquals("<Operation name='dec'>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='test' typref='2044650'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Id value='test2' typref='2044650'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", operationsGenerator.generateOperation(operationNode));
    }


    @Test
    public void test_generateOperation_2(){
        OperationNode operationNode = new OperationNode(new SourceCodePosition(), "dec",
                List.of(dummy_DeclarationNodeGenerator(BoolType.getInstance())),
                dummy_AssignSubstitutionNodeGenerator(), new ArrayList<>());

        assertEquals("<Operation name='dec'>\n" +
                "    <Output_Parameters>\n" +
                "        <Id value='test' typref='2044650'/>\n" +
                "    </Output_Parameters>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='test' typref='2044650'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Id value='test2' typref='2044650'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", operationsGenerator.generateOperation(operationNode));
    }

    @Test
    public void test_generateOperations(){
        OperationNode operationNode1 = new OperationNode(new SourceCodePosition(), "dec", new ArrayList<>(),
                dummy_AssignSubstitutionNodeGenerator(), new ArrayList<>());

        OperationNode operationNode2 = new OperationNode(new SourceCodePosition(), "dec",
                List.of(dummy_DeclarationNodeGenerator(BoolType.getInstance())),
                dummy_AssignSubstitutionNodeGenerator(), new ArrayList<>());


        assertEquals("<Operations>\n" +
                "    <Operation name='dec'>\n" +
                "        <Body>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='test' typref='2044650'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Id value='test2' typref='2044650'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Body>\n" +
                "    </Operation>\n" +
                "    <Operation name='dec'>\n" +
                "        <Output_Parameters>\n" +
                "            <Id value='test' typref='2044650'/>\n" +
                "        </Output_Parameters>\n" +
                "        <Body>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='test' typref='2044650'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Id value='test2' typref='2044650'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Body>\n" +
                "    </Operation>\n" +
                "</Operations>", operationsGenerator.generateOperations(List.of(operationNode1, operationNode2)));
    }


}
