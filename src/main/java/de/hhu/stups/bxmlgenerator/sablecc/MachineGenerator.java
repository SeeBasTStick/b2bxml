package de.hhu.stups.bxmlgenerator.sablecc;

import de.be4.classicalb.core.parser.node.*;
import de.hhu.stups.bxmlgenerator.util.MachineClauseFinder;
import de.hhu.stups.bxmlgenerator.util.Pair;
import de.hhu.stups.bxmlgenerator.util.SubstitutionFinder;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.Typechecker;
import de.prob.typechecker.btypes.BType;
import org.stringtemplate.v4.ST;
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

    /**
     * @return a List with the parts of the \<machine> </machine> clause e.g. invariants, initialisation...
     */
    public List<String> generateSubExpression(){
        Start node = (Start) getStartNode();
        AAbstractMachineParseUnit abstractMachineParseUnit = (AAbstractMachineParseUnit) node.getPParseUnit();

        List<String> result = new ArrayList<>();

        for(PMachineClause subElement : abstractMachineParseUnit.getMachineClauses()){
            if(subElement instanceof AVariablesMachineClause){
                caseAVariablesMachineClause((AVariablesMachineClause) subElement);
                result.add(getCurrentGroup().getAttribute("abstract_variables").toString());
            }
            if(subElement instanceof AInvariantMachineClause){
                caseAInvariantMachineClause((AInvariantMachineClause) subElement);
                result.add(getCurrentGroup().getAttribute("invariant").toString());
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

        visitMachineClause(node.getMachineClauses());
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

        visitMachineClause(node.getMachineClauses());
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

        visitMachineClause(node.getMachineClauses());

    }

    public void visitMachineClause(List<PMachineClause> list){
        for (PMachineClause machineClause : list) {
            Pair<String, String> templateTarget = MachineClauseFinder.findMachineClause(machineClause);
            String machinePlaceHolder = templateTarget.getKey();
            String subTemplate = templateTarget.getValue();
            TemplateHandler.add(getCurrentGroup(), machinePlaceHolder,
                    new STGroupGenerator(
                            getStGroupFile(),
                            getStGroupFile().getInstanceOf(subTemplate),
                            getNodeType(),
                            getTypechecker(),
                            machineClause).generateCurrent());
        }
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






}
