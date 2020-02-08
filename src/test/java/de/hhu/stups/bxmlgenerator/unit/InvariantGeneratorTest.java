package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.generators.InvariantGenerator;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.SourceCodePosition;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.types.BoolType;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InvariantGeneratorTest extends DummyNodeGenerator{

    InvariantGenerator invariantGenerator;

    @Before
    public void prepare(){
        invariantGenerator = new InvariantGenerator(new HashMap<>(),
                new NameHandler(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateInvariants(){
        ExpressionOperatorNode expressionOperatorNode = dummy_ExpressionOperatorNodeGenerator(BoolType.getInstance(),
                ExpressionOperatorNode.ExpressionOperator.PLUS);

        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNode(new SourceCodePosition(),
                PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF,
                List.of(expressionOperatorNode));

        assertEquals("<Invariant>\n" +
                "    <Exp_Comparison op=':'>\n" +
                "        <Binary_Exp op='+' typref='2044650'>\n" +
                "        </Binary_Exp>\n" +
                "    </Exp_Comparison>\n" +
                "</Invariant>", invariantGenerator.generateInvariants(predicateNode));
    }
}
