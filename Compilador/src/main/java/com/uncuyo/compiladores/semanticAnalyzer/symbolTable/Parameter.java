package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase que representa a un parametro que existe solo en un metodo y representa un valor pasado desde afuera
 */
public class Parameter {
    /**
     * Nombre del parametro
     */
    private String name;
    /**
     * Tipo del parametro
     */
    private Type type;
    /**
     * Token que representa al nombre del parametro
     */
    private Token token;

    /**
     * Constructor de Parameter
     * @author Paulina Suden y Tomas Rando
     * @param token Token correspondiente
     * @param type Type
     */
    public Parameter(Token token, Type type) {
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

