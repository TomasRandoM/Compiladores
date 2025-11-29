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
     * Constructor de AssignmentNode
     * @param leftNode ExpressionNode
     * @param rightNode ExpressionNode
     */
    public AssignmentNode(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /**
     * Metodo para checkear semanticamente
     */
    public void check() {
        //pendiente
    }
}
