package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el acceso a un array
 * Extiende {@link OperandNode}
 */
public class ArrayAccessNode extends OperandNode{
    /**
     * Token que representa al array
     */
    private Token token;
    /**
     * Representa la expresion dentro del array
     */
    private ExpressionNode expressionNode;
    /**
     * Representa encadenamiento en un array
     */
    private ChainedNode chainedNode;

    /**
     * Constructor de la clase ArrayAccess
     * @param token
     */
    public ArrayAccessNode(Token token) {
        this.token = token;
    }

    /**
     * Metodo que chequea el tipo
     * @return
     */
    public Type check() {
        return null;
    }
}
