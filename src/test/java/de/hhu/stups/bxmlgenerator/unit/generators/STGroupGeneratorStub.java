package de.hhu.stups.bxmlgenerator.unit.generators;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.generators.STGroupGenerator;
import de.hhu.stups.bxmlgenerator.util.ExpressionFinder;
import de.hhu.stups.bxmlgenerator.util.MachineClauseFinder;
import de.hhu.stups.bxmlgenerator.util.PredicateFinder;
import de.hhu.stups.bxmlgenerator.util.SubstitutionFinder;
import de.prob.typechecker.MachineContext;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

class STGroupGeneratorStub extends STGroupGenerator {

    public STGroupGeneratorStub(HashMap<Integer, BType> nodeType, MachineContext ctx, Node startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg").getInstanceOf("machine"),
                nodeType,
                new Typechecker(ctx),
                startNode, "");
    }

    public STGroupGeneratorStub(Typechecker typechecker, PExpression startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")
                        .getInstanceOf(ExpressionFinder.findExpression(startNode)),
                new HashMap<>(),
                typechecker,
                startNode, "");
    }

    public STGroupGeneratorStub(Typechecker typechecker, PPredicate startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")
                        .getInstanceOf(PredicateFinder.findPredicate(startNode)),
                new HashMap<>(),
                typechecker,
                startNode, "");
    }

    public STGroupGeneratorStub(Typechecker typechecker, PSubstitution startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")
                        .getInstanceOf(SubstitutionFinder.findSubstitution(startNode)),
                new HashMap<>(),
                typechecker,
                startNode, "");
    }

    public STGroupGeneratorStub(Typechecker typechecker, PMachineClause startNode) {
        super(new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg"),
                new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg")
                        .getInstanceOf(MachineClauseFinder.findMachineClause(startNode).getValue()),
                new HashMap<>(),
                typechecker,
                startNode, "");
    }


}
