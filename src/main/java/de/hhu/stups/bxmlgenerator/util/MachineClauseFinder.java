package de.hhu.stups.bxmlgenerator.util;

import de.be4.classicalb.core.parser.node.*;

public interface MachineClauseFinder {
    default String findMachineClause(PMachineClause machineClause){
        if(machineClause instanceof AInvariantMachineClause){
            return "invariant";
        }
        if(machineClause instanceof AVariablesMachineClause){
            return "abstract_variables";
        }
        if(machineClause instanceof AInitialisationMachineClause){
            return "initialisation";
        }
        if(machineClause instanceof AOperationsMachineClause){
            return "operations";
        }

        throw new NullPointerException("MachineClause not found"  + machineClause.getClass());

    }
}
