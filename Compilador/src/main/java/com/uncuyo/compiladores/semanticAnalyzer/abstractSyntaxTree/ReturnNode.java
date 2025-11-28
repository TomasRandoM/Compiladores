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
     * Metodo para checkear la semantica
     */
    public void check() {}
}
