package com.uncuyo.compiladores.exceptions;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.utils.Addons;

/**
 * Exception del analizador sintáctico
 * @version 1.0.0
 * @author Tomás Rando
 */
public class SyntacticException extends Exception {
    /**
     * Constructor
     * @param message String con el error producido
     */
    public SyntacticException(Token lookahead, String message) {
        super("ERROR: SINTACTICO" + '\n' + "| NUMERO DE LINEA (NUMERO DE COLUMNA) " +
                "| DESCRIPCION: |" + '\n' + "| LINEA " +
                lookahead.getRow() + " (COLUMNA " +
                lookahead.getColumn() + ") " + "| " +
                Addons.removeAccents(message) + " |" + '\n');
    }
}