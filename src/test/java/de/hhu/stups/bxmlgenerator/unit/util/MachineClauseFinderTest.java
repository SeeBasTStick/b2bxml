package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AInitialisationMachineClause;
import de.be4.classicalb.core.parser.node.AInvariantMachineClause;
import de.be4.classicalb.core.parser.node.AOperationsMachineClause;
import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.hhu.stups.bxmlgenerator.util.AbstractFinder;
import de.hhu.stups.bxmlgenerator.util.MachineClauseFinder;
import de.hhu.stups.bxmlgenerator.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineClauseFinderTest implements AbstractFinder {

    @Test
    public void test_invariant(){

        String toAssert = "invariant";

        String result = find(new AInvariantMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_initialisation(){

      String toAssert = "initialisation";

        String result = find(new AInitialisationMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_variables(){

        String toAssert = "abstract_variables";

        String result = find(new AVariablesMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_operations(){

        String toAssert = "operations";

        String result = find(new AOperationsMachineClause());

        assertEquals(toAssert, result);
    }
}
