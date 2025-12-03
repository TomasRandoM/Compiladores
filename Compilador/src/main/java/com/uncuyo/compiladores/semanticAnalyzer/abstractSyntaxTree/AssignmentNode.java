package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.sql.SQLOutput;

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
        Type leftType;
        if (leftNode instanceof ChainedAccessNode) {
            ChainedAccessNode auxNode = (ChainedAccessNode) leftNode;
            leftType = auxNode.checkNames(null);
        }
        else {
            if (leftNode instanceof SelfNode) {
                if (((SelfNode) leftNode).getChainedNode() == null) {
                    throw new SemanticASTException(leftNode.getToken(), "Self " +
                            "no puede ser asignado");
                }
            }
            leftType = leftNode.check();
        }

        System.out.println("RIGHT NODE ASIGNACION" + rightNode);
        Type rightType;
        if (rightNode instanceof ChainedAccessNode) {
            ChainedAccessNode auxNode = (ChainedAccessNode) rightNode;
            rightType = auxNode.checkNames(null);
        }
        else {
            rightType = rightNode.check();
        }
        System.out.println("Tipo derecho: " + rightType);

        if (leftType == null) {
            System.out.println("LEFT NODE ASSIGNMENT NODE: " + ((ChainedAccessNode) leftNode).getToken().getLexeme());
        }

        System.out.println("LEFT NODE ASSIGNMENT NODE: " + leftNode.toString());
        if (!leftType.getName().equals(rightType.getName())) {
            if ((leftType.getName().equals("Double") && !rightType.getName().equals("Int")) ||
               (!rightType.getName().equals("nil") && !(leftType.getName().equals("Double") && rightType.getName().equals("Int")))
            ) {
                throw new SemanticASTException(rightNode.getToken(), "El tipo asignado " +
                        "es incorrecto. " +
                        "Se esperaba: " + leftType.getName() +
                        ". Se obtuvo: " + rightType.getName() + ".");
            }
        }
    }

}

