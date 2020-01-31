package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.OperationNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsGenerator extends BXMLBodyGenerator {
    public OperationsGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateOperations(List<OperationNode> nodeList){
        ST operations = super.getSTGroup().getInstanceOf("operations");
        TemplateHandler.add(operations, "operation", nodeList.stream()
                .map(this::generateOperation)
                .collect(Collectors.toList()));
        return operations.render();
    }

    private String generateOperation(OperationNode node){
        ST operation = super.getSTGroup().getInstanceOf("operation");
        TemplateHandler.add(operation, "name", node.getName());

        if(!node.getOutputParams().isEmpty())
        {
            TemplateHandler.add(operation, "output_parameter", node.getOutputParams().stream()
                    .map(this::processDeclarationNode)
                    .collect(Collectors.toList()));
        }

        if(!node.getParams().isEmpty())
        {
            System.out.println(node.getParams() + " operation not implemented yet");
        }

        TemplateHandler.add(operation, "body" , processSubstitutionNode(node.getSubstitution()));

        return  operation.render();
    }

}
