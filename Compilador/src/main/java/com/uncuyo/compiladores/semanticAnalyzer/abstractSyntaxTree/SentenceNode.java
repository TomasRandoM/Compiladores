package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

/**
 * Clase que representa la abstraccion de los nodos de tipo sentencia
 */
public abstract class SentenceNode {

    /**
     * Metodo check que todos los nodos hijos deben implementar
     */
    public abstract void check();
}
