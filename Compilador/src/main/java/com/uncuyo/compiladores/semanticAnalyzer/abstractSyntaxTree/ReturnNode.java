package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;

/**
 * Nodo que representa un return
 * Extiende {@link SentenceNode}
 */
public class ReturnNode extends SentenceNode {

    /**
     * Nodo que representa la expresión que devuelve
     */
    private ExpressionNode expressionNode;

    /**
     * Constructor de ReturnNode
     * @param expressionNode ExpressionNode
     */
    public ReturnNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    /**
     * Metodo para checkear la semantica
     */
    public void check() throws SemanticASTException {
        expressionNode.check();
    }

}
