package de.hhu.stups.bxmlgenerator.generators;

import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.typechecker.btypes.BType;
import de.prob.typechecker.btypes.SetType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TypeInfoGenerator  {


    private STGroupFile currentGroup;
    private HashMap<Integer, BType> nodeType;

    public TypeInfoGenerator(STGroupFile currentGroup, HashMap<Integer, BType> nodeType){
        this.currentGroup = currentGroup;
        this.nodeType = nodeType;
    }

    public String generateTypeInfo(){
        ST typeInfo = currentGroup.getInstanceOf("type_info");
        TemplateHandler.add(typeInfo, "types", nodeType.keySet().stream()
                .map(this::generateTyp)
                .collect(Collectors.toList()));
        return typeInfo.render();
    }

    public String generateTyp(Integer value)
    {
        ST type = currentGroup.getInstanceOf("type");
        TemplateHandler.add(type, "id", value);
        TemplateHandler.add(type, "body", generateTypeInformation(nodeType.get(value)));
        return type.render();
    }

    public String generateTypeInformation(BType type)
    {
        String result;
        switch (type.getClass().getSimpleName())
        {
            case "IntegerType":
                ST integerTypeGroup = currentGroup.getInstanceOf("id_blank");
                TemplateHandler.add(integerTypeGroup, "val", type.toString());
                result = integerTypeGroup.render();
                break;
            case "BoolType":
                ST boolTypeGroup = currentGroup.getInstanceOf("id_blank");
                TemplateHandler.add(boolTypeGroup, "val", type.toString());
                result = boolTypeGroup.render();
                break;
            case "SetType":
                SetType setType = (SetType) type;
                ST setTypeGroup = currentGroup.getInstanceOf("unary_exp");
                TemplateHandler.add(setTypeGroup, "op", "POW");
                TemplateHandler.add(setTypeGroup, "body", generateTypeInformation(setType.getSubtype()));
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
