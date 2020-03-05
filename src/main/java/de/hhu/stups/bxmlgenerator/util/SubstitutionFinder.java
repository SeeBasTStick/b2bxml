package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.AAssignSubstitution;
import de.be4.classicalb.core.parser.node.PSubstitution;

public interface SubstitutionFinder {
    default String findSubstitution(PSubstitution substitution){
        if(substitution instanceof AAssignSubstitution){
            return "assignment_sub";
        }

        throw new NullPointerException("Substitution not found"  + substitution.getClass());

    }
}
