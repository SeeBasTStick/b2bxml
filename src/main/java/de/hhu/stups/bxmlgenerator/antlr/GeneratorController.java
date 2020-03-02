package de.hhu.stups.bxmlgenerator.antlr;

import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

public class GeneratorController {

    private MachineGenerator machineGenerator;

    public GeneratorController(STGroupFile stGroupFile, HashMap<Integer, BType> integerBTypeHashMap){
        machineGenerator = new MachineGenerator(stGroupFile, integerBTypeHashMap);
    }

    public String start(MachineNode node){
        return machineGenerator.generateMachine(node);
    }
}
