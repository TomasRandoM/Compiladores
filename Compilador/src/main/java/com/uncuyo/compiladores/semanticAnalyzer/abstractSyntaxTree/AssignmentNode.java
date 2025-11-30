package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
     * Verifica que ambos lados de la asignación tengan el mismo tipo
     */
    public void check() throws SemanticASTException {
        Type leftType = leftNode.check();
        Type rightType = rightNode.check();

        if (leftType != rightType) {
            throw new SemanticASTException(rightType.getToken(), "El tipo asignado " +
                    "es incorrecto. " +
                    "Se esperaba: " + leftType.getName() +
                    ". Se obtuvo: " + rightType.getName());
        }

    }
}
