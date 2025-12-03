package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Parameter;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa un new (de clase y de array).
 * Extiende {@link OperandNode}
 */
public class NewNode extends OperandNode{
    /**
     * Token que representa el subtipo, si es una clase corresponde al token de una clase, si es un array corresponde
     * al token del tipo primitivo
     */
    private Token type;
    /**
     * String que representa que tipo de NewNode es. Puede ser "class" o "array"
     */
    private String option;
    /**
     * ChainedNode que representa un encadenamiento. Puede ser null
     */
    private ChainedNode chainedNode;
    /**
     * chainedNode representa la expresion dentro de un array
     */
    private ExpressionNode expressionNode;
    /**
     * Lista de parametros en caso de que sea un constructor de clase. Puede ser null
     */
    private List<ExpressionNode> parameterList = new ArrayList<>();

    /**
     * Constructor de NewNode
     * @param type Token
     * @param option String
     */
    public NewNode(Token type, String option) {
        if (option.equals("class")) {
            this.option = "class";
        }
        else {
            if (option.equals("array")) {
                this.option = "array";
            }
        }
        this.type = type;
    }

    /**
     * Setter de subtype
     * @param type
     */
    public void setType(Token type) {
        this.type = type;
    }

    /**
     * Chequea las semantica
     * @return
     */
    public Type check() throws SemanticASTException {
        Type newType;
        if (option.equals("class")) {
            //Resolución de nombres
            if (SymbolTable.getClass(type.getLexeme()) == null) {
                throw new SemanticASTException(type, "La clase " +
                        type.getLexeme() + " referenciada " +
                        "no se encuentra declarada");
            }
            //Constructor si posee porque lo chequeamos en la tabla de símbolos
            checkParameters(type, parameterList, type.getLexeme());
            if (chainedNode != null) {
                //Resolución de nombres (para los encadenados). Nos trae el último tipo de la serie de encadenamientos
                newType = chainedNode.checkNames(new Type(type, "class"));
            }
            else {
                newType = new Type(type, "class");
            }
        }
        else {
            if (chainedNode != null) {
                throw new SemanticASTException(chainedNode.getToken(), "Un array no puede tener un encadenamiento");
            }
            else {
                //Si pudiese haber arrays de tipo diferente aca debería haber resolución de nombres
                newType = new Type(type, "Array");
                newType.setArrType(new Type(type, type.getLexeme()));
            }
        }
        return newType;
    }

    public void checkParameters(Token token, List<ExpressionNode> expressionList, String class1) throws SemanticASTException {
        Map<String, Parameter> parameters = SymbolTable.getClass(class1).getConstructor().getParameters();

        if (parameters.size() != expressionList.size()) {
            throw new SemanticASTException(token, "El número de parámetros " +
                    "del constructor no coincide con los brindados");
        }

        int index = 0;
        for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
            Type parameterType = entry.getValue().getType();
            ExpressionNode expressionNode = expressionList.get(index);
            Type providedType;
            if (expressionNode instanceof ChainedNode) {
                providedType = ((ChainedNode) expressionNode).checkNames(null);
            }
            else {
                providedType = expressionNode.check();
            }

            if (parameterType.getName().equals(providedType.getName())) {
                if (parameterType.getName().equals("Array")) {
                    if (!parameterType.getArrType().getName().equals(providedType.getArrType().getName())) {
                        throw new SemanticASTException(providedType.getToken(), "Tipo " +
                                "de Array incorrecto en " +
                                "parámetros del constructor de la clase " +
                                class1 + ". Se obtuvo: " +
                                providedType.getArrType().getName() +
                                ". Se esperaba " +
                                parameterType.getArrType().getName());
                    }
                }
                index++;
            }
            else {
                if (SymbolTable.getClass(providedType.getName()).isInheritedClass(parameterType.getName())) {
                    index++;
                }
                else {
                    throw new SemanticASTException(entry.getValue().getToken(), "Tipo incorrecto en " +
                            "parámetros del constructor de la " +
                            "clase " + class1 + ". Se obtuvo: " +
                            providedType.getName() + ".Se esperaba " +
                            parameterType.getName());
                }
            }
        }
    }

    //Getters y setters
    public Token getType() {
        return type;
    }

    public Token getToken() {
        return type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public ExpressionNode getExpressionNode() {
        return chainedNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }
}
