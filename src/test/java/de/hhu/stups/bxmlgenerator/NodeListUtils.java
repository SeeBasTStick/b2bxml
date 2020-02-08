package de.hhu.stups.bxmlgenerator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class NodeListUtils{

    public static boolean compareNodeList(NodeList target, NodeList test){
        List<Node> controlNodes = unpackNodeList(target);

        List<Node> testNodes = unpackNodeList(test);

        if (controlNodes.size() != testNodes.size()) {
            return false;
        }

        for (int i = 0; i < testNodes.size(); i++) {
            if (!testNodes.get(i).getNodeName().equals(controlNodes.get(i).getNodeName())) {
                return false;
            }
        }

        return true;
    }

    public static List<Node> unpackNodeList(NodeList list)
    {
        List<Node> result = new ArrayList<>();

        for (int i = 0; i < list.getLength(); i++) {
            result.add(list.item(i));
        }

        return result;
    }
}