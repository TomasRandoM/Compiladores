package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;

import java.sql.SQLOutput;

/**
 * Nodo que representa una sentencia simple
 * Extiende {@link SentenceNode}
 */
public class SimpleSentenceNode extends SentenceNode {

    /**
     * Nodo que representa la expresion que contiene
     */
    private ExpressionNode expressionNode;

    public SimpleSentenceNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    /**
     * Metodo para chequear la semantica
     */
    @Override
    public void check() throws SemanticASTException {
        if (expressionNode instanceof ChainedNode) {
            ((ChainedNode) expressionNode).checkNames(null);
        }
        else {
            expressionNode.check();
        }
    }

    @Override
    public void codeGen(StringBuilder string) {

    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
