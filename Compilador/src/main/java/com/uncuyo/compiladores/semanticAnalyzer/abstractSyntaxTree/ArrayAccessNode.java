package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el acceso a un array
 * Extiende {@link OperandNode}
 */
public class ArrayAccessNode extends OperandNode{
    /**
     * Token que representa al array
     */
    private Token token;
    /**
     * Representa la expresion dentro del array
     */
    private ExpressionNode expressionNode;
    /**
     * Representa encadenamiento en un array
     */
    private ChainedNode chainedNode;

    public ArrayAccessNode() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Metodo que chequea el tipo
     * @return
     */
    public Type check() {
        return null;
    }


}
