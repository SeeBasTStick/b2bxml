package de.hhu.stups.codegenerator;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestBXML {

    public void testC(String machine) throws Exception {
        Path mchPath = Paths.get(CodeGenerator.class.getClassLoader()
                .getResource("de/hhu/stups/codegenerator/" + machine + ".mch").toURI());
        CodeGenerator codeGenerator = new CodeGenerator();
        List<Path> bxmlFilePaths = codeGenerator.generate(mchPath, GeneratorMode.BXML, false,
                String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), "10",
                true, null, false);

        System.out.println(bxmlFilePaths);
        //cFilePaths.forEach(path -> cleanUp(path.toString()));


    }

    @Test
    public void testExample() throws Exception {
       // testC("Mega");
        System.out.println("---------------------"
        );
        testC("foo");
    }
}
