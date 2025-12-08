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
        Class currentClass = null;
        if (this.currentClass != null) {
            currentClass = SymbolTable.getClass(this.currentClass);
        }

        Method currentMethod;

        if (this.currentMethod == null) {
            if (this.currentClass == null) {
                throw new SemanticASTException(token, "La clase y el método actual son nulos");
            }
            currentMethod = currentClass.getConstructor();
        }
        else {
            if (this.currentMethod.equals("start")) {
                currentMethod = SymbolTable.getStartMethodStored();
            }
            else {
                if (this.currentClass == null) {
                    throw new SemanticASTException(token, "La clase es nula");
                }
                currentMethod = currentClass.getMethods().get(this.currentMethod);
            }
        }

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
                if ((currentClass != null) && (currentClass.getAttributes().get(token.getLexeme()) != null)) {
                    if (currentMethod.isStaticMethod()) {
                        throw new SemanticASTException(this.token, "El atributo " +
                                "de instancia " + token.getLexeme() +
                                " es accedido desde un contexto estático.");
                    }
                    type = currentClass.getAttributes().get(token.getLexeme()).getType();
                    type.setToken(this.token);
                }
                else {
                    throw new SemanticASTException(this.token, "La variable " +
                            token.getLexeme() + " no ha sido " +
                            "correctamente declarada en este contexto.");
                }
            }
        }
        this.nodeType = type;
        return type;
    }

    @Override
    public Token getToken() {
        return token;
    }

    /**
     * Genera el codigo para acceder a la direccion de una variable en la pila o a un atributo en un CIR
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        Class currentClass = null;
        if (this.currentClass != null) {
            currentClass = SymbolTable.getClass(this.currentClass);
        }
        Method currentMethod;
        if (this.currentMethod == null) {
            currentMethod = currentClass.getConstructor();
        } else {
            if (this.currentMethod.equals("start")) {
                currentMethod = SymbolTable.getStartMethodStored();
            } else {
                currentMethod = currentClass.getMethods().get(this.currentMethod);
            }
        }
        //Calculamos el offset, dependiendo de si está en el parámetro, variable o atributo.
        int offset = 0;
        boolean isAttribute = false;
        if (currentMethod.getParameters().get(token.getLexeme()) != null) {
            offset = currentMethod.getParameterOffset(token.getLexeme());
        } else {
            if (currentMethod.getVariables().get(token.getLexeme()) != null) {
                offset = currentMethod.getVariableOffset(token.getLexeme());
            } else {
                if ((currentClass != null) && (currentClass.getAttributes().get(token.getLexeme()) != null)) {
                    offset = currentClass.getAttributeOffset(token.getLexeme());
                    isAttribute = true;
                }
            }
        }
        string.append("#Carga de variable \n");
        if (isAttribute) {
            int parameterSize = currentMethod.getParameterMemory();
            string.append("#Cargamos la direccion del atributo en a0 utilizando la \n");
            string.append("#cantidad de parametros para acceder a self, y de ahi al atrubuto\n");
            string.append("lw $a0, ").append(parameterSize).append("($fp)\n");
            string.append("la $a0, ").append(offset).append("($a0) \n");
        }
        else {
            string.append("#Cargamos la direccion de la variable en a0 utilizando el \n");
            string.append("offset con el fp \n");
            string.append("la $a0, ").append(offset).append("($fp) \n");
        }

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
