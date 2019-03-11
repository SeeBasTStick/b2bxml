package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.QuantifiedExpressionNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;

import static de.prob.parser.ast.nodes.expression.QuantifiedExpressionNode.QuantifiedExpressionOperator.PI;
import static de.prob.parser.ast.nodes.expression.QuantifiedExpressionNode.QuantifiedExpressionOperator.SIGMA;

/**
 * Created by fabian on 06.03.19.
 */
public class QuantifiedExpressionGenerator {

    private final STGroup group;

    private final MachineGenerator machineGenerator;

    private final NameHandler nameHandler;

    private final IterationConstructGenerator iterationConstructGenerator;

    private final IterationConstructHandler iterationConstructHandler;

    private final IterationPredicateGenerator iterationPredicateGenerator;

    public QuantifiedExpressionGenerator(final STGroup group, final MachineGenerator machineGenerator, final NameHandler nameHandler, final IterationConstructGenerator iterationConstructGenerator,
                                         final IterationConstructHandler iterationConstructHandler, final IterationPredicateGenerator iterationPredicateGenerator) {
        this.group = group;
        this.machineGenerator = machineGenerator;
        this.nameHandler = nameHandler;
        this.iterationConstructGenerator = iterationConstructGenerator;
        this.iterationConstructHandler = iterationConstructHandler;
        this.iterationPredicateGenerator = iterationPredicateGenerator;
    }

    public String generateQuantifiedExpresion(QuantifiedExpressionNode node) {
        PredicateNode predicate = node.getPredicateNode();
        List<DeclarationNode> declarations = node.getDeclarationList();
        ExprNode expression = node.getExpressionNode();
        BType type = node.getType();
        iterationConstructGenerator.prepareGeneration(predicate, declarations, type);

        QuantifiedExpressionNode.QuantifiedExpressionOperator operator = node.getOperator();
        boolean isInteger = !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_UNION) && !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_INTER);

        ST template = group.getInstanceOf("quantified_expression");
        generateOtherIterationConstructs(template, predicate, expression);

        int iterationConstructCounter = iterationConstructHandler.getIterationConstructCounter();
        String identifier = isInteger ? "_ic_integer_" + iterationConstructCounter : "_ic_set_"+ iterationConstructCounter;

        generateBody(template, identifier, operator, predicate, expression, declarations);

        String result = template.render();
        iterationConstructGenerator.addGeneration(node.toString(), identifier, declarations, result);
        return result;
    }

    private String getIdentity(QuantifiedExpressionNode.QuantifiedExpressionOperator operator) {
        String identity = "";
        if(operator == SIGMA) {
            identity = "0";
        } else if(operator == PI) {
            identity = "1";
        }
        return identity;
    }

    private String getOperation(QuantifiedExpressionNode.QuantifiedExpressionOperator operator) {
        boolean isInteger = !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_UNION) && !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_INTER);
        String operation;
        if(isInteger) {
            if(operator == SIGMA) {
                operation = "plus";
            } else {
                operation = "multiply";
            }
        } else {
            if(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_UNION) {
                operation = "union";
            } else {
                operation = "intersect";
            }
        }
        return nameHandler.handle(operation);
    }

    private void generateOtherIterationConstructs(ST template, PredicateNode predicate, ExprNode expression) {
        IterationConstructGenerator otherConstructsGenerator = iterationConstructHandler.inspectExpression(
                iterationConstructHandler.inspectPredicate(predicate), expression);
        for (String key : otherConstructsGenerator.getIterationsMapIdentifier().keySet()) {
            iterationConstructGenerator.getIterationsMapIdentifier().put(key, otherConstructsGenerator.getIterationsMapIdentifier().get(key));
        }
        TemplateHandler.add(template, "otherIterationConstructs", otherConstructsGenerator.getIterationsMapCode().values());
    }

    private void generateBody(ST template, String identifier, QuantifiedExpressionNode.QuantifiedExpressionOperator operator, PredicateNode predicate, ExprNode expression, List<DeclarationNode> declarations) {
        boolean isInteger = !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_UNION) && !(operator == QuantifiedExpressionNode.QuantifiedExpressionOperator.QUANTIFIED_INTER);

        List<ST> enumerationTemplates = iterationPredicateGenerator.getEnumerationTemplates(declarations, predicate);

        iterationConstructHandler.setIterationConstructGenerator(iterationConstructGenerator);

        String innerBody = generateQuantifiedExpressionEvaluation(predicate, identifier, getOperation(operator), expression);
        String evaluation = iterationPredicateGenerator.evaluateEnumerationTemplates(enumerationTemplates, innerBody).render();

        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "identity", getIdentity(operator));
        TemplateHandler.add(template, "isInteger", isInteger);
        TemplateHandler.add(template, "evaluation", evaluation);
    }

    private String generateQuantifiedExpressionEvaluation(PredicateNode predicateNode, String identifier, String operation, ExprNode expression) {
        //TODO only take end of predicate arguments
        ST template = group.getInstanceOf("quantified_expression_evaluation");
        machineGenerator.inIterationConstruct();

        TemplateHandler.add(template, "predicate", machineGenerator.visitPredicateNode(predicateNode, null));
        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "operation", operation);
        TemplateHandler.add(template, "expression", machineGenerator.visitExprNode(expression, null));
        machineGenerator.leaveIterationConstruct();
        return template.render();
    }

}
