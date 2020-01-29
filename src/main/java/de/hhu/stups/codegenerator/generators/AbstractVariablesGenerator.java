package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.Node;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractVariablesGenerator extends BXMLBodyGenerator {


    public AbstractVariablesGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateTypeInfo(List<DeclarationNode> variables){
        ST typeInfo = super.getSTGroup().getInstanceOf("abstract_variable");
        TemplateHandler.add(typeInfo, "ids", variables.stream()
                .map(this::generateIDs)
                .collect(Collectors.toList()));

        //TODO Attrs

        return typeInfo.render();
    }

    public String generateIDs(DeclarationNode variable)
    {
        ST id = super.getSTGroup().getInstanceOf("id");
        TemplateHandler.add(id, "name", super.getNameHandler().handleIdentifier(variable.getName(),
                NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

        TemplateHandler.add(id, "typref", variable.getType().hashCode());

        super.getNodeTyp().put(variable.getType().hashCode(), variable.getType());
        return id.render();
    }
}
