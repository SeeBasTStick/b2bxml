package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.AInitialisationMachineClause;
import de.be4.classicalb.core.parser.node.PSubstitution;
import de.hhu.stups.bxmlgenerator.util.SubstitutionFinder;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

public class InitialisationGenerator extends STGroupGenerator{



    public InitialisationGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer, BType> nodeType, Typechecker typeChecker,
                                   AInitialisationMachineClause startNode) {
        super(stGroupFile, group, nodeType, typeChecker, startNode);
    }

    public String generateAllExpression() {

        AInitialisationMachineClause aInitialisationMachineClause = (AInitialisationMachineClause) getStartNode();

        PSubstitution substitution = aInitialisationMachineClause.getSubstitutions();

        String target = SubstitutionFinder.findSubstitution(substitution);

        STGroupGenerator stGroupGenerator =
                new STGroupGenerator(getStGroupFile(), getStGroupFile().getInstanceOf(target), getNodeType(),
                        getTypechecker(),
                        substitution);

        TemplateHandler.add(getCurrentGroup(), "body", stGroupGenerator.generateCurrent());

        return generateCurrent();
    }
}
