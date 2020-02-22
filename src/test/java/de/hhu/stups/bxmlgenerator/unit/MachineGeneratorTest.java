package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.MachineGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.expr.ExprOperatorNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.highLevel.DeclarationNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubInterfaces.substitution.AssignSubstituteStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.types.BoolType;
import de.prob.parser.ast.types.IntegerType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MachineGeneratorTest extends BXMLBodyGeneratorTest {

    @Test
    public void test_generateMachine(){
        MachineGenerator machineGenerator = new MachineGenerator();
        MachineNode machineNode = new MachineNode(new SourceCodePosition());
        machineNode.setName("TEST");

        machineNode.setConstants(List.of(new DeclarationNodeStub(IntegerType.getInstance())));
        machineNode.setInitialisation(new AssignSubstituteStub());
        machineNode.setInvariant(new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.PLUS, BoolType.getInstance()))));

        OperationNode operationNode1 = new OperationNode(new SourceCodePosition(), "dec", new ArrayList<>(),
                new AssignSubstituteStub(), new ArrayList<>());

        OperationNode operationNode2 = new OperationNode(new SourceCodePosition(), "dec",
                List.of(new DeclarationNodeStub(IntegerType.getInstance())),
                new AssignSubstituteStub(), new ArrayList<>());


        machineNode.setOperations(List.of(operationNode1, operationNode2));
        assertEquals("<Machine name='TEST' type='abstraction'>\n" +
                "    <Abstract_Variables>\n" +
                "    </Abstract_Variables>\n" +
                "    <Invariant>\n" +
                "        <Exp_Comparison op=':'>\n" +
                "            <Binary_Exp op='+' typref='2044650'>\n" +
                "            </Binary_Exp>\n" +
                "        </Exp_Comparison>\n" +
                "    </Invariant>\n" +
                "    <Initialisation>\n" +
                "        <Assignement_Sub>\n" +
                "            <Variables>\n" +
                "                <Id value='ii' typref='1618932450'/>\n" +
                "            </Variables>\n" +
                "            <Values>\n" +
                "                <Integer_Literal value='42' typref='1618932450'/>\n" +
                "            </Values>\n" +
                "        </Assignement_Sub>\n" +
                "    </Initialisation>\n" +
                "    <Operations>\n" +
                "        <Operation name='dec'>\n" +
                "            <Body>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='ii' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='42' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Body>\n" +
                "        </Operation>\n" +
                "        <Operation name='dec'>\n" +
                "            <Output_Parameters>\n" +
                "                <Id value='test' typref='1618932450'/>\n" +
                "            </Output_Parameters>\n" +
                "            <Body>\n" +
                "                <Assignement_Sub>\n" +
                "                    <Variables>\n" +
                "                        <Id value='ii' typref='1618932450'/>\n" +
                "                    </Variables>\n" +
                "                    <Values>\n" +
                "                        <Integer_Literal value='42' typref='1618932450'/>\n" +
                "                    </Values>\n" +
                "                </Assignement_Sub>\n" +
                "            </Body>\n" +
                "        </Operation>\n" +
                "    </Operations>\n" +
                "    <TypeInfos>\n" +
                "        <Type id='2044650'>\n" +
                "        </Type>\n" +
                "        <Type id='1618932450'>\n" +
                "            <Id value='INTEGER'/>\n" +
                "        </Type>\n" +
                "    </TypeInfos>\n" +
                "</Machine>", machineGenerator.generateMachine(machineNode));
    }

}
