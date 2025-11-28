package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

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
    public Type check() {
        return null;
    }
}
