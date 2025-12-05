package com.uncuyo.compiladores.exceptions;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Exception de la generación de código
 * @version 1.0.0
 * @author Tomás Rando
 */
public class CodeGenerationException extends Exception {
    /**
     * Constructor
     * @param message String con el error producido
     */
    public CodeGenerationException(Token lookahead, String message) {
        super("ERROR: Generación de código" + '\n' + "| NUMERO DE LINEA (NUMERO DE COLUMNA) " +
                "| DESCRIPCION: |" + '\n' + "| LINEA " +
                lookahead.getRow() + " (COLUMNA " +
                lookahead.getColumn() + ") " + "| " +
                message + " |" + '\n');
    }
}