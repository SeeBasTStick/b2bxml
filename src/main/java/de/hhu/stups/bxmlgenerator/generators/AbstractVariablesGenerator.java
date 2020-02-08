package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
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

    public String generateAbstractVariables(List<DeclarationNode> declarations){
        ST typeInfo = super.getSTGroup().getInstanceOf("abstract_variable");
        TemplateHandler.add(typeInfo, "ids", declarations.stream()
                .map(super::processDeclarationNode)
                .collect(Collectors.toList()));

        //TODO Attrs

        return typeInfo.render();
    }

}
