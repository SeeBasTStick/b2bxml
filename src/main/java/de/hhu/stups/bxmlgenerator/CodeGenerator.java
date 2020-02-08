package de.hhu.stups.bxmlgenerator;

import de.hhu.stups.codegenerator.generators.CodeGenerationException;
import de.hhu.stups.bxmlgenerator.generators.MachineGenerator;
import de.prob.parser.antlr.Antlr4BParser;
import de.prob.parser.antlr.BProject;
import de.prob.parser.antlr.ScopeException;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.visitors.TypeErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class CodeGenerator {

	private List<Path> paths = new ArrayList<>();

	/*
	 * [0] Target
	 * [1] Destination (optional)
	 */
	public static void main(String[] args) throws CodeGenerationException {
		if(args.length < 1) {
			System.err.println("Wrong number of arguments: " + args.length);
			return;
		}

		CodeGenerator codeGenerator = new CodeGenerator();
		Path path = Paths.get(args[0]);
		checkPath(path);

		Path destination;
		if(args.length == 2) {
			destination = Paths.get(args[1]);
			checkPath(destination);
		}else {
			destination = path;
		}

		codeGenerator.generate(path, destination);
	}

	private static void checkPath(Path path) {
		if(path == null) {
			throw new RuntimeException("File not found");
		}
	}

	public Path generate(Path path, Path destination) throws CodeGenerationException {
		BProject project = parseProject(path);
		return  writeToFile(destination, project.getMainMachine());
	}

	private Path writeToFile(Path path,  MachineNode node) {
		MachineGenerator generator = new MachineGenerator();

		String code = generator.generateMachine(node);

		int lastIndexSlash = path.toString().lastIndexOf("/");

		Path newPath = Paths.get(path.toString().substring(0, lastIndexSlash + 1) + node.getName() + ".bxml");

		System.out.println(newPath);

		try {
			return Files.write(newPath, code.getBytes(), Files.exists(newPath) ? TRUNCATE_EXISTING : CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private BProject parseProject(Path path) throws CodeGenerationException {
		BProject project;
		try {
			project = Antlr4BParser.createBProjectFromMainMachineFile(path.toFile());
		} catch (TypeErrorException | ScopeException | IOException e) {
			e.printStackTrace();
			throw new CodeGenerationException(e.getMessage());
		}
		return project;
	}

	public List<Path> getPaths() {
		return paths;
	}

}
