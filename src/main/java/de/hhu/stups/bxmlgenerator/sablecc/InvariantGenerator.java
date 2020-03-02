package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.AConjunctPredicate;
import de.be4.classicalb.core.parser.node.AInvariantMachineClause;
import de.be4.classicalb.core.parser.node.PPredicate;
import de.hhu.stups.bxmlgenerator.util.PredicateFinder;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;

public class InvariantGenerator extends STGroupGenerator implements SubGenerator{

    public InvariantGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer, BType> nodeType, Typechecker typeChecker, AInvariantMachineClause startNode) {
        super(stGroupFile, group, nodeType, typeChecker, startNode);
    }

    @Override
    public String generateAllExpression() {
        AInvariantMachineClause aInvariantMachineClause = (AInvariantMachineClause) getStartNode();

        PPredicate predicate = aInvariantMachineClause.getPredicates();

        STGroupGenerator stGroupGenerator = new STGroupGenerator(getStGroupFile(),
                getStGroupFile().getInstanceOf(PredicateFinder.findPredicate(predicate)),
                getNodeType(), getTypechecker(), predicate);

        TemplateHandler.add(getCurrentGroup(), "body",  stGroupGenerator.generateCurrent());
        return getCurrentGroup().render();

    }

    @Override
    public List<String> generateSubExpression() {

        return null;
    }


}
