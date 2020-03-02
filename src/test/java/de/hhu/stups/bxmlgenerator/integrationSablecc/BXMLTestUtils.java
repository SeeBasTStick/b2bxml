package de.hhu.stups.bxmlgenerator.integrationSablecc;

import de.be4.classicalb.core.parser.exceptions.BCompoundException;
import de.hhu.stups.bxmlgenerator.CodeGenerator;
import de.hhu.stups.bxmlgenerator.CodeGenerator2;
import org.junit.Assert;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class BXMLTestUtils {

    static String projectLocation = System.getProperty("user.dir");

    public static void calculateDifference(String path) throws Exception {

        String name;
        //Dealing with nested structures
        if(path.contains(File.separator)) {
            name = path.substring(path.lastIndexOf(File.separator)+1, path.lastIndexOf("."));
        }else{
            name = path.substring(0, path.lastIndexOf("."));
        }

        Path originalPath = generateOriginal(path, name);

        Path imagePath = generateImage(path);

        File originalFile = new File(originalPath.toString());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc1 = db.parse(originalFile);

        File imageFile = new File(imagePath.toString());
        DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
        DocumentBuilder db2 = dbf2.newDocumentBuilder();
        Document doc2 = db2.parse(imageFile);

        ElementSelector es = ElementSelectors.conditionalBuilder()
                .whenElementIsNamed("Type")
                .thenUse(new SameTypeSelector())
                .elseUse(ElementSelectors.byNameAndText)
                .build();


        Diff myDiff = DiffBuilder.compare(toXmlString(doc1)).withTest(toXmlString(doc2))
                .withNodeMatcher(new DefaultNodeMatcher(es))
                .withDifferenceEvaluator(new IgnoreAttributeDifferenceAndChildOrderEvaluator(List.of("typref", "id")))
                .ignoreWhitespace()
                .ignoreElementContentWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();
        Assert.assertFalse(myDiff.toString(), myDiff.hasDifferences());

    }


    private static Path generateOriginal(String pathName, String machineName) throws IOException{


        Path cuuc = Paths.get(":" ,projectLocation , "/src/test/resources/de/hhu/stups/generators/");

        Path mchPath = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/machine/"
                + pathName );

        Path bxml = Paths.get(projectLocation, "/src/test/resources/de/hhu/stups/generators/bxml");

        Path bxmlTarget;

        Path furtherResources;

        //Meaning : If we deal with a file down in the folder tree -> modify string to fit to the deeper location
        if(!pathName.contains(File.separator))
        {
            furtherResources =
                    Paths.get(projectLocation, "/src/test/resources/de/hhu/stups/machine/",
                            pathName.substring(0, pathName.lastIndexOf(File.separator)+1));
            bxmlTarget = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/results/sablecc/original/");

        }else{
            String treeBranchName = pathName.substring(0, pathName.lastIndexOf(File.separator));
            furtherResources = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/machine/",
                    treeBranchName);
            bxmlTarget = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/results/sablecc/original/",
                    treeBranchName);
        }

        File dir = new File(bxmlTarget.toString());
        if(!dir.exists()){
            boolean result = dir.mkdirs();
            if(result){
                throw new FileSystemException("Folder could not be created while generating original, something bad happend");
            }
        }



        /*
         * calls the standalone bxml generator contained in atelierB to create an "original" file
         * https://www.atelierb.eu/en/download/
         * Arguments of Process Builder are:
         *  <application> <enable semantic analysis> <indent of 4> <further resources> <destination for result> <target>
         *    ./bxml        -a                          -i 4          -I /machine          -o ../result       ../bla.mch
         */


        ProcessBuilder pb = new ProcessBuilder(bxml.toString(),  "-a", "-i", "4",
                "-I", furtherResources.toString(),
                "-O", bxmlTarget.toString(),
                mchPath.toString());


        //Sets needed library path
        Map<String, String> env = pb.environment();
        env.put("LD_LIBRARY_PATH", cuuc.toString());

        pb.start();

        return Paths.get(bxmlTarget.toString(), machineName + ".bxml");
    }

    private static Path generateImage(String pathName) throws IOException, BCompoundException {

        Path mchPath = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/machine/" + pathName );

        Path target;

        if(!pathName.contains(File.separator))
        {
            target = Paths.get(projectLocation , "/src/test/resources/de/hhu/stups/results/sablecc/image/");
        }else{
            target =  Paths.get(projectLocation, "/src/test/resources/de/hhu/stups/results/sablecc/image",
                    pathName.substring(0, pathName.lastIndexOf(File.separator)));
        }

        File dir = new File(target.toString());
        if(!dir.exists()){
            boolean result = dir.mkdirs();

            if(result){
                throw new FileSystemException("Folder could not be created while generating image, something bad happend");
            }
        }

        CodeGenerator2 codeGenerator = new CodeGenerator2();

        return codeGenerator.generate(mchPath, target);
    }

    private static String toXmlString(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StringWriter strWriter = new StringWriter();
        StreamResult result = new StreamResult(strWriter);

        transformer.transform(source, result);

        return strWriter.getBuffer().toString();

    }

    protected static final class IgnoreAttributeDifferenceAndChildOrderEvaluator implements DifferenceEvaluator {


        private List<String> attributeNames;

        public IgnoreAttributeDifferenceAndChildOrderEvaluator(List<String> attributeNames) {
            this.attributeNames = attributeNames;
        }

        @Override
        public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {

            // Nodes are equal
            if (outcome == ComparisonResult.EQUAL) {
                return outcome;
            }

            //Nodes differ in order of their children
            if (outcome == ComparisonResult.DIFFERENT && comparison.getType() == ComparisonType.CHILD_NODELIST_SEQUENCE)
            {
                if(NodeListUtils.compareNodeList(comparison.getControlDetails().getTarget().getChildNodes(),
                        comparison.getTestDetails().getTarget().getChildNodes()))
                {
                    return ComparisonResult.SIMILAR;
                }
                else{
                    return ComparisonResult.DIFFERENT;
                }

            }

            //Nodes only differ in their attributes
            final Node controlNode = comparison.getControlDetails().getTarget();
            if (controlNode instanceof Attr) {

                Attr attr = (Attr) controlNode;
                if (attributeNames.contains(attr.getName())) {
                    return ComparisonResult.SIMILAR; // will evaluate this difference as similar
                }

            }
            return outcome;
        }
    }


    protected  static class SameTypeSelector implements ElementSelector  {
        @Override
        public boolean canBeCompared(Element controlElement, Element testElement) {
            return NodeListUtils.compareNodeList(controlElement.getChildNodes(), testElement.getChildNodes());
        }
    }
}


