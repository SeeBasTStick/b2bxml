package de.hhu.stups.codegenerator.generators;


import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.EnumeratedSetElementNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.LambdaNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.expression.QuantifiedExpressionNode;
import de.prob.parser.ast.nodes.expression.SetComprehensionNode;
import de.prob.parser.ast.nodes.predicate.CastPredicateExpressionNode;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.CoupleType;
import de.prob.parser.ast.types.SetType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.BOOL;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.CARD;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.CARTESIAN_PRODUCT;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.CLOSURE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.CLOSURE1;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.COMPOSITION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.COUPLE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.DIRECT_PRODUCT;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.DIVIDE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.DOMAIN;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.DOMAIN_RESTRICTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.DOMAIN_SUBTRACTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.FALSE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.FIN;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.FUNCTION_CALL;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.GENERALIZED_INTER;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.GENERALIZED_UNION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.ID;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.INTERSECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.INTERVAL;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.INVERSE_RELATION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.ITERATE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.MAX;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.MIN;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.MINUS;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.MOD;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.MULT;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.OVERWRITE_RELATION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PARALLEL_PRODUCT;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PARTIAL_BIJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PARTIAL_FUNCTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PARTIAL_INJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PARTIAL_SURJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.PLUS;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.POW;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.RANGE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.RANGE_RESTRICTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.RANGE_SUBTRACTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.RELATIONAL_IMAGE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.SET_ENUMERATION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.SET_SUBTRACTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_BIJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_FUNCTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_INJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_RELATION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_SURJECTION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TOTAL_SURJECTION_RELATION;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.TRUE;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.UNARY_MINUS;
import static de.prob.parser.ast.nodes.expression.ExpressionOperatorNode.ExpressionOperator.UNION;

public class ExpressionGenerator {

    /*
    * Hard-coded lists for identifying the type of the operators for expresion
    */
    private static final List<ExpressionOperatorNode.ExpressionOperator> BINARY_EXPRESSION_OPERATORS =
            Arrays.asList(PLUS,MINUS,MULT,DIVIDE,MOD,INTERSECTION, UNION, SET_SUBTRACTION, RELATIONAL_IMAGE,
                    FUNCTION_CALL, OVERWRITE_RELATION, DOMAIN_RESTRICTION, DOMAIN_SUBTRACTION,
                    RANGE_RESTRICTION, RANGE_SUBTRACTION, DIRECT_PRODUCT, PARALLEL_PRODUCT, COMPOSITION, CARTESIAN_PRODUCT, TOTAL_BIJECTION, TOTAL_FUNCTION, TOTAL_INJECTION,
                    TOTAL_RELATION, TOTAL_SURJECTION, TOTAL_SURJECTION_RELATION, PARTIAL_BIJECTION, PARTIAL_FUNCTION, PARTIAL_INJECTION,
                    PARTIAL_SURJECTION);

    private static final List<ExpressionOperatorNode.ExpressionOperator> UNARY_EXPRESSION_OPERATORS =
            Arrays.asList(UNARY_MINUS, CARD, DOMAIN, RANGE, ID, CLOSURE, CLOSURE1, ITERATE, INVERSE_RELATION, MIN, MAX, FIN, POW, GENERALIZED_UNION, GENERALIZED_INTER);

    private static final List<ExpressionOperatorNode.ExpressionOperator> EXPRESSION_BOOLEANS =
            Arrays.asList(TRUE,FALSE);

    private final STGroup currentGroup;

    private final MachineGenerator machineGenerator;

    private final NameHandler nameHandler;

    private final boolean useBigInteger;

    private final ImportGenerator importGenerator;

    private final DeclarationGenerator declarationGenerator;

    private final IdentifierGenerator identifierGenerator;

    private final TypeGenerator typeGenerator;

    private SubstitutionGenerator substitutionGenerator;

    private OperatorGenerator operatorGenerator;

    private final IterationConstructHandler iterationConstructHandler;

