package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
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
     * Clase del metodo (y la variable)
     */
    private String currentClass;

    /**
     * Metodo actual
     */
    private String currentMethod;

    /**
     * Constructor de la clase VariableNode
     * @param token
     */
    public VariableNode(Token token, String currentClass, String currentMethod) {
        this.token = token;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
    }

    /**
     * Metodo que chequea el tipo de la variable
     * @return Type
     */
    public Type check() throws SemanticASTException {
        Class currentClass = SymbolTable.getClass(this.currentClass);
        Method currentMethod = currentClass.getMethods().get(this.currentMethod);
        Type type;
        if (currentMethod.getParameters().get(token.getLexeme()) != null) {
            type = currentMethod.getParameters().get(token.getLexeme()).getType();
            type.setToken(this.token);
        }
        else {
            if (currentMethod.getVariables().get(token.getLexeme()) != null) {
                type = currentMethod.getVariables().get(token.getLexeme()).getType();
                type.setToken(this.token);
            }
            else {
                if (currentClass.getAttributes().get(token.getLexeme()) != null) {
                    type = currentClass.getAttributes().get(token.getLexeme()).getType();
                    type.setToken(this.token);
                }
                else {
                    throw new SemanticASTException(this.token, "La variable " +
                            token.getLexeme() + " no ha sido " +
                            "correctamente declarada");
                }
            }
        }
        return type;
    }

    @Override
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
    }
}
