package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.List;

/**
 * Metodo que representa una llamada a un metodo
 * Extiende {@link OperandNode}
 */
public class MethodCallNode extends OperandNode {

    private String className;

    private boolean isStatic;
    /**
     * Token que representa el id del metodo
     */
    private Token token;
    /**
     * Lista que representa los parametros del metodo
     */
    private List<ExpressionNode> parameterList;
    /**
     * ChainNode que representa los encadenamientos
     */
    private ChainedNode chainNode;

    /**
     * Constructor de la clase
     * @param token Token
     * @param isStatic boolean
     * @param className String con el nombre de la clase
     */
    public MethodCallNode(String className, Token token, boolean isStatic) {
        this.className = className;
        this.isStatic = isStatic;
        this.token = token;
    }




    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }

    public ChainedNode getChainNode() {
        return chainNode;
    }

    public void setChainNode(ChainedNode chainNode) {
        this.chainNode = chainNode;
    }

    /**
     * Metodo para chequear los tipos
     * @return Type
     */
    @Override
    public Type check() {
        return null;
    }
}
