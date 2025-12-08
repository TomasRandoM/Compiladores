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
        this.nodeType = type;
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

    /**
     * Genera el codigo MIPS de los literales
     * @param string StringBuilder
     */
    public void codeGen(StringBuilder string) {
        String name;
        string.append("#Literales\n");
        switch (option) {
            case "nil":
                string.append("li $a0, 0\n");
                break;
            case "const_int":
                string.append("li $a0, ").append(token.getLexeme()).append("\n");
                break;
            case "true":
                string.append("li $a0, 1" + "\n");
                break;
            case "false":
                string.append("li $a0, 0" + "\n");
                break;
            case "const_double":
                name = "double" + token.getLexeme() +
                        "_" + token.getRow() + "_" + token.getColumn();
                string.append(".data \n").append(name).append(": .double ").
                        append(token.getLexeme()).append("\n");
                string.append(".text \n l.d $f0, ").append(name).append("\n");
                break;
            case "const_string":
                name = "string_"+ token.getRow() + "_" + token.getColumn();
                string.append(".data\n");
                string.append(name).append(": .asciiz ").
                        append(token.getLexeme()).append("\n");
                string.append((".text\n"));
                string.append("la $a0, ").append(name).append("\n");
                break;
        }
    }
}
