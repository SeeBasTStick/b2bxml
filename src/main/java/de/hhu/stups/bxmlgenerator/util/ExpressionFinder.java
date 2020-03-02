package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.AIdentifierExpression;
import de.be4.classicalb.core.parser.node.AIntegerExpression;
import de.be4.classicalb.core.parser.node.AIntervalExpression;
import de.be4.classicalb.core.parser.node.PExpression;

public class ExpressionFinder {

    public static String findExpression(PExpression pExpression){

        if(pExpression instanceof AIdentifierExpression){
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
