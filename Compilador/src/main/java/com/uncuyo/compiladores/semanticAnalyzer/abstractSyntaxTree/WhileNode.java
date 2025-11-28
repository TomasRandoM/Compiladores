package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

/**
 * Clase que representa el bucle While
 * Extiende {@link SentenceNode}
 */
public class WhileNode extends SentenceNode {

    /**
     * Nodo que representa la condición del while
     */
    private ExpressionNode expressionNode;
    /**
     * Nodo que representa la sentencia dentro del while
     */
    private SentenceNode sentenceNode;

    /**
     * Metodo para chequear la semantica
     **/
    @Override
    public void check() {

    }
}
