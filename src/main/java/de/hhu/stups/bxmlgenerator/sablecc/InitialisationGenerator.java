package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.Node;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

public class InitialisationGenerator extends STGroupGenerator{
    public InitialisationGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer, BType> nodeType, Typechecker typeChecker, Node startNode) {
        super(stGroupFile, group, nodeType, typeChecker, startNode);
    }

    public String generateAllExpression() {
        return "";
    }
}
