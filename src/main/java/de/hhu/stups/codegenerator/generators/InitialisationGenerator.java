package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.nodes.substitution.SubstitutionNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.STGroup;

import java.util.Map;

public class InitialisationGenerator extends BXMLBodyGenerator{
    public InitialisationGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateInitialisation(SubstitutionNode node){
       return processSubstitutionNode(node);
    }
}
