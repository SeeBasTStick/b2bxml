package de.hhu.stups.codegenerator.generators;


import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;

/*
* The code generator is implemented by using the visitor pattern
*/

public class MachineGenerator {


	private NameHandler nameHandler;

	private STGroup currentGroup;

	private TypeInfoGenerator typeInfoGenerator;

	private AbstractVariablesGenerator abstractVariablesGenerator;

	private InvariantGenerator invariantGenerator;

	private InitialisationGenerator initialisationGenerator;

	private OperationsGenerator operationsGenerator;


	public MachineGenerator() {
		this.currentGroup = new STGroupFile("de/hhu/stups/codegenerator/BXMLTemplate.stg");
		this.nameHandler = new NameHandler(currentGroup);
		HashMap<Integer, BType> nodeType = new HashMap<>();
		this.abstractVariablesGenerator = new AbstractVariablesGenerator(nodeType, nameHandler, currentGroup);
		this.invariantGenerator = new InvariantGenerator(nodeType, nameHandler, currentGroup);
		this.initialisationGenerator = new InitialisationGenerator(nodeType, nameHandler, currentGroup);
		this.operationsGenerator = new OperationsGenerator(nodeType, nameHandler, currentGroup);
		this.typeInfoGenerator = new TypeInfoGenerator(nodeType, nameHandler, currentGroup);
	}

	/*
	 * This function generates code for the whole machine with the given AST node.
	 */
	public String generateMachine(MachineNode node) {
		ST machine = currentGroup.getInstanceOf("machine");
		TemplateHandler.add(machine, "machine", nameHandler.handle(node.getName()));
		generateBody(node, machine);
		return machine.render();
	}


	/*types
	 * This function generates the whole body of a machine from the given AST node for the machine.
	 */
	private void generateBody(MachineNode node, ST machine) {
		TemplateHandler.add(machine, "abstract_variables", abstractVariablesGenerator.generateAbstractVariables(node.getVariables()));
		TemplateHandler.add(machine, "invariant", invariantGenerator.generateInvariants(node.getInvariant()));
		TemplateHandler.add(machine, "initialisation", initialisationGenerator.generateInitialisation(node.getInitialisation()));
		TemplateHandler.add(machine, "operations", operationsGenerator.generateOperations(node.getOperations()));
		TemplateHandler.add(machine, "type_info", typeInfoGenerator.generateTypeInfo());
	}

}
