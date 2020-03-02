package de.hhu.stups.bxmlgenerator.antlr;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Map;

public class InitialisationGenerator extends BXMLBodyGenerator{
    public InitialisationGenerator(Map<Integer, BType> nodeType,  STGroup currentGroup) {
        super(nodeType, currentGroup);
    }

    public String generateInitialisation(SubstitutionNode node){
        ST initialisation = getSTGroup().getInstanceOf("initialisation");
        TemplateHandler.add(initialisation, "body", visitSubstitutionNode(node, null));
        return initialisation.render();
    }
}