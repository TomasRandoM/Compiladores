package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase correspondiente a los métodos
 * @author Paulina Suden y Tomas Rando
 */
public class Method {

    private String name;
    private Type type;
    private Token token;
    private boolean isStaticMethod;
    private Map<String, Variable> variables = new HashMap<>();
    private Map<String, Parameter> parameters = new LinkedHashMap<>();

    /**
     * Constructor
     * @param token Token
     * @param type Type
     * @param isStaticMethod boolean
     */
    public Method(Token token, Type type, boolean isStaticMethod) {
        this.token = token;
        this.type = type;
        this.name = token.getLexeme();
        this.isStaticMethod = isStaticMethod;
    }

    /**
     * Constructor para manejar el caso del constructor
     * @param name String
     * @param token Token
     * @param type Type
     * @param isStaticMethod boolean
     */
    public Method(String name, Token token, Type type, boolean isStaticMethod) {
        this.token = token;
        this.type = type;
        this.name = name;
        this.isStaticMethod = isStaticMethod;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isStaticMethod() {
        return isStaticMethod;
    }

    public void setStaticMethod(boolean staticMethod) {
        isStaticMethod = staticMethod;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Agrega un parametro al hashmap
     * @param parameter Parameter
     * @throws SemanticException Excepcion si el parametro ya ha sido definido en el ambito
     */
    public void addParameter(Parameter parameter) throws SemanticException {
        if (parameters.containsKey(parameter.getName())) {
            throw new SemanticException(parameter.getToken(),
                    "El parámetro " + parameter.getName() + " " +
                            "ya fue definido en este ámbito");
        }
        parameters.put(parameter.getName(), parameter);
    }

    /**
     * Agrega una variable al hashmap
     * @param variable Variable
     * @throws SemanticException Excepcion si la variable ya ha sido definida en el ambito
     */
    public void addVariable(Variable variable) throws SemanticException {
        if (parameters.containsKey(variable.getName())) {
            throw new SemanticException(variable.getToken(),
                    "La variable " + variable.getName() + " " +
                            "posee el mismo identificador que un " +
                            "parámetro del método");
        }

        if (variables.containsKey(variable.getName())) {
            throw new SemanticException(variable.getToken(),
                    "La variable " + variable.getName() + " " +
                            "ya fue definida en este ámbito");
        }
        variables.put(variable.getName(), variable);
    }

}
