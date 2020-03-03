package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public class ExpressionFinder {

    public static String findExpression(PExpression pExpression){

        if(pExpression instanceof AIdentifierExpression || pExpression instanceof ANatSetExpression){
            return "id";
        }

        if(pExpression instanceof AIntervalExpression){
            return "binary_exp";
        }

        if(pExpression instanceof AIntegerExpression){
            return "integer_literal";
        }


        throw new NullPointerException("Predicate not found"  + pExpression.getClass());
    }
}
