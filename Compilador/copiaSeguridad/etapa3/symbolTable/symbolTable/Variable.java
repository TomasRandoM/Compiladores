package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase que representa a una variable local (por ejemplo dentro de un bloque if o un metodo)
 */
public class Variable {
    private String name;
    private Type type;
    private Token token;

    /**
     * Constructor de Variable
     * @author Paulina Suden y Tomas Rando
     * @param token Token correspondiente
     * @param type Type
     */
    public Variable(Token token, Type type) {
        name = token.getLexeme();
        this.token = token;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Token getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
