package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.node.*;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import de.prob.typechecker.btypes.IntegerType;

import java.util.List;

public interface STGroupGeneratorTestUtils {

    default ALessPredicate generateALessPredicate(PExpression left, PExpression right, Typechecker typechecker, BType typeLeft, BType typeRight){
        ALessPredicate aLessPredicate = new ALessPredicate();

        aLessPredicate.setLeft(left);
        typechecker.setType(left, typeLeft);

        aLessPredicate.setRight(right);
        typechecker.setType(right, typeRight);

        typechecker.setType(aLessPredicate, IntegerType.getInstance());

        return aLessPredicate;
    }

    default AGreaterPredicate generateAGreaterPredicate(PExpression left, PExpression right, Typechecker typechecker, BType typeLeft, BType typeRight){
        AGreaterPredicate aGreaterPredicate = new AGreaterPredicate();

        aGreaterPredicate.setLeft(left);
        typechecker.setType(left, typeLeft);

        aGreaterPredicate.setRight(right);
        typechecker.setType(right, typeRight);

        typechecker.setType(aGreaterPredicate, IntegerType.getInstance());

        return aGreaterPredicate;
    }

    default AAssignSubstitution generateAAssignSubstitution(String varName, BType varType,
                                                            PExpression rightSide, BType rightSideType,
                                                            Typechecker typechecker){
        typechecker.setType(rightSide, rightSideType);

        AIdentifierExpression aIdentifierExpression = generateIdentifierExpression(varName, typechecker, varType);

        AAssignSubstitution aAssignSubstitution = new AAssignSubstitution();

        aAssignSubstitution.setRhsExpressions(List.of(rightSide));

        aAssignSubstitution.setLhsExpression(List.of(aIdentifierExpression));

        typechecker.setType(aAssignSubstitution, rightSideType);

        return aAssignSubstitution;

    }




    default AIntegerExpression generateIntegerExpression(int value, Typechecker typechecker){
        AIntegerExpression integerExpression = new AIntegerExpression();
        integerExpression.setLiteral(new TIntegerLiteral(String.valueOf(value)));
        typechecker.setType(integerExpression, IntegerType.getInstance());
        return integerExpression;
    }

    default AIdentifierExpression generateIdentifierExpression(String id, Typechecker typechecker, BType type){
        AIdentifierExpression aIdentifierExpression = new AIdentifierExpression();
        aIdentifierExpression.setIdentifier(List.of(new TIdentifierLiteral(id)));
        typechecker.setType(aIdentifierExpression, type);
        return aIdentifierExpression;
    }

    default AConjunctPredicate generateConjunctPredicate(PPredicate left, PPredicate right){
        AConjunctPredicate aConjunctPredicate = new AConjunctPredicate(left, right);
        aConjunctPredicate.setLeft(left);
        aConjunctPredicate.setRight(right);

        return aConjunctPredicate;
    }

    default String machineWithInterval() {
        return "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : 1..2 & y : NAT  & y >= 4\n" + "INITIALISATION x := 1 ; y:=2\n" + "END";
    }

    default String hashTestMachine() {
        return "MACHINE test \n" + "VARIABLES x, y\n"
                + "INVARIANT x : NAT & y : NAT \n" + "INITIALISATION x := 1 ; y:=2\n" + "END";
    }
}
