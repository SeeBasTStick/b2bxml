package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

/**
 * Gets a Predicate Node and Returns the corresponding template name
 */

public class PredicateFinder {

    public static String findPredicate(PPredicate predicate){

        if(predicate instanceof AConjunctPredicate){
            return "nary_pred";
        }

        if(predicate instanceof AMemberPredicate || predicate instanceof AGreaterEqualPredicate || predicate instanceof ALessPredicate){
            return "exp_comparision";
        }


        throw new NullPointerException("Predicate not found"  + predicate.getClass());
    }
}
