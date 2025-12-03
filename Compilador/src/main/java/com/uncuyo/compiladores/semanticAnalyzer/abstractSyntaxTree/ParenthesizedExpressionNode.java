package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Metodo que representa una expresion parentizada
 * Extiende {@link ExpressionNode}
 */
public class ParenthesizedExpressionNode extends ExpressionNode {

    /**
     * Representa la expresion
     */
    private ExpressionNode expressionNode;
    /**
     * Representa el encadenamiento
     */
    private ChainedNode chainedNode;

    public ParenthesizedExpressionNode() {
    }

    public Token getToken() throws SemanticASTException {
        return expressionNode.getToken();
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
     * Metodo para chequear el tipo
     * @return
     */
    public Type check() throws SemanticASTException {
        System.out.println("entro expresion Paretnizada");
        Type type;
        if (expressionNode instanceof ChainedNode) {
            type = ((ChainedNode) this.expressionNode).checkNames(null);
        }
        else {
            type = this.expressionNode.check();
        }
        if (this.chainedNode != null) {
            type = this.chainedNode.checkNames(type);
        }

        return type;
    }

}
