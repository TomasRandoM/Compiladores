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

    /**
     * Nombre del metodo
     */
    private String name;
    /**
     * Tipo de retorno del metodo
     */
    private Type type;
    /**
     * Token del id del metodo
     */
    private Token token;
    /**
     * Booleano que indica si es estatico
     */
    private boolean isStaticMethod;
    /**
     * Variables locales del metodo
     */
    private Map<String, Variable> variables = new HashMap<>();
    /**
     * Parametros del metodo
     */
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

    /**
     * Calcula el offset del parametro en el metodo. Lo recorre obligatoriamente
     * porque el double ocupa el doble de espacio. Devuelve un offset positivo teniendo
     * en cuenta la estructura del registro de activacion
     * @param parameter String con el identificador del parametro
     * @return int offset
     */
    public int getParameterOffset(String parameter) {
        int memory = 4;
        for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
            if (entry.getKey().equals(parameter)) {
                return memory;
            }
            else {
                if (entry.getValue().getType().getName().equals("Double")) {
                    memory += 8;
                }
                else {
                    memory += 4;
                }
            }
        }
        //No se debería llegar a este return, si se llega falló el semántico
        return memory;
    }

    /**
     * Calcula el offset de la variable en el metodo. En este caso, estan por debajo del fp por lo
     * que el offset sera negativo.
     * @param variable String con el identificador de la variable
     * @return int offset
     */
    public int getVariableOffset(String variable) {
        int memory = -4;
        for (Map.Entry<String, Variable> entry : variables.entrySet()) {
            if (entry.getKey().equals(variable)) {
                return memory;
            }
            else {
                if (entry.getValue().getType().getName().equals("Double")) {
                    memory -= 8;
                }
                else {
                    memory -= 4;
                }
            }
        }
        //No se debería llegar a este return, si se llega falló el semántico
        return memory;
    }

    /**
     * Calcula la memoria que ocupan los parametros en la pila.
     * @return int con la memoria que ocupan todos los parametros. Apunta a la direccion
     * donde estaria el self. Es decir, la siguiente direccion despues de los parametros
     */
    public int getParameterMemory() {
        System.out.println("hola");
        int memory = 4;
        System.out.println("entro " + memory);
        for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
            if (entry.getValue().getType().getName().equals("Double")) {
                memory += 8;
            }
            else {
                memory += 4;
            }
        }
        //No se debería llegar a este return, si se llega falló el semántico
        System.out.println("salgo " + memory);
        return memory;
    }



}
