package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.Node;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BXMLBodyGenerator {

    private Map<Integer, BType> nodeType;

    private NameHandler nameHandler;

    private STGroup currentGroup;

    public BXMLBodyGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup)
    {
        this.nodeType = nodeType;
        this.currentGroup = currentGroup;
        this.nameHandler = nameHandler;
    }

    public Map<Integer, BType> getNodeTyp(){
        return nodeType;
    }

    public NameHandler getNameHandler(){
        return nameHandler;
    }

    public STGroup getSTGroup(){ return currentGroup; }


}
