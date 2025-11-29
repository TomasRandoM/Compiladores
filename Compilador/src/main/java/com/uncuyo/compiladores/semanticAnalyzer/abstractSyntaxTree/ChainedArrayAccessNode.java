package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase que representa un acceso encadenado desde un acceso a un array.
 * Extiende {@link ChainedAccessNode}
 */
public class ChainedArrayAccessNode extends ChainedAccessNode {

    /**
     * Representa la expresion que indica la posicion del array
     */
    private ExpressionNode expression;

    /**
     * Constructor de ChainedArrayAccess
     * @param name
     */
    public ChainedArrayAccessNode(Token name) {
        super(name);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }
}
