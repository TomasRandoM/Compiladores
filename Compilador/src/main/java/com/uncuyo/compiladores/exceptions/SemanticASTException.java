package com.uncuyo.compiladores.exceptions;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Exception del analizador semántico
 * @version 1.0.0
 * @author Tomás Rando
 */
public class SemanticASTException extends Exception {
    /**
     * Constructor
     * @param message String con el error producido
     */
    public SemanticASTException(Token lookahead, String message) {
        super("ERROR: SEMÁNTICO - DECLARACIONES" + '\n' + "| NUMERO DE LINEA (NUMERO DE COLUMNA) " +
                "| DESCRIPCION: |" + '\n' + "| LINEA " +
                lookahead.getRow() + " (COLUMNA " +
                lookahead.getColumn() + ") " + "| " +
                message + " |" + '\n');
    }
}