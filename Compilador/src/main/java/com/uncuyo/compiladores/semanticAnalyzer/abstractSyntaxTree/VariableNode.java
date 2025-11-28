package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa las variables
 * Extiende {@link OperandNode}
 */
public class VariableNode extends OperandNode {
    /**
     * Representa el token de la variable
     */
    private Token token;

    /**
     * Constructor de la clase VariableNode
     * @param token
     */
    public VariableNode(Token token) {
        this.token = token;
    }

    /**
     * Metodo que chequea el tipo de la variable
     * @return Type
     */
    public Type check(){
        return null;
    }
}
