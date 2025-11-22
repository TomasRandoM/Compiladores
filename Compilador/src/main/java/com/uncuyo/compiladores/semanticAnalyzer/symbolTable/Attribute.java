package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase que representa a una variable de instancia
 */
public class Attribute {
    private String name;
    private Type type;
    private Token token;
    private boolean isPublic;

    /**
     * Constructor de Attribute
     * @author Paulina Suden y Tomas Rando
     * @param token Token correspondiente
     * @param type Type
     */
    public Attribute(Token token, Type type, boolean isPublic) {
        name = token.getLexeme();
        this.token = token;
        this.type = type;
        this.isPublic = isPublic;
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
    
    public boolean getIsPublic() {
        return isPublic;
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

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}

