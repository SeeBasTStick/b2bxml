package de.hhu.stups.bxmlgenerator.antlr;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.MachineReferenceNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReferenceGenerator extends BXMLBodyGenerator {

    public ReferenceGenerator(Map<Integer, BType> nodeType, STGroup currentGroup) {
        super(nodeType, currentGroup);
    }

    public List<String> generateReferences(List<MachineReferenceNode> nodeList){
        return nodeList.stream().map(this::generateReference).collect(Collectors.toList());
    }

    public String generateReference(MachineReferenceNode node){
        String result;
        switch (node.getType()){
            case EXTENDED:
                ST extended = currentGroup.getInstanceOf("extends");
                TemplateHandler.add(extended, "name", node.getMachineName());
                result = extended.render();
                break;
            default:
                result = exceptionThrower(node);
        }

        return result;
    }
}
