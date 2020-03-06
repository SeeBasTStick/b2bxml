package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.generators.STGroupGenerator;
import de.hhu.stups.bxmlgenerator.util.*;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

class STGroupGeneratorStub extends STGroupGenerator implements AbstractFinder {

    public STGroupGeneratorStub(HashMap<Integer, BType> nodeType, MachineContext ctx, Node startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg").getInstanceOf("machine"),
                nodeType,
                new Typechecker(ctx),
                startNode);
    }

    public STGroupGeneratorStub(Typechecker typechecker, Node startNode, String templateTarget) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")
                        .getInstanceOf(templateTarget),
                new HashMap<>(),
                typechecker,
                startNode);
    }




}
