package de.hhu.stups.bxmlgenerator.unit.util;

import de.be4.classicalb.core.parser.node.AInitialisationMachineClause;
import de.be4.classicalb.core.parser.node.AInvariantMachineClause;
import de.be4.classicalb.core.parser.node.AOperationsMachineClause;
import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.hhu.stups.bxmlgenerator.util.MachineClauseFinder;
import de.hhu.stups.bxmlgenerator.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineClauseFinderTest {

    @Test
    public void test_invariant(){

        Pair<String, String> toAssert = new Pair<>("invariant","invariant");

        Pair<String, String> result = MachineClauseFinder.findMachineClause(new AInvariantMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_initialisation(){

        Pair<String, String> toAssert = new Pair<>("initialisation","initialisation");

        Pair<String, String> result = MachineClauseFinder.findMachineClause(new AInitialisationMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_variables(){

        Pair<String, String> toAssert = new Pair<>("abstract_variables","abstract_variable");

        Pair<String, String> result = MachineClauseFinder.findMachineClause(new AVariablesMachineClause());

        assertEquals(toAssert, result);
    }

    @Test
    public void test_operations(){

        Pair<String, String> toAssert = new Pair<>("operation","operations");

        Pair<String, String> result = MachineClauseFinder.findMachineClause(new AOperationsMachineClause());

        assertEquals(toAssert, result);
    }
}
