package com.uncuyo.compiladores.exceptions;

/**
 * Exception del analizador léxico
 * @version 1.0.0
 * @author Tomás Rando
 */
public class LexicalException extends Exception {
    /**
     * Constructor
     * @param message String con el mensaje de la excepción
     * @param col int Con el número de columna donde se produjo la excepción
     * @param row int Con el número de fila donde se produjo la excepción
     */
    public LexicalException(String message, int col, int row) {
        super("ERROR: LEXICO" + '\n' + "| NUMERO DE LINEA (NUMERO DE COLUMNA) | DESCRIPCION: |" + '\n' + "| LINEA " + row + "(COLUMNA " + col + ") | " + message + " |");
    }
}
