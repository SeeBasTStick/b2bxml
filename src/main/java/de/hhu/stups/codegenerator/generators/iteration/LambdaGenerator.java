package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.ImportGenerator;
import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.generators.TypeGenerator;
import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.LambdaNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;

/**
 * Created by fabian on 04.03.19.
 */
public class LambdaGenerator {

    private final STGroup group;

    private final MachineGenerator machineGenerator;

    private final IterationConstructGenerator iterationConstructGenerator;

    private final IterationConstructHandler iterationConstructHandler;

    private final IterationPredicateGenerator iterationPredicateGenerator;

    private final ImportGenerator importGenerator;

    private final TypeGenerator typeGenerator;

    public LambdaGenerator(final STGroup group, final MachineGenerator machineGenerator, final IterationConstructGenerator iterationConstructGenerator,
                                     final IterationConstructHandler iterationConstructHandler, final IterationPredicateGenerator iterationPredicateGenerator, final ImportGenerator importGenerator,
                                     final TypeGenerator typeGenerator) {
        this.group = group;
        this.machineGenerator = machineGenerator;
        this.iterationConstructGenerator = iterationConstructGenerator;
        this.iterationConstructHandler = iterationConstructHandler;
        this.iterationPredicateGenerator = iterationPredicateGenerator;
        this.importGenerator = importGenerator;
        this.typeGenerator = typeGenerator;
    }

    private void prepareGeneration(PredicateNode predicate, List<DeclarationNode> declarations, BType type) {
        iterationConstructGenerator.addBoundedVariables(declarations);
        iterationPredicateGenerator.checkPredicate(predicate, declarations);
        importGenerator.addImportInIteration(type);
    }

    public String generateLambda(LambdaNode node) {
        PredicateNode predicate = node.getPredicate();
        List<DeclarationNode> declarations = node.getDeclarations();
        ExprNode expression = node.getExpression();
        BType type = node.getType();

        prepareGeneration(predicate, declarations, type);

        ST template = group.getInstanceOf("lambda");
        generateOtherIterationConstructs(template, predicate, expression);

        int iterationConstructCounter = iterationConstructHandler.getIterationConstructCounter();
        String identifier = "_ic_set_" + iterationConstructCounter;
        generateBody(template, identifier, predicate, declarations, expression, type);

        String result = template.render();
        addGeneration(node.toString(), identifier, declarations, result);
        return result;
    }

    private void generateOtherIterationConstructs(ST template, PredicateNode predicate, ExprNode expression) {
        IterationConstructGenerator otherConstructsGenerator = iterationConstructHandler.inspectExpression(
                iterationConstructHandler.inspectPredicate(predicate), expression);
        for (String key : otherConstructsGenerator.getIterationsMapIdentifier().keySet()) {
            iterationConstructGenerator.getIterationsMapIdentifier().put(key, otherConstructsGenerator.getIterationsMapIdentifier().get(key));
        }
        TemplateHandler.add(template, "otherIterationConstructs", otherConstructsGenerator.getIterationsMapCode().values());
    }

    private void generateBody(ST template, String identifier, PredicateNode predicate, List<DeclarationNode> declarations, ExprNode expression, BType type) {
        List<ST> enumerationTemplates = iterationPredicateGenerator.getEnumerationTemplates(declarations, predicate);

        iterationConstructHandler.setIterationConstructGenerator(iterationConstructGenerator);

        String innerBody = generateLambdaExpression(predicate, expression, identifier, "_ic_" + declarations.get(declarations.size() - 1).getName());
        String lambda = iterationPredicateGenerator.evaluateEnumerationTemplates(enumerationTemplates, innerBody).render();

        TemplateHandler.add(template, "type", typeGenerator.generate(type));
        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "lambda", lambda);
    }

    private void addGeneration(String node, String identifier, List<DeclarationNode> declarations, String result) {
        iterationConstructGenerator.addIteration(node, identifier, result);
        iterationConstructGenerator.clearBoundedVariables(declarations);
    }

    public String generateLambdaExpression(PredicateNode predicateNode, ExprNode expression, String relationName, String elementName) {
        //TODO only take end of predicate arguments
        ST template = group.getInstanceOf("lambda_expression");
        machineGenerator.inIterationConstruct();
        TemplateHandler.add(template, "predicate", machineGenerator.visitPredicateNode(predicateNode, null));
        TemplateHandler.add(template, "relation", relationName);
        TemplateHandler.add(template, "element", elementName);
        TemplateHandler.add(template, "expression", machineGenerator.visitExprNode(expression, null));
        machineGenerator.leaveIterationConstruct();
        return template.render();
    }

}
