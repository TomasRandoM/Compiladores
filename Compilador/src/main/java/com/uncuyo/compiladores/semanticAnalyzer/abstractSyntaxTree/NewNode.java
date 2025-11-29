package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.ArrayList;
import java.util.List;

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
    public Type check() {
        //Pendiente
        return null;
    }

    public Token getType() {
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
