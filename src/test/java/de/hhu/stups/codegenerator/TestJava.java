package de.hhu.stups.codegenerator;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by fabian on 31.05.18.
 */

public class TestJava {

    public static void writeInputToSystem(InputStream inputStream) throws IOException {
        writeInputToOutput(inputStream, System.err);
    }

    public static void writeInputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException {
        int size;
        byte[] buffer = new byte[1024];
        while ((size = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, size);
    }

    public void testJava(String machine) throws Exception {
        Path mchPath = Paths.get(CodeGenerator.class.getClassLoader().getResource("de/hhu/stups/codegenerator/" + machine + ".mch").toURI());
        CodeGenerator.generate(mchPath, GeneratorMode.JAVA);

        Process process = Runtime.getRuntime().exec("javac build/resources/test/de/hhu/stups/codegenerator/" + machine + ".java " + "-cp btypes.jar");

        writeInputToSystem(process.getErrorStream());
        writeInputToOutput(process.getErrorStream(), process.getOutputStream());
        process.waitFor();

        Path javaPath = Paths.get(CodeGenerator.class.getClassLoader().getResource("de/hhu/stups/codegenerator/" + machine + ".java").toURI());
        Path classPath = Paths.get(CodeGenerator.class.getClassLoader().getResource("de/hhu/stups/codegenerator/" + machine + ".class").toURI());
        cleanUp(javaPath.toString());
        cleanUp(classPath.toString());
    }

    @Test
    public void testAbstractMachine() throws Exception {
        testJava("AbstractMachine");
    }

    @Ignore
    @Test
    public void testAbstractMachine2() throws Exception {
        //TODO
        testJava("AbstractMachine2");
    }

    @Test
    public void testAbstractMachine3() throws Exception {
        testJava("AbstractMachine3");
    }

    @Test
    public void testAbstractMachine4() throws Exception {
        testJava("AbstractMachine4");
    }

    @Ignore
    @Test
    public void testAbstractMachine5() throws Exception {
        //TODO
        testJava("AbstractMachine5");
    }

    @Test
    public void testAbstractMachine6() throws Exception {
        testJava("AbstractMachine6");
    }

    @Test
    public void testAbstractMachine10() throws Exception {
        testJava("AbstractMachine10");
    }


    @Ignore
    @Test
    public void testAbstractMachine11() throws Exception {
        testJava("AbstractMachine11");
    }

    @Ignore
    @Test
    public void testAbstractMachine14() throws Exception {
        testJava("AbstractMachine14");
    }

    @Test
    public void testCounter() throws Exception {
        testJava("Counter");
    }

    @Test
    public void testBakery0() throws Exception {
        testJava("Bakery0");
    }

    @Ignore
    @Test
    public void testGCD() throws Exception {
        //TODO
    	testJava("GCD");
    }

    @Test
    public void testACounter() throws Exception {
        testJava("ACounter");
    }

    @Test
    public void testLift() throws Exception {
        testJava("Lift");
    }

    @Ignore
    @Test
    public void testPhonebook() throws Exception {
        //TODO
        testJava("phonebook");
    }

    @Ignore
    @Test
    public void testPhonebook6() throws Exception {
        //TODO
        testJava("phonebook6");
    }

    @Test
    public void testProject() throws Exception {
        testJava("project1/A");
    }

    
    private void cleanUp(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


}