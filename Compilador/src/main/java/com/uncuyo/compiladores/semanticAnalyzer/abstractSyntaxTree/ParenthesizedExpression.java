package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Metodo que representa una expresion parentizada
 * Extiende {@link ExpressionNode}
 */
public class ParenthesizedExpression extends ExpressionNode {

    /**
     * Representa la expresion
     */
    private ExpressionNode expressionNode;
    /**
     * Representa el encadenamiento
     */
    private ChainedNode chainedNode;

    /**
     * Metodo para chequear el tipo
     * @return
     */
    public Type check(){
        return null;
    }

}
