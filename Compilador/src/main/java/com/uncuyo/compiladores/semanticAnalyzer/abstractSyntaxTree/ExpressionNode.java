package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el nodo abstracto Expresion que engloba todas las expresiones
 */
public abstract class ExpressionNode {

    public Type nodeType;
    /**
     * Metodo que chequea los tipos
     * @return Type
     */
    public abstract Type check() throws SemanticASTException;

    /**
     * Metodo que devuelve un token. Cada expresion devuelve un token distinto
     * @return Type
     */
    public abstract Token getToken() throws SemanticASTException;

    public abstract void codeGen(StringBuilder string);

    public abstract ChainedNode getLastChainedNode();

}
