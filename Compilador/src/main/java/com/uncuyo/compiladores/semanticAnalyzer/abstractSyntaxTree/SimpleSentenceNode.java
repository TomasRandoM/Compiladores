package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;

import java.sql.SQLOutput;

/**
 * Nodo que representa una sentencia simple
 * Extiende {@link SentenceNode}
 */
public class SimpleSentenceNode extends SentenceNode {

    /**
     * Nodo que representa la expresión que contiene
     */
    private ExpressionNode expressionNode;

    public SimpleSentenceNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    /**
     * Metodo para checkear semanticamente
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
}