    public ExpressionGenerator(final STGroup currentGroup, final MachineGenerator machineGenerator, boolean useBigInteger, final NameHandler nameHandler,
                               final ImportGenerator importGenerator, final DeclarationGenerator declarationGenerator,
                               final IdentifierGenerator identifierGenerator, final TypeGenerator typeGenerator,
                               final IterationConstructHandler iterationConstructHandler) {
        this.currentGroup = currentGroup;
        this.machineGenerator = machineGenerator;
        this.useBigInteger = useBigInteger;
        this.nameHandler = nameHandler;
        this.importGenerator = importGenerator;
        this.declarationGenerator = declarationGenerator;
        this.identifierGenerator = identifierGenerator;
        this.typeGenerator = typeGenerator;
        this.iterationConstructHandler = iterationConstructHandler;
    }

    /*
    * This function generates code from an expression in B.
    */
    public String visitExprNode(ExprNode node) {
        importGenerator.addImport(node.getType());
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node);
        } else if (node instanceof ExpressionOperatorNode) {
            return visitExprOperatorNode((ExpressionOperatorNode) node);
        } else if (node instanceof EnumeratedSetElementNode) {
            EnumeratedSetElementNode element = (EnumeratedSetElementNode) node;
            return declarationGenerator.callEnum(element.getType().toString(), element.getDeclarationNode());
        } else if(node instanceof IdentifierExprNode) {
            Map<String, List<String>> enumTypes = nameHandler.getEnumTypes();
            if(enumTypes.keySet().contains(node.getType().toString()) &&
                    enumTypes.get(node.getType().toString()).contains(((IdentifierExprNode) node).getName())) {
                return declarationGenerator.callEnum(node.getType().toString(), ((IdentifierExprNode) node).getDeclarationNode());
            }
            return visitIdentifierExprNode((IdentifierExprNode) node);
        } else if(node instanceof CastPredicateExpressionNode) {
            return visitCastPredicateExpressionNode((CastPredicateExpressionNode) node);
        } else if(node instanceof QuantifiedExpressionNode) {
            return visitQuantifiedExpressionNode((QuantifiedExpressionNode) node);
        } else if(node instanceof SetComprehensionNode) {
            return visitSetComprehensionNode((SetComprehensionNode) node);
        } else if(node instanceof LambdaNode) {
            return visitLambdaNode((LambdaNode) node);
        }
        throw new RuntimeException("Given node is not implemented: " + node.getClass());
    }

    /*
    * This function generates code for an expression.
    */
    public String visitExprOperatorNode(ExpressionOperatorNode node) {
        List<String> expressionList = node.getExpressionNodes().stream()
                .map(this::visitExprNode)
                .collect(Collectors.toList());
        return generateExpression(node, expressionList);
    }

    /*
    * This function generates code for an identifier from the belonging AST node.
    */
    public String visitIdentifierExprNode(IdentifierExprNode node) {
        if(machineGenerator.isInIterationConstruct() && iterationConstructHandler.getCurrentIterationConstructGenerator().getBoundedVariables().contains(node.getName())) {
            return "_ic_" + node.getName();
        }
        if(substitutionGenerator.getCurrentLocalScope() > 0) {
            boolean isAssigned = identifierGenerator.isAssigned(node, node.getParent());
            return identifierGenerator.generateVarDeclaration(node.getName(), isAssigned);
        }
        return identifierGenerator.generate(node);
    }

    /*
    * This function generates code for numbers from the belonging AST node and the belonging template
    */
    public String visitNumberNode(NumberNode node) {
        ST number = currentGroup.getInstanceOf("number");
        TemplateHandler.add(number, "number", node.getValue().toString());
        TemplateHandler.add(number, "useBigInteger", useBigInteger);
        return number.render();
    }

    /*
    * This function generates code for cast predicates
    */
    public String visitCastPredicateExpressionNode(CastPredicateExpressionNode node) {
        return machineGenerator.visitPredicateNode(node.getPredicate(), null);
    }

    /*
    * This function generates code for an expression with the given AST node and the list of expressions within the expression.
    */
    public String generateExpression(ExpressionOperatorNode node, List<String> expressionList) {
        ExpressionOperatorNode.ExpressionOperator operator = node.getOperator();
        if(BINARY_EXPRESSION_OPERATORS.contains(operator)) {
            return operatorGenerator.generateBinary(() -> operator, expressionList);
        } else if(UNARY_EXPRESSION_OPERATORS.contains(operator)) {
            return generateUnaryExpression(operator, expressionList);
        } else if(EXPRESSION_BOOLEANS.contains(operator)) {
            return generateBoolean(operator);
        } else if(node.getOperator() == SET_ENUMERATION){
            return generateSetEnumeration(node.getType(), expressionList);
        } else if(node.getOperator() == INTERVAL) {
            return generateInterval(expressionList);
        } else if(node.getOperator() == COUPLE) {
            return generateCouple(expressionList, node.getExpressionNodes().get(0).getType(), node.getExpressionNodes().get(1).getType());
        } else if(node.getOperator() == BOOL) {
            return generateBooleans();
        }
        throw new RuntimeException("Given operator is not implemented: " + node.getOperator());
    }

    /*
    * This function generates code for an unary expression with the given operator and arguments.
    */
    private String generateUnaryExpression(ExpressionOperatorNode.ExpressionOperator operator, List<String> expressionList) {
        ST expression = generateUnary(operator);
        TemplateHandler.add(expression, "obj", expressionList.get(0));
        TemplateHandler.add(expression, "args", expressionList.subList(1, expressionList.size()));
        return expression.render();
    }

    /*
    * This function gets the template for unary expressions and replaces the placeholder with the given operator.
    */
    private ST generateUnary(ExpressionOperatorNode.ExpressionOperator operator) {
        ST template = currentGroup.getInstanceOf("unary");
        String operatorName;
        switch (operator) {
            case UNARY_MINUS:
                operatorName = "negative";
                break;
            case CARD:
                operatorName = "card";
                break;
            case DOMAIN:
                operatorName = "domain";
                break;
            case RANGE:
                operatorName = "range";
                break;
            case ID:
                operatorName = "identity";
                break;
            case CLOSURE:
                operatorName = "closure";
                break;
            case CLOSURE1:
                operatorName = "closure1";
                break;
            case ITERATE:
                operatorName = "iterate";
                break;
            case INVERSE_RELATION:
                operatorName = "inverse";
                break;
            case MIN:
                operatorName = "min";
                break;
            case MAX:
                operatorName = "max";
                break;
            case POW:
                operatorName = "pow";
                break;
            case FIN:
                operatorName = "fin";
                break;
            case GENERALIZED_UNION:
                operatorName = "union";
                break;
            case GENERALIZED_INTER:
                operatorName = "intersect";
                break;
            default:
                throw new RuntimeException("Given operator is not implemented: " + operator);
        }
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template;
    }

    /*
    * This function gets the template for binary expressions and replaces the placeholder with the given operator.
    */
    public ST generateBinary(ExpressionOperatorNode.ExpressionOperator operator) {
        ST template = currentGroup.getInstanceOf("binary");
        String operatorName;
        switch(operator) {
            case PLUS:
                operatorName = "plus";
                break;
            case MINUS:
                operatorName = "minus";
                break;
            case MULT:
                operatorName = "multiply";
                break;
            case DIVIDE:
                operatorName = "divide";
                break;
            case MOD:
                operatorName = "modulo";
                break;
            case INTERSECTION:
                operatorName = "intersect";
                break;
            case UNION:
                operatorName = "union";
                break;
            case SET_SUBTRACTION:
                operatorName = "difference";
                break;
            case FUNCTION_CALL:
                operatorName = "functionCall";
                break;
            case RELATIONAL_IMAGE:
                operatorName = "relationImage";
                break;
            case OVERWRITE_RELATION:
                operatorName = "overwrite";
                break;
            case DOMAIN_RESTRICTION:
                operatorName = "domainRestriction";
                break;
            case DOMAIN_SUBTRACTION:
                operatorName = "domainSubstraction";
                break;
            case RANGE_RESTRICTION:
                operatorName = "rangeRestriction";
                break;
            case RANGE_SUBTRACTION:
                operatorName = "rangeSubstraction";
                break;
            case DIRECT_PRODUCT:
                operatorName = "directProduct";
                break;
            case PARALLEL_PRODUCT:
                operatorName = "parallelProduct";
                break;
            case COMPOSITION:
                operatorName = "composition";
                break;
            case CARTESIAN_PRODUCT:
                operatorName = "cartesianProduct";
                break;
            case TOTAL_FUNCTION:
                operatorName = "totalFunction";
                break;
            case TOTAL_BIJECTION:
                operatorName = "totalBijection";
                break;
            case TOTAL_INJECTION:
                operatorName = "totalInjection";
                break;
            case TOTAL_SURJECTION:
                operatorName = "totalSurjection";
                break;
            case TOTAL_RELATION:
                operatorName = "totalRelation";
                break;
            case TOTAL_SURJECTION_RELATION:
                operatorName = "totalSurjectionRelation";
                break;
            case PARTIAL_BIJECTION:
                operatorName = "partialBijection";
                break;
            case PARTIAL_SURJECTION:
                operatorName = "partialSurjection";
                break;
            case PARTIAL_FUNCTION:
                operatorName = "partialFunction";
                break;
            case PARTIAL_INJECTION:
                operatorName = "partialInjection";
                break;
            default:
                throw new RuntimeException("Given operator is not implemented: " + operator);
        }
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template;
    }

    /*
    * This function generates code for an interval with the given arguments.
    */
    private String generateInterval(List<String> arguments) {
        ST interval = currentGroup.getInstanceOf("interval");
        TemplateHandler.add(interval, "arg1", arguments.get(0));
        TemplateHandler.add(interval, "arg2", arguments.get(1));
        return interval.render();
    }

    /*
    * This function generates code for a couple with the given arguments.
    */
    private String generateCouple(List<String> arguments, BType leftType, BType rightType) {
        ST couple = currentGroup.getInstanceOf("couple_create");
        TemplateHandler.add(couple, "leftType", typeGenerator.generate(leftType));
        TemplateHandler.add(couple, "rightType", typeGenerator.generate(rightType));
        TemplateHandler.add(couple, "arg1", arguments.get(0));
        TemplateHandler.add(couple, "arg2", arguments.get(1));
        return couple.render();
    }

    /*
    * This function generates code for set enumerations with the given arguments.
    */
    private String generateSetEnumeration(BType type, List<String> expressions) {
        ST enumeration = currentGroup.getInstanceOf("set_enumeration");
        BType subType = ((SetType) type).getSubType();
        TemplateHandler.add(enumeration,"enums", expressions);
        if(subType instanceof CoupleType) {
            TemplateHandler.add(enumeration, "leftType", typeGenerator.generate(((CoupleType) subType).getLeft()));
            TemplateHandler.add(enumeration, "rightType", typeGenerator.generate(((CoupleType) subType).getRight()));
        } else {
            TemplateHandler.add(enumeration, "type", typeGenerator.generate(subType));
        }
        TemplateHandler.add(enumeration, "isRelation", subType instanceof CoupleType);
        return enumeration.render();
    }

    /*
    * This function generates code for boolean constants as expressions.
    */
    private String generateBoolean(ExpressionOperatorNode.ExpressionOperator operator) {
        ST val = currentGroup.getInstanceOf("boolean_val");
        TemplateHandler.add(val, "val", operator == TRUE);
        return val.render();
    }

    public String visitQuantifiedExpressionNode(QuantifiedExpressionNode node) {
        return iterationConstructHandler.getIterationsMapIdentifier().get(node.toString());
    }

    public String visitSetComprehensionNode(SetComprehensionNode node) {
        return iterationConstructHandler.getIterationsMapIdentifier().get(node.toString());
    }

    public String visitLambdaNode(LambdaNode node) {
        return iterationConstructHandler.getIterationsMapIdentifier().get(node.toString());
    }

    public String generateBooleans() {
        return currentGroup.getInstanceOf("bool").render();
    }

    public void setOperatorGenerator(OperatorGenerator operatorGenerator) {
        this.operatorGenerator = operatorGenerator;
    }

    public void setSubstitutionGenerator(SubstitutionGenerator substitutionGenerator) {
        this.substitutionGenerator = substitutionGenerator;
    }

}
