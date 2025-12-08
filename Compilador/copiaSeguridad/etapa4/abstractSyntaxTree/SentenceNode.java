package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;

/**
 * Clase que representa la abstraccion de los nodos de tipo sentencia
 */
public abstract class SentenceNode {

    /**
     * Metodo check que todos los nodos hijos deben implementar
     */
    public abstract void check() throws SemanticASTException;
}
