package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

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
    public void check() {}
}
