package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Map;

public class InvariantGenerator extends BXMLBodyGenerator {
    public InvariantGenerator(Map<Integer, BType> nodeType,  STGroup currentGroup) {
        super(nodeType, currentGroup);
    }

    public String generateInvariants(PredicateNode node){
        ST invariant = currentGroup.getInstanceOf("invariant");
        TemplateHandler.add(invariant, "body", processPredicateNode(node));
        return invariant.render();
    }

}
