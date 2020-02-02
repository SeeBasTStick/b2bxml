package de.hhu.stups.codegenerator;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.*;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;
import org.xmlunit.util.Linqy;
import org.xmlunit.util.Nodes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TestBXML {

    public List<Path> buildBXML(String machine) throws Exception {
        Path mchPath = Paths.get(CodeGenerator.class.getClassLoader()
                .getResource("de/hhu/stups/codegenerator/" + machine + ".mch").toURI());
        System.out.println(mchPath.toString());
        CodeGenerator codeGenerator = new CodeGenerator();
        return codeGenerator.generate(mchPath, GeneratorMode.BXML, false,
                String.valueOf(Integer.MIN_VALUE), String.valueOf(Integer.MAX_VALUE), "10",
                true, null, false);
    }

    @Test
    public void testExamples() throws Exception {
        // /home/sebastian/IdeaProjects/b2program/build/resources/test/Mega.bxml
        // /home/sebastian/IdeaProjects/b2program/src/test/resources/Mega.bxml
        String name = "Mega";
      //
          Path result = buildBXML(name).get(0);

        URI original = URI.create(System.getProperty("user.dir")+"/src/test/resources/bxml/" + name + ".bxml");

      //  URI result = URI.create("/home/sebastian/IdeaProjects/b2program/build/resources/test/de/hhu/stups/codegenerator/Mega.bxml");
        System.out.println(original);


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
                .withDifferenceEvaluator(new IgnoreAttributeDifferenceEvaluator(List.of("typref", "id")))
           //     .withDifferenceEvaluator(DifferenceEvaluators.downgradeDifferencesToSimilar(ComparisonType.TEXT_VALUE))
                .ignoreWhitespace()
                .ignoreElementContentWhitespace()
                .ignoreComments()
                .checkForSimilar()
                .build();




        Assert.assertFalse(myDiff.toString(), myDiff.hasDifferences());


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

    public static final class IgnoreAttributeDifferenceEvaluator implements DifferenceEvaluator {



        private List<String> attributeNames;

        public IgnoreAttributeDifferenceEvaluator(List<String> attributeNames) {
            this.attributeNames = attributeNames;


        }

        @Override
        public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {

            if (outcome == ComparisonResult.EQUAL)
            {
                return outcome; // only evaluate differences.
            }

            if(outcome == ComparisonResult.DIFFERENT && comparison.getType() == ComparisonType.CHILD_NODELIST_SEQUENCE)
            {

                NodeList controlElementList = comparison.getControlDetails().getTarget().getChildNodes();

                NodeList testElementList = comparison.getTestDetails().getTarget().getChildNodes();

                List<Node> control = new ArrayList<>();

                List<Node> test = new ArrayList<>();

                for(int i = 0; i < controlElementList.getLength(); i++)
                {
                    control.add(controlElementList.item(i));
                }

                for(int i = 0; i < testElementList.getLength(); i++)
                {
                    test.add(testElementList.item(i));
                }


                for(int i = 0; i < test.size(); i++)
                {
                    if(!test.get(i).getNodeName().equals(control.get(i).getNodeName()))
                    {
                        System.out.println("false " + test.toString() + " " + control.toString());
                        return ComparisonResult.DIFFERENT;
                    }
                }

                return ComparisonResult.SIMILAR;
            }

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



    static class SameTypeSelector implements ElementSelector{

        @Override
        public boolean canBeCompared(Element controlElement, Element testElement) {


            NodeList controlElementList = controlElement.getChildNodes();

            NodeList testElementList = testElement.getChildNodes();

            List<Node> control = new ArrayList<>();

            List<Node> test = new ArrayList<>();

            for(int i = 0; i < controlElementList.getLength(); i++)
            {
                control.add(controlElementList.item(i));
            }

            for(int i = 0; i < testElementList.getLength(); i++)
            {
                test.add(testElementList.item(i));
            }

            if(control.size() != test.size())
            {
                System.out.println("false " + test.toString() + " " + control.toString());

                return false;
            }

            for(int i = 0; i < test.size(); i++)
            {
                if(!test.get(i).getNodeName().equals(control.get(i).getNodeName()))
                {
                    System.out.println("false " + test.toString() + " " + control.toString());
                    return false;
                }
            }
            System.out.println("true: " + test.toString() + " " + control.toString());
            return true;
        }
    }


}
