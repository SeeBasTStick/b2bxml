package de.hhu.stups.codegenerator;

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
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BXMLTestUtils {

    public static void calculateDifference(String name) throws Exception {
        Path result = buildBXML(name).get(0);

        URI original = URI.create(System.getProperty("user.dir") + "/src/test/resources/bxml/" + name + ".bxml");

        File xmlFile1 = new File(original.toString());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc1 = db.parse(xmlFile1);

        File xmlFile2 = new File(result.toString());
        DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
        DocumentBuilder db2 = dbf2.newDocumentBuilder();
        Document doc2 = db2.parse(xmlFile2);

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

    private static List<Path> buildBXML(String machine) throws Exception {
        Path mchPath = Paths.get(CodeGenerator.class.getClassLoader()
                .getResource("de/hhu/stups/codegenerator/" + machine + ".mch").toURI());
        System.out.println(mchPath.toString());
        CodeGenerator codeGenerator = new CodeGenerator();
        return codeGenerator.generate(mchPath, GeneratorMode.BXML, false,
                String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), "10",
                true, null, false);
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


