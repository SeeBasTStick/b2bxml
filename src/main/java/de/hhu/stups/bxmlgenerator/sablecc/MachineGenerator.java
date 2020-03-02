package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineGenerator extends STGroupGenerator implements SubGenerator{

    private String machineName = "machine" + this.hashCode();

    private boolean hasRun = false;

    public MachineGenerator(STGroupFile stGroupFile, HashMap<Integer, BType> nodeType, Typechecker typeChecker,
                            Start startNode) {
        super(stGroupFile, stGroupFile.getInstanceOf("machine"), nodeType, typeChecker, startNode);
    }

    public String generateAllExpression(){
        if(!hasRun) {
            getStartNode().apply(this);
            hasRun = true;
        }
        return getCurrentGroup().render();
    }

    public List<String> generateSubExpression(){
        Start node = (Start) getStartNode();
        AAbstractMachineParseUnit abstractMachineParseUnit = (AAbstractMachineParseUnit) node.getPParseUnit();
        abstractMachineParseUnit.apply(this);
        List<String> result = new ArrayList<>();
        for(PMachineClause subElement : abstractMachineParseUnit.getMachineClauses()){
            if(subElement instanceof AVariablesMachineClause){
                result.add(getCurrentGroup().getAttribute("abstract_variables").toString());
            }
        }
        return result;
    }

    public String getMachineName(){
        return machineName;
    }

    @Override
    public void caseAAbstractMachineParseUnit(AAbstractMachineParseUnit node)
    {
        TemplateHandler.add(super.getCurrentGroup(), "kind", "abstraction");


        if(node.getVariant() != null)
        {
            node.getVariant().apply(this);
        }

        if(node.getHeader() != null)
        {

            node.getHeader().apply(this);
        }

        List<PMachineClause> copy = new ArrayList<>(node.getMachineClauses());
        for(PMachineClause e : copy)
        {
            e.apply(this);
        }
    }

    @Override
    public void caseAImplementationMachineParseUnit(AImplementationMachineParseUnit node)
    {
        TemplateHandler.add(super.getCurrentGroup(), "kind", "implementation");

        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        if(node.getRefMachine() != null)
        {
            node.getRefMachine().apply(this);
        }

        List<PMachineClause> copy = new ArrayList<>(node.getMachineClauses());
        for(PMachineClause e : copy)
        {
           e.apply(this);
        }
    }

    @Override
    public void caseARefinementMachineParseUnit(ARefinementMachineParseUnit node) {
        TemplateHandler.add(super.getCurrentGroup(), "kind", "refinement");

        if (node.getHeader() != null) {
            node.getHeader().apply(this);
        }
        if (node.getRefMachine() != null) {
            node.getRefMachine().apply(this);
        }

        List<PMachineClause> copy = new ArrayList<>(node.getMachineClauses());
        for (PMachineClause e : copy) {
           e.apply(this);
        }

    }

    @Override
    public void caseAMachineMachineVariant(AMachineMachineVariant node)
    {

    }

    @Override
    public void caseAModelMachineVariant(AModelMachineVariant node)
    {

    }

    @Override
    public void caseASystemMachineVariant(ASystemMachineVariant node)
    {

    }

    @Override
    public void caseAMachineHeader(AMachineHeader node)
    {
        machineName = node.getName().get(0).toString().replace(" ", "");

        TemplateHandler.add(getCurrentGroup(), "machine", machineName);

        List<TIdentifierLiteral> identifierLiteralList = new ArrayList<>(node.getName());
        for(TIdentifierLiteral e : identifierLiteralList)
        {
            e.apply(this);
        }

        List<PExpression> pExpressionList = new ArrayList<>(node.getParameters());
        for(PExpression e : pExpressionList)
        {
            e.apply(this);
        }
    }

    @Override
    public void caseAMachineClauseParseUnit(AMachineClauseParseUnit node)
    {
        if(node.getMachineClause() != null)
        {
            node.getMachineClause().apply(this);
        }
    }


    @Override
    public void caseASetsMachineClause(ASetsMachineClause node)
    {
        inASetsMachineClause(node);
        {
            List<PSet> copy = new ArrayList<PSet>(node.getSetDefinitions());
            for(PSet e : copy)
            {
                e.apply(this);
            }
        }
        outASetsMachineClause(node);
    }

    @Override
    public void caseAFreetypesMachineClause(AFreetypesMachineClause node)
    {
            List<PFreetype> copy = new ArrayList<>(node.getFreetypes());
            for(PFreetype e : copy)
            {
                e.apply(this);
            }
    }

    @Override
    public void caseAVariablesMachineClause(AVariablesMachineClause node)
    {
        AbstractVariableGenerator abstractVariableGenerator = new AbstractVariableGenerator(getStGroupFile(),
                getStGroupFile().getInstanceOf("abstract_variable"), getNodeType(), getTypechecker(), node);

        String abstractVariables = abstractVariableGenerator.generateAllExpression();

        TemplateHandler.add(getCurrentGroup(), "abstract_variables", abstractVariables);
    }

    @Override
    public void caseAConcreteVariablesMachineClause(AConcreteVariablesMachineClause node)
    {
        inAConcreteVariablesMachineClause(node);
        {
            List<PExpression> copy = new ArrayList<PExpression>(node.getIdentifiers());
            for(PExpression e : copy)
            {
                e.apply(this);
            }
        }
        outAConcreteVariablesMachineClause(node);
    }

    @Override
    public void caseAAbstractConstantsMachineClause(AAbstractConstantsMachineClause node)
    {
        inAAbstractConstantsMachineClause(node);
        {
            List<PExpression> copy = new ArrayList<PExpression>(node.getIdentifiers());
            for(PExpression e : copy)
            {
                e.apply(this);
            }
        }
        outAAbstractConstantsMachineClause(node);
    }

    @Override
    public void caseAConstantsMachineClause(AConstantsMachineClause node)
    {
        inAConstantsMachineClause(node);
        {
            List<PExpression> copy = new ArrayList<PExpression>(node.getIdentifiers());
            for(PExpression e : copy)
            {
                e.apply(this);
            }
        }
        outAConstantsMachineClause(node);
    }

    @Override
    public void caseAPropertiesMachineClause(APropertiesMachineClause node)
    {
        inAPropertiesMachineClause(node);
        if(node.getPredicates() != null)
        {
            node.getPredicates().apply(this);
        }
        outAPropertiesMachineClause(node);
    }

    @Override
    public void caseAConstraintsMachineClause(AConstraintsMachineClause node)
    {
        inAConstraintsMachineClause(node);
        if(node.getPredicates() != null)
        {
            node.getPredicates().apply(this);
        }
        outAConstraintsMachineClause(node);
    }

    @Override
    public void caseAInitialisationMachineClause(AInitialisationMachineClause node)
    {
        inAInitialisationMachineClause(node);
        if(node.getSubstitutions() != null)
        {
            node.getSubstitutions().apply(this);
        }
        outAInitialisationMachineClause(node);
    }

    @Override
    public void caseAInvariantMachineClause(AInvariantMachineClause node)
    {
        inAInvariantMachineClause(node);
        if(node.getPredicates() != null)
        {
            node.getPredicates().apply(this);
        }
        outAInvariantMachineClause(node);
    }

    @Override
    public void caseAAssertionsMachineClause(AAssertionsMachineClause node)
    {
        inAAssertionsMachineClause(node);
        {
            List<PPredicate> copy = new ArrayList<PPredicate>(node.getPredicates());
            for(PPredicate e : copy)
            {
                e.apply(this);
            }
        }
        outAAssertionsMachineClause(node);
    }

    @Override
    public void caseAValuesMachineClause(AValuesMachineClause node)
    {
        inAValuesMachineClause(node);
        {
            List<PValuesEntry> copy = new ArrayList<PValuesEntry>(node.getEntries());
            for(PValuesEntry e : copy)
            {
                e.apply(this);
            }
        }
        outAValuesMachineClause(node);
    }

    @Override
    public void caseALocalOperationsMachineClause(ALocalOperationsMachineClause node)
    {
        inALocalOperationsMachineClause(node);
        {
            List<POperation> copy = new ArrayList<POperation>(node.getOperations());
            for(POperation e : copy)
            {
                e.apply(this);
            }
        }
        outALocalOperationsMachineClause(node);
    }

    @Override
    public void caseAOperationsMachineClause(AOperationsMachineClause node)
    {
        inAOperationsMachineClause(node);
        {
            List<POperation> copy = new ArrayList<POperation>(node.getOperations());
            for(POperation e : copy)
            {
                e.apply(this);
            }
        }
        outAOperationsMachineClause(node);
    }

    @Override
    public void caseAReferencesMachineClause(AReferencesMachineClause node)
    {
        inAReferencesMachineClause(node);
        {
            List<PMachineReference> copy = new ArrayList<PMachineReference>(node.getMachineReferences());
            for(PMachineReference e : copy)
            {
                e.apply(this);
            }
        }
        outAReferencesMachineClause(node);
    }

    @Override
    public void caseAInvalidOperationsClauseMachineClause(AInvalidOperationsClauseMachineClause node)
    {
        inAInvalidOperationsClauseMachineClause(node);
        if(node.getSemicolon() != null)
        {
            node.getSemicolon().apply(this);
        }
        outAInvalidOperationsClauseMachineClause(node);
    }

    @Override
    public void caseAExpressionsMachineClause(AExpressionsMachineClause node)
    {
        inAExpressionsMachineClause(node);
        {
            List<PExpressionDefinition> copy = new ArrayList<PExpressionDefinition>(node.getExpressions());
            for(PExpressionDefinition e : copy)
            {
                e.apply(this);
            }
        }
        outAExpressionsMachineClause(node);
    }

    @Override
    public void caseAPredicatesMachineClause(APredicatesMachineClause node)
    {
        inAPredicatesMachineClause(node);
        {
            List<PPredicateDefinition> copy = new ArrayList<PPredicateDefinition>(node.getPredicates());
            for(PPredicateDefinition e : copy)
            {
                e.apply(this);
            }
        }
        outAPredicatesMachineClause(node);
    }

}
