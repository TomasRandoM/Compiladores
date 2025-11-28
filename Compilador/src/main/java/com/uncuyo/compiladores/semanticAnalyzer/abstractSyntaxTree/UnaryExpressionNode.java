package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Metodo que representa una expresion unaria
 * Extiende {@link ExpressionNode}
 */
public class UnaryExpressionNode extends ExpressionNode {

    /**
     * Representa la expresion
     */
    private ExpressionNode expressionNode;
    /**
     * Token que representa el operador y sirve para guardar la linea
     */
    Token operator;


    /**
     * Metodo para chequear los tipos
     * @return
     */
    public Type check() {
        return null;
    }
}
