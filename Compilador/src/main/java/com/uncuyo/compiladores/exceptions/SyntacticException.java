package com.uncuyo.compiladores.exceptions;

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
    public SyntacticException(String message) {
        super("ERROR: READER" + '\n' + "| ERROR EN LA LECTURA DEL ARCHIVO: " + message + " |" + '\n');
    }
}