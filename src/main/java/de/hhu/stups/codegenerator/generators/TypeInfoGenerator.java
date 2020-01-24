package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TypeInfoGenerator  {


    NameHandler nameHandler;

    STGroup currentGroup;

    public TypeInfoGenerator(NameHandler nameHandler, STGroup currentGroup) {
        this.currentGroup = currentGroup;
        this.nameHandler = nameHandler;
    }

    public String generateTypeInfo(List<DeclarationNode> variables){
        ST typeInfo = currentGroup.getInstanceOf("type_info");
        TemplateHandler.add(typeInfo, "types", variables.stream()
                .map(this::generateTyp)
                .collect(Collectors.toList()));

        return typeInfo.render();
    }

    public String generateTyp(DeclarationNode variable)
    {
        System.out.println("l");
        ST type = currentGroup.getInstanceOf("type");
        TemplateHandler.add(type, "id", nameHandler.handleIdentifier(variable.getName(),
                NameHandler.IdentifierHandlingEnum.FUNCTION_NAMES));

        return type.render();
    }
}
