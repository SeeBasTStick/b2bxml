package de.hhu.stups.bxmlgenerator.generators;


import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;


public class MachineGenerator {

	private STGroup currentGroup;

	private TypeInfoGenerator typeInfoGenerator;

	private AbstractVariablesGenerator abstractVariablesGenerator;

	private InvariantGenerator invariantGenerator;

	private InitialisationGenerator initialisationGenerator;

	private OperationsGenerator operationsGenerator;


	public MachineGenerator() {
		this.currentGroup = new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg");
		HashMap<Integer, BType> nodeType = new HashMap<>();
		this.abstractVariablesGenerator = new AbstractVariablesGenerator(nodeType,  currentGroup);
		this.invariantGenerator = new InvariantGenerator(nodeType,  currentGroup);
		this.initialisationGenerator = new InitialisationGenerator(nodeType,  currentGroup);
		this.operationsGenerator = new OperationsGenerator(nodeType,  currentGroup);
		this.typeInfoGenerator = new TypeInfoGenerator(nodeType,  currentGroup);
	}


	public String generateMachine(MachineNode node) {
		ST machine = currentGroup.getInstanceOf("machine");
		TemplateHandler.add(machine, "machine", node.getName());
		generateBody(node, machine);
		return machine.render();
	}


	private void generateBody(MachineNode node, ST machine) {
		TemplateHandler.add(machine, "abstract_variables", abstractVariablesGenerator.generateAbstractVariables(node.getVariables()));
		TemplateHandler.add(machine, "invariant", invariantGenerator.generateInvariants(node.getInvariant()));
		TemplateHandler.add(machine, "initialisation", initialisationGenerator.generateInitialisation(node.getInitialisation()));
		TemplateHandler.add(machine, "operations", operationsGenerator.generateOperations(node.getOperations()));
		TemplateHandler.add(machine, "type_info", typeInfoGenerator.generateTypeInfo());
	}

}
