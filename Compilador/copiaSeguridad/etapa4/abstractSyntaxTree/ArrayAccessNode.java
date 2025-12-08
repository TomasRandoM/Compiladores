package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

/**
 * Clase que representa el acceso a un array
 * Extiende {@link OperandNode}
 */
public class ArrayAccessNode extends OperandNode{
    /**
     * Token que representa al array
     */
    private Token token;
    /**
     * Representa la expresion dentro del array
     */
    private ExpressionNode expressionNode;
    /**
     * Representa encadenamiento en un array
     */
    private ChainedNode chainedNode;
    /**
     * Representa la clase del metodo en donde esta el Array
     */
    private String className;
    /**
     * Representa el metodo donde esta el Array.
     */
    private String methodName;

    public ArrayAccessNode(Token token, String className, String methodName) {
        this.token = token;
        this.className = className;
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

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Metodo que chequea el tipo
     * @return
     */
    public Type check() throws SemanticASTException {
        Class currentClass;
        Method currentMethod;

        // obtener la clase y el metodo actual
        if (methodName == null) {
            if (className == null) {
                //Este error no debería aparecer
                throw new SemanticASTException(token, "El método y " +
                        "la clase actuales son nulos");
            }
            currentClass = SymbolTable.getClass(className);
            currentMethod = currentClass.getConstructor();
        }
        else {
            if (methodName.equals("start")) {
                currentMethod = SymbolTable.getStartMethodStored();
            }
            else {
                currentMethod = SymbolTable.getClass(className).getMethods().get(methodName);
            }
        }

        Type arrayType;

        // buscar primero en parámetros
        if (currentMethod.getParameters().containsKey(token.getLexeme())) {
            arrayType = currentMethod.getParameters().get(token.getLexeme()).getType();
        }
        // buscar en variables locales
        else {
            if (currentMethod.getVariables().containsKey(token.getLexeme())) {
                arrayType = currentMethod.getVariables().get(token.getLexeme()).getType();
            }
            else {
                if ((className != null) && (SymbolTable.getClass(className).getAttributes().containsKey(token.getLexeme()))) {

                    Attribute attr = SymbolTable.getClass(className).getAttributes().get(token.getLexeme());
                    //verifico si es privado
                    /*
                    if (!attr.getIsPublic()) {
                        if (!SymbolTable.getClass(className).getName().equals(this.className)) {
                            throw new SemanticASTException(token, "No se puede acceder al atributo privado "
                                    + attr.getName() + " desde la clase " + this.className);
                        }
                    }
                     */
                    arrayType = attr.getType();
                }
                else {
                    throw new SemanticASTException(token,
                            "El identificador '" + token.getLexeme() + "' no ha sido declarado.");
                }
            }
        }

        // Validar que el tipo sea Array
        if (!arrayType.getName().equals("Array")) {
            throw new SemanticASTException(token,
                    "Se intentó indexar '" + token.getLexeme() +
                            "' de tipo " + arrayType.getName() + " pero no es un Array.");
        }

        //el indice debe ser una expresion (no a[])
        if (expressionNode == null) {
            throw new SemanticASTException(token, "Se esperaba una expresión del índice del" +
                    " Array");
        }

        //obtengo el tipo del indice
        Type intType = expressionNode.check();

        if (!intType.getName().equals("Int")) {
            throw new SemanticASTException(token, "El índice debe ser de tipo Int. Se encontró: " +
                    intType.getName());
        }

        //obtengo el tipo que devuelve el array
        Type arrType = arrayType.getArrType();

        if (arrType == null) {
            throw new SemanticASTException(token,
                    "El tipo interno del Array es null.");
        }

        if (chainedNode != null) {
            throw new SemanticASTException(token, "Un array no puede tener un encadenamiento.");

        }

        return arrType;
    }

}
