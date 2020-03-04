package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public class MachineClauseFinder {
    
    public static Pair<String, String> findMachineClause(PMachineClause machineClause){
        if(machineClause instanceof AInvariantMachineClause){
            return new Pair<>("invariant", "invariant");
        }
        if(machineClause instanceof AVariablesMachineClause){
            return new Pair<>("abstract_variables", "abstract_variable");
        }
        if(machineClause instanceof AInitialisationMachineClause){
            return new Pair<>("initialisation", "initialisation");
        }
        if(machineClause instanceof AOperationsMachineClause){
            return new Pair<>("operations", "operations");
        }

        throw new NullPointerException("MachineClause not found"  + machineClause.getClass());

    }
}
