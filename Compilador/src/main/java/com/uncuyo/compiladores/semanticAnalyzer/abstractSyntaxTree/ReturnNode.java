package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Nodo que representa un return
 * Extiende {@link SentenceNode}
 */
public class ReturnNode extends SentenceNode {

    /**
     * Clase en la cual se encuentra el metodo que contiene el return
     */
    private String className;

    /**
     * Metodo en el cual se encuentra el return
     */
    private String methodName;

    /**
     * Token de la palabra reservada ret
     */
    private Token token;
    /**
     * Nodo que representa la expresión que devuelve
     */
    private ExpressionNode expressionNode;

    /**
     * Constructor de ReturnNode
     * @param expressionNode ExpressionNode
     * @param className String con el nombre de la clase actual que contiene al metodo
     * @param methodName String con el nombre del metodo que contiene el return
     */
    public ReturnNode(ExpressionNode expressionNode, String className, String methodName, Token token) {
        this.expressionNode = expressionNode;
        this.className = className;
        this.methodName = methodName;
        this.token = token;
    }

    /**
     * Metodo para checkear la semantica
     */
    public void check() throws SemanticASTException {
        if (methodName == null) {
            throw new SemanticASTException(token, "El constructor " +
                    "de " + className + " no puede tener ret");
        }
        if (methodName.equals("start")) {
            throw new SemanticASTException(token, "El bloque start no " +
                    "puede tener ret");
        }
        Type returnMethodType = SymbolTable.getClass(className).getMethods().get(methodName).getType();
        if (returnMethodType.getName().equals("void")) {
            throw new SemanticASTException(token, "Un método que devuelva " +
                    "un tipo void no puede " +
                    "contener ret");
        }
        Type returnType = expressionNode.check();
        if (!returnType.getName().equals(returnMethodType.getName()) && !returnType.getName().equals("nil")) {
            if (!(SymbolTable.getClass(returnType.getName()).isInheritedClass(returnMethodType.getName()))) {
                throw new SemanticASTException(returnType.getToken(), "El tipo " +
                        "de retorno no coincide, se esperaba " +
                        returnMethodType.getName() + " y se " +
                        "encontró " + returnType.getName());
            }
        }
        //Si el return está correcto se indica en el AST que el metodo actual tiene un return correcto
        AST.setIsReturnPresent(true);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }
}
