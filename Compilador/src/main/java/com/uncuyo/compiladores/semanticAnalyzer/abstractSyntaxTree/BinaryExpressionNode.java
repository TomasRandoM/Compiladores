package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

public class BinaryExpressionNode extends ExpressionNode {
    /**
     * Token que representa el operador y sirve para guardar la linea
     */
    Token operator;
    /**
     * Expresion de la izquierda
     */
    ExpressionNode left;
    /**
     * Expresion de la derecha
     */
    ExpressionNode right;

    /**
     * Constructor de BinaryExpressionNode
     * @param left ExpressionNode
     * @param right ExpressionNode
     * @param operator Token del operador
     */
    public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, Token operator) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    /**
     * Metodo para checkear la semantica
     * @return Type
     */
    public Type check() {
        //Pendiente
        return null;
    }
}
