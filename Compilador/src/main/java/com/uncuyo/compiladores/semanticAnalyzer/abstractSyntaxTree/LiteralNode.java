package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa a un literal
 * Extiende {@link OperandNode}
 */
public class LiteralNode extends OperandNode {

    /**
     * Representa el token del literal
     */
    private Token token;
    /**
     * Representa al literal (true, false, nil, etc.)
     */
    private String option;

    /**
     * Constructor de LiteralNode
     * @param token Token
     * @param option String
     */
    public LiteralNode(Token token, String option) {
        switch (option) {
            case "true":
                this.option = "true";
                break;
            case "false":
                this.option = "false";
                break;
            case "nil":
                this.option = "nil";
                break;
            case "const_int":
                this.option = "const_int";
                break;
            case "const_double":
                this.option = "const_double";
                break;
            case "const_string":
                this.option =  "const_string";
                break;
            default:
                System.out.println("La opción ingresada no es correcta");
                break;
        }
        this.token = token;
    }

    /**
     * Metodo que chequea los tipos
     * @return Type
     */
    public Type check() throws SemanticASTException {
        Type type;
        if (option.equals("true") || option.equals("false")) {
            type = new Type(token, "Bool");
        }
        else {
            if (option.equals("nil")) {
                type = new Type(token, "nil");
            }
            else {
                if (option.equals("const_int")) {
                    type = new Type(token, "Int");
                }
                else {
                    if (option.equals("const_double")) {
                        type = new Type(token, "Double");
                    }
                    else {
                        if (option.equals("const_string")) {
                            type = new Type(token, "Str");
                        }
                        else {
                            throw new SemanticASTException(token, "Se esperaba Int, " +
                                    "Double, nil, Str o Bool. " +
                                    "Se encontró: " + option);
                        }
                    }
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

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
