package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el nodo abstracto Expresion que engloba todas las expresiones
 */
public abstract class ExpressionNode {

    /**
     * Metodo que chequea los tipos
     * @return Type
     */
    public abstract Type check();
}
