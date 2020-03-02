package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.AVariablesMachineClause;
import de.be4.classicalb.core.parser.node.PExpression;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbstractVariableGenerator extends STGroupGenerator implements SubGenerator{

    AVariablesMachineClause startNode;

    public AbstractVariableGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer,
            BType> nodeType, Typechecker typeChecker, AVariablesMachineClause startNode) {
        super(stGroupFile, group, nodeType, typeChecker, startNode);
        this.startNode = startNode;
    }


    public String generateAllExpression(){

        System.out.println(getStGroupFile());
        TemplateHandler.add(getCurrentGroup(), "ids", generateSubExpression());
        return getCurrentGroup().render();
    }

    public List<String> generateSubExpression() {
        List<PExpression> copy = new ArrayList<>(startNode.getIdentifiers());

        List<String> result = new ArrayList<>();
        for (PExpression e : copy) {
            result.add(
                    new STGroupGenerator(getStGroupFile(),
                            getStGroupFile().getInstanceOf("id"), getNodeType(), getTypechecker(), e)
                            .generateCurrent());
        }

        return result;
    }
}
