package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public interface PredicateFinder {
    default String findPredicate(PPredicate predicate){

        if(predicate instanceof AConjunctPredicate){
            return "nary_pred";
        }

        if(predicate instanceof AMemberPredicate || predicate instanceof AGreaterEqualPredicate || predicate instanceof ALessPredicate){
            return "exp_comparision";
        }


        throw new NullPointerException("Predicate not found"  + predicate.getClass());
    }
}
