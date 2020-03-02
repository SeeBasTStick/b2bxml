package de.hhu.stups.bxmlgenerator.antlr;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.SetType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Map;
import java.util.stream.Collectors;

public class TypeInfoGenerator extends BXMLBodyGenerator{


    public TypeInfoGenerator(Map<Integer, BType> nodeType,  STGroup currentGroup) {
        super(nodeType,  currentGroup);
    }


    public String generateTypeInfo(){
        ST typeInfo = getSTGroup().getInstanceOf("type_info");
        TemplateHandler.add(typeInfo, "types", getNodeTyp().keySet().stream()
                .map(this::generateTyp)
                .collect(Collectors.toList()));
        return typeInfo.render();
    }

    public String generateTyp(Integer value)
    {
        ST type = getSTGroup().getInstanceOf("type");
        TemplateHandler.add(type, "id", value);
        TemplateHandler.add(type, "body", generateTypeInformation(getNodeTyp().get(value)));
        return type.render();
    }

    public String generateTypeInformation(BType type)
    {
        String result;

        switch (type.getClass().getSimpleName())
        {
            case "IntegerType":
                ST integerTypeGroup = getSTGroup().getInstanceOf("id_blank");
                TemplateHandler.add(integerTypeGroup, "val", type.toString());
                result = integerTypeGroup.render();
                break;
            case "BoolType":
                ST boolTypeGroup = getSTGroup().getInstanceOf("id_blank");
                TemplateHandler.add(boolTypeGroup, "val", type.toString());
                result = boolTypeGroup.render();
                break;
            case "SetType":
                SetType setType = (SetType) type;
                ST setTypeGroup = getSTGroup().getInstanceOf("unary_exp");
                TemplateHandler.add(setTypeGroup, "op", "POW");
                TemplateHandler.add(setTypeGroup, "body", generateTypeInformation(setType.getSubType()));
                result = setTypeGroup.render();
                break;
            default:
                result = "";
                try {
                    throw new Exception(type.getClass().getSimpleName() + " is not implemented yet");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        return result;
    }


}
