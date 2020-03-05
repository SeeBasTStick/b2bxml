package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AInitialisationMachineClause;
import de.be4.classicalb.core.parser.node.AInvariantMachineClause;
import de.be4.classicalb.core.parser.node.AOperationsMachineClause;
import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.hhu.stups.bxmlgenerator.util.MachineClauseFinder;
import de.hhu.stups.bxmlgenerator.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineClauseFinderTest implements MachineClauseFinder {

    @Test
    public void test_invariant(){

        String toAssert = "invariant";

        String result = findMachineClause(new AInvariantMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_initialisation(){

      String toAssert = "initialisation";

        String result = findMachineClause(new AInitialisationMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_variables(){

        String toAssert = "abstract_variables";

        String result = findMachineClause(new AVariablesMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_operations(){

        String toAssert = "operations";

        String result = findMachineClause(new AOperationsMachineClause());

        assertEquals(toAssert, result);
    }
}
