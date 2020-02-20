package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.nodes.substitution.*;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsGenerator extends BXMLBodyGenerator  {

    ST operation;

    public OperationsGenerator(Map<Integer, BType> nodeType, STGroup currentGroup) {
        super(nodeType, currentGroup);
    }

    public String generateOperations(List<OperationNode> nodeList) {
        ST operations = super.getSTGroup().getInstanceOf("operations");
        TemplateHandler.add(operations, "operation", nodeList.stream()
                .map(this::generateOperation)
                .collect(Collectors.toList()));
        return operations.render();
    }

    public String generateOperation(OperationNode node) {

        operation = super.getSTGroup().getInstanceOf("operation");

        TemplateHandler.add(operation, "name", node.getName());

        if (!node.getOutputParams().isEmpty()) {
            ST outputParameters = super.getSTGroup().getInstanceOf("output_parameters");
            TemplateHandler.add(outputParameters, "body", node.getOutputParams().stream()
                    .map(this::processDeclarationNode)
                    .collect(Collectors.toList()));

            TemplateHandler.add(operation, "output_parameters", outputParameters.render());
        }

        if (!node.getParams().isEmpty()) {
            System.out.println(node.getParams() + " operation not implemented yet");
        }

        TemplateHandler.add(operation, "body", visitSubstitutionNode(node.getSubstitution(), node));
        /*
         * Case distinction ASSERT und PRE
         */
        return operation.render();
    }

    /*
     * What happens here is not simple. AtelierB makes a difference between top and low level pre-condition
     * what that means is that we check in the "body" node if there is a top level precondition. If this is true we need
     * to alter the construct (aka the body of the operation) to add a tag <precondtion> before the actual body.
     * In case we find a inner PRE condition we just can solve the problem when we deal with the body.
     * In the method above, this is why we need to set a global variable. To do this we overwrite the conditionSubNode
     * of the parent class to catch this special case. Otherwise we hand the problem down.
     *
     * I know very dirty...
     */
    public String visitConditionSubstitutionNode(ConditionSubstitutionNode node, Object expected) {
        if (node.getKind() == ConditionSubstitutionNode.Kind.PRECONDITION) {
            ST precondition = super.getSTGroup().getInstanceOf("precondition");
            TemplateHandler.add(precondition, "body", processPredicateNode(node.getCondition()));
            TemplateHandler.add(operation, "precondition", precondition.render());
            return super.visitSubstitutionNode(node.getSubstitution(), node);
        }
        else{
            return super.visitSubstitutionNode(node, null);
        }
    }
}

