package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.AbstractVariablesGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubs.highLevel.DeclarationNodeStub;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BoolType;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractVariableGeneratorTest {

    AbstractVariablesGenerator abstractVariablesGenerator;

    @Before
    public void prepare(){
        abstractVariablesGenerator = new AbstractVariablesGenerator(new HashMap<> (),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateAbstractVariables(){
        DeclarationNode declarationNode = new DeclarationNodeStub(BoolType.getInstance());
        assertEquals("<Abstract_Variables>\n" +
                        "    <Id value='test' typref='2044650'/>\n" +
                        "    <Id value='test' typref='2044650'/>\n" +
                        "</Abstract_Variables>",
                abstractVariablesGenerator.generateAbstractVariables(List.of(declarationNode, declarationNode)));
    }
}
