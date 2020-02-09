package de.hhu.stups.bxmlgenerator;

import de.hhu.stups.codegenerator.generators.CodeGenerationException;
import de.hhu.stups.bxmlgenerator.generators.MachineGenerator;
import de.prob.parser.antlr.Antlr4BParser;
import de.prob.parser.antlr.BProject;
import de.prob.parser.antlr.ScopeException;
import de.prob.parser.ast.nodes.MachineNode;
import de.prob.parser.ast.visitors.TypeErrorException;

import java.io.File;
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
	 * [1] Destination (optional) e.g ~/Desktop/
	 */
	public static void main(String[] args) throws CodeGenerationException {
		if(args.length < 1) {
			System.err.println("Wrong number of arguments: " + args.length);
			return;
		}

		Path path = Paths.get(args[0]);
		checkPath(path);

		Path destination;
		if(args.length == 2) {
			destination = Paths.get(args[1]);
			checkPath(destination);
		}else {
			destination = path;
		}

		BProject project = parseProject(path);
		writeToFile(path, destination, project.getMainMachine());
	}

	private static void checkPath(Path path) {
		if(path == null) {
			throw new RuntimeException("File not found");
		}
	}

	public Path generate(Path target, Path destination){
		BProject project = parseProject(target);
		return writeToFile(target, destination, project.getMainMachine());
	}

	public Path generate(Path target){
		BProject project = parseProject(target);
		return writeToFile(target, Paths.get(target.toString().substring(0, target.toString().lastIndexOf(File.separator)+1)), project.getMainMachine());
	}


	private static Path writeToFile(Path target, Path destination,  MachineNode node) {

		Path newPath;

		if(target.toString().equals(destination.toString()))
		{
			int lastIndexSlash = target.toString().lastIndexOf(File.separator);

			newPath = Paths.get(target.toString().substring(0, lastIndexSlash + 1)  + node.getName() + ".bxml");
		}
		else{
			newPath = Paths.get(destination + File.separator + node.getName() + ".bxml");
		}

		MachineGenerator generator = new MachineGenerator();

		String code = generator.generateMachine(node);

		try {
			return Files.write(newPath, code.getBytes(), Files.exists(newPath) ? TRUNCATE_EXISTING : CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	private static BProject parseProject(Path path) throws CodeGenerationException {
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
