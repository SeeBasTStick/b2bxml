package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public interface SubstitutionFinder {
    default String findSubstitution(PSubstitution substitution){
        if(substitution instanceof AAssignSubstitution){
            return "assignment_sub";
        }
        if(substitution instanceof ASelectSubstitution){
            return "select";
        }
        if(substitution instanceof AParallelSubstitution){
            return "nary_sub";
        }
        if(substitution instanceof ASkipSubstitution){
            return "skip";
        }
        if(substitution instanceof AAssertionSubstitution){
            return "assert_sub";
        }
        if(substitution instanceof APreconditionSubstitution){
            return "pre_sub";
        }

        throw new NullPointerException("Substitution not found"  + substitution.getClass());

    }
}
