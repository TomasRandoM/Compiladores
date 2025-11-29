package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class Class {

    private String name;
    private Token token;
    private String parentClass;
    private Map<String, Attribute> attributes = new HashMap<>();
    private Map<String, Method> methods = new HashMap<>();
    private Constructor constructor;
    private boolean implInitialized;
    private boolean classInitialized;
    private Token implToken;
    private Token classToken;

    public Class(Token token) {
        this.name = token.getLexeme();
        this.token = token;
    }

    public Class(Token token, String name) {
        this.name = name;
        this.token = token;
    }

    public Token getImplToken() {
        return implToken;
    }
    public void setImplToken(Token implToken) {
        this.implToken = implToken;
    }

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Agrega un atributo al hashmap
     * @param attribute
     * @throws SemanticException Excepcion si el atributo de clase ya ha sido definido en el ambito
     */
    public void addAttributes(Attribute attribute) throws SemanticException {
        if (attributes.containsKey(attribute.getName())) {
            throw new SemanticException(attribute.getToken(),
                    "El atributo de clase " + attribute.getName() + " " +
                            "ya fue definido en este ámbito");
        }
        attributes.put(attribute.getName(), attribute);
    }

    /**
     * Agrega un metodo al hashmap
     * @param method
     * @throws SemanticException Excepcion si el metodo ya ha sido definido en el ambito
     */
    public void addMethods(Method method) throws SemanticException {
        if (methods.containsKey(method.getName())) {
            throw new SemanticException(method.getToken(),
                    "El método " + method.getName() + " " +
                            "ya fue definido en este ámbito");
        }
        methods.put(method.getName(), method);
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setClassInitialized(boolean classInitialized) {
        this.classInitialized = classInitialized;
    }

    public boolean isClassInitialized() {
        return classInitialized;
    }

    public boolean isImplInitialized() {
        return implInitialized;
    }

    public void setImplInitialized(boolean implInitialized) {
        this.implInitialized = implInitialized;
    }

    public void setConstructor(Constructor constructor) throws SemanticException {
        if (this.constructor == null) {
            this.constructor = constructor;
        }
        else {
            throw new SemanticException(constructor.getToken(), "Una clase no puede tener más de un constructor");
        }
    }
}
