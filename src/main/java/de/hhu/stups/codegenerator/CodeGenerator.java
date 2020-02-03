package de.hhu.stups.codegenerator;

import de.hhu.stups.codegenerator.generators.CodeGenerationException;
import de.hhu.stups.codegenerator.generators.MachineGenerator;
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

	public static void main(String[] args) throws CodeGenerationException {
		if(args.length < 1) {
			System.err.println("Wrong number of arguments");
			return;
		}
		CodeGenerator codeGenerator = new CodeGenerator();
		Path path = Paths.get(args[0]);
		checkPath(path);
		codeGenerator.generate(path);
	}

	private static void checkPath(Path path) {
		if(path == null) {
			throw new RuntimeException("File not found");
		}
	}

	public Path generate(Path path) throws CodeGenerationException {
		BProject project = parseProject(path);
		return  writeToFile(path, project.getMainMachine());
	}

	private Path writeToFile(Path path,  MachineNode node) {
		MachineGenerator generator = new MachineGenerator();

		String code = generator.generateMachine(node);

		int lastIndexSlash = path.toString().lastIndexOf("/");

		Path newPath = Paths.get(path.toString().substring(0, lastIndexSlash + 1) + node.getName() + ".bxml");

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
