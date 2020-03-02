package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.*;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceGenerator extends STGroupGenerator {


    public ReferenceGenerator(STGroupFile stGroupFile, ST group, HashMap<Integer, BType> nodeType, Typechecker typeChecker, Node startNode) {
        super(stGroupFile, group, nodeType, typeChecker, startNode);
    }



    @Override
    public void caseADefinitionsMachineClause(ADefinitionsMachineClause node)
    {
        List<PDefinition> copy = new ArrayList<>(node.getDefinitions());
        for(PDefinition e : copy)
        {
            e.apply(this);
        }
    }

    @Override
    public void caseASeesMachineClause(ASeesMachineClause node)
    {
        List<PExpression> copy = new ArrayList<>(node.getMachineNames());
        for(PExpression e : copy)
        {
            e.apply(this);
        }
    }

    @Override
    public void caseAPromotesMachineClause(APromotesMachineClause node)
    {

        List<PExpression> copy = new ArrayList<>(node.getOperationNames());
        for(PExpression e : copy)
        {
            e.apply(this);
        }

    }

    @Override
    public void caseAUsesMachineClause(AUsesMachineClause node)
    {

        List<PExpression> copy = new ArrayList<>(node.getMachineNames());
        for(PExpression e : copy)
        {
            e.apply(this);
        }

    }

    @Override
    public void caseAIncludesMachineClause(AIncludesMachineClause node)
    {
        List<PMachineReference> copy = new ArrayList<>(node.getMachineReferences());
        for(PMachineReference e : copy)
        {
           e.apply(this);
        }
    }

    @Override
    public void caseAExtendsMachineClause(AExtendsMachineClause node)
    {
        List<PMachineReference> copy = new ArrayList<>(node.getMachineReferences());
        for(PMachineReference e : copy)
        {
           e.apply(this);
        }
    }

    @Override
    public void caseAImportsMachineClause(AImportsMachineClause node)
    {
        List<PMachineReference> copy = new ArrayList<>(node.getMachineReferences());
        for(PMachineReference e : copy)
        {
            e.apply(this);
        }

    }
}
