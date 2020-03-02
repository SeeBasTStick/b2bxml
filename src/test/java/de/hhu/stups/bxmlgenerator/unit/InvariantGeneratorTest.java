package de.hhu.stups.bxmlgenerator.unit;

import de.hhu.stups.bxmlgenerator.antlr.InvariantGenerator;
import de.hhu.stups.bxmlgenerator.unit.stubs.expr.ExprOperatorNodeStub;
import de.hhu.stups.bxmlgenerator.unit.stubs.predicat.PredicateOperatorWithExprArgsNodeStub;
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

public class InvariantGeneratorTest{

    InvariantGenerator invariantGenerator;

    @Before
    public void prepare(){
        invariantGenerator = new InvariantGenerator(new HashMap<>(),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"));
    }

    @Test
    public void test_generateInvariants(){
        ExpressionOperatorNode expressionOperatorNode =
                new ExprOperatorNodeStub(ExpressionOperatorNode.ExpressionOperator.PLUS, BoolType.getInstance());

        PredicateNode predicateNode = new PredicateOperatorWithExprArgsNodeStub(
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
