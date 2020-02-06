package de.hhu.stups.codegenerator.unit;

import de.hhu.stups.codegenerator.generators.AbstractVariablesGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
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
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateAbstractVariables(){
        DeclarationNode declarationNode = new DeclarationNode(new SourceCodePosition(), "test",
                DeclarationNode.Kind.CONSTANT, new MachineNode(new SourceCodePosition()));
        BType type = BoolType.getInstance();
        declarationNode.setType(type);
        assertEquals("<Abstract_Variables>\n" +
                        "    <Id value='test' typref='2044650'/>\n" +
                        "    <Id value='test' typref='2044650'/>\n" +
                        "</Abstract_Variables>",
                abstractVariablesGenerator.generateAbstractVariables(List.of(declarationNode, declarationNode)));
    }
}
