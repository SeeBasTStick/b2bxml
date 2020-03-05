package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public interface AbstractFinder extends ExpressionFinder, MachineClauseFinder, PredicateFinder, SubstitutionFinder {

    default String find(Node node){

        if(node instanceof PExpression)
        {
            return findExpression((PExpression) node);
        }
        if(node instanceof PMachineClause)
        {
            return findMachineClause((PMachineClause) node);
        }
        if(node instanceof PPredicate)
        {
            return findPredicate((PPredicate) node);
        }
        if(node instanceof PSubstitution)
        {
            return findSubstitution((PSubstitution) node);
        }

        throw new NullPointerException(node.getClass() + " not implemented/unknown");
    }

}
