package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un new (de clase y de array).
 * Extiende {@link ExpressionNode}
 */
public class NewNode extends ExpressionNode {
    /**
     * Token que representa la palabra reservada new
     */
    private Token token;
    /**
     * String que representa que tipo de NewNode es. Puede ser "class" o "array"
     */
    private String option;
    /**
     * Token que representa el subtipo, si es una clase corresponde al token de una clase, si es un array corresponde
     * al token del tipo primitivo
     */
    private Token subType;
    /**
     * ChainedNode que representa un encadenamiento. Puede ser null
     */
    private ChainedNode chainedNode;
    /**
     * Lista de parametros en caso de que sea un constructor de clase. Puede ser null
     */
    private List<ExpressionNode> parameterList = new ArrayList<>();

    /**
     * Constructor de NewNode
     * @param token
     * @param option
     */
    public NewNode(Token token, String option) {
        this.token = token;
    }

    /**
     * Setter de subtype
     * @param subType
     */
    public void setSubType(Token subType) {
        this.subType = subType;
    }

    /**
     * Chequea las semantica
     * @return
     */
    public Type check() {
        //Pendiente
        return null;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Token getSubType() {
        return subType;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }
}
