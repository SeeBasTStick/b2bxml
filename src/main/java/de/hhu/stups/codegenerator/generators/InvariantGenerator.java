package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Map;
import java.util.stream.Collectors;

public class InvariantGenerator extends BXMLBodyGenerator {
    public InvariantGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateInvariants(PredicateNode node){

        String result;
        switch (node.getClass().getSimpleName()){
            case "PredicateOperatorNode":
                PredicateOperatorNode predicateOperatorNode = (PredicateOperatorNode) node;
                ST invariant = super.getSTGroup().getInstanceOf("invariant");
                TemplateHandler.add(invariant, "body", processPredicateOperatorNode(predicateOperatorNode));
                result = invariant.render();
                // do stuff

                break;
            default:
                result = exceptionThrower(node);
        }


        return result;
    }

    private String processPredicateOperatorNode(PredicateOperatorNode node)
    {
        String result = "";
        if(node.getPredicateArguments().size() == 1){
            result = "";
            //ToDo ExprComparision and more
        }
        else{
            //System.out.println(node);
            ST nary_pred = super.getSTGroup().getInstanceOf("nary_pred");
            TemplateHandler.add(nary_pred, "op", generateOperatorNAry(node));
            TemplateHandler.add(nary_pred, "statements", node.getPredicateArguments().stream()
                    .map(this::processPredicateNode)
                    .collect(Collectors.toList()));
            result = nary_pred.render();

        }

        return result;
    }


    private String generateOperatorNAry(PredicateOperatorNode node)
    {
        return "&amp;";
    }


}
