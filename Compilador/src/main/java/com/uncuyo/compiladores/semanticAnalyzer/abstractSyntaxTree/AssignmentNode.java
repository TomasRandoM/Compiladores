package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase que representa los nodos de asignación
 * Extiende {@link SentenceNode}
 */
public class AssignmentNode extends SentenceNode {

    /**
     * Nodo izquierdo de la asignacion
     */
    private ExpressionNode leftNode;
    /**
     * Nodo derecho de la asignacion
     */
    private ExpressionNode rightNode;
    /**
     * Token que representa el operador y da informacion acerca de la linea si hubiese error
     */
    private Token operator;

    /**
     * Metodo para checkear semanticamente
     */
    public void check() {
        //pendiente
    }
}
