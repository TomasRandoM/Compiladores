package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;

//FALTA EL TIPO ARRAY

public class Type {

    private String name;
    private Type arrType;
    private Token token;


    /**
     * Constructor de Type
     * @author Paulina Suden y Tomas Rando
     * @param token Token correspondiente
     * @param typeName String con el tipo correspondiente.
     */
    public Type(Token token, String typeName) {
        switch (typeName) {
            case "Int":
                name = "Int";
                break;
            case "void":
                name = "void";
                break;
            case "Bool":
                name = "Bool";
                break;
            case "Str":
                name = "Str";
                break;
            case "Char":
                name = "Char";
                break;
            case "class":
                name = token.getLexeme();
                break;
            case "Double":
                name = "Double";
                break;
            case "Array":
                name = "Array";
                break;
            default:
                System.out.println("Error al llamar a Type");
                break;
        }
        this.token = token;
    }

    public void setArrType(Type arrType) {
        this.arrType = arrType;
    }

    public Type getArrType() {
        return arrType;
    }

    public String getName() {
        return name;
    }

    public Token getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
