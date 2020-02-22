package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.OperationsGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubs.highLevel.DeclarationNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.highLevel.OperationNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.substitution.AssignSubstituteNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.substitution.ConditionSubstitutionNodeStub;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.substitution.ConditionSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;
import de.prob.parser.ast.types.BoolType;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OperationsGeneratorTest{

    OperationsGenerator operationsGenerator;

    @Before
    public void prepare(){
        operationsGenerator = new OperationsGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateOperation_1(){
        OperationNode operationNode = new OperationNodeStub( "dec", new ArrayList<>(),
                new AssignSubstituteNodeStub(), new ArrayList<>());

        assertEquals("<Operation name='dec'>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='ii' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='42' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", operationsGenerator.generateOperation(operationNode));
    }


    @Test
    public void test_generateOperation_2(){
        OperationNode operationNode = new OperationNodeStub( "dec",
                List.of(new DeclarationNodeStub(BoolType.getInstance())),
                new AssignSubstituteNodeStub(), new ArrayList<>());

        assertEquals("<Operation name='dec'>\n" +
                "    <Output_Parameters>\n" +
                "        <Id value='test' typref='2044650'/>\n" +
                "    </Output_Parameters>\n" +
                "    <Body>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='ii' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='42' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Body>\n" +
                "</Operation>", operationsGenerator.generateOperation(operationNode));
    }

    @Test
    public void test_generateOperations(){
        OperationNode operationNode1 = new OperationNodeStub( "dec", new ArrayList<>(),
                new AssignSubstituteNodeStub(), new ArrayList<>());

        OperationNode operationNode2 = new OperationNodeStub( "dec",
                List.of(new DeclarationNodeStub(BoolType.getInstance())),
                new AssignSubstituteNodeStub(), new ArrayList<>());


        assertEquals("<Operations>\n" +
                "    <Operation name='dec'>\n" +
                "        <Body>\n" +
                "            <Assignement_Sub>\n" +
                "                <Variables>\n" +
                "                    <Id value='ii' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='42' typref='1618932450'/>\n" +
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
                "                    <Id value='ii' typref='1618932450'/>\n" +
                "                </Variables>\n" +
                "                <Values>\n" +
                "                    <Integer_Literal value='42' typref='1618932450'/>\n" +
                "                </Values>\n" +
                "            </Assignement_Sub>\n" +
                "        </Body>\n" +
                "    </Operation>\n" +
                "</Operations>", operationsGenerator.generateOperations(List.of(operationNode1, operationNode2)));
    }

    @Test
    public void test_visitConditionSubstitutionNodeTopLevel_CorrectNode_and_Precondition(){
        ConditionSubstitutionNode conditionSubstitutionNode =
                new ConditionSubstitutionNodeStub(ConditionSubstitutionNode.Kind.PRECONDITION);

        Pair<String, SubstitutionNode> result = operationsGenerator.visitConditionSubstitutionNodeTopLevel(conditionSubstitutionNode);

        assertEquals("<Precondition>\n" +
                "</Precondition>", result.getKey());

        assertThat(result.getValue(), instanceOf(SubstitutionNode.class));
    }

    @Test
    public void test_visitConditionSubstitutionNodeTopLevel_WrongNode_and_Precondition(){
        ConditionSubstitutionNode conditionSubstitutionNode =
                new ConditionSubstitutionNodeStub(ConditionSubstitutionNode.Kind.ASSERT);

        Pair<String, SubstitutionNode> result = operationsGenerator.visitConditionSubstitutionNodeTopLevel(conditionSubstitutionNode);

        assertEquals("", result.getKey());

        assertThat(result.getValue(), instanceOf(SubstitutionNode.class));

    }

    @Test
    public void test_visitConditionSubstitutionNodeTopLevel_CorrectNode_and_Assert(){
        AssignSubstituteNodeStub assignSubstituteNodeStub = new AssignSubstituteNodeStub();

        Pair<String, SubstitutionNode> result = operationsGenerator.visitConditionSubstitutionNodeTopLevel(assignSubstituteNodeStub);

        assertEquals("", result.getKey());

        assertThat(result.getValue(), instanceOf(SubstitutionNode.class));
    }


}
