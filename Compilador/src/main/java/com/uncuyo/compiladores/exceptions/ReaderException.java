package com.uncuyo.compiladores.exceptions;

import com.uncuyo.compiladores.utils.Addons;

/**
 * Exception del reader del analizador léxico
 * @version 1.0.0
 * @author Tomás Rando
 */
public class ReaderException extends Exception {
    /**
     * Constructor
     * @param message String con el error producido
     */
    public ReaderException(String message) {
        super("ERROR: READER" + '\n' + "| ERROR EN LA LECTURA DEL ARCHIVO: " +
                Addons.removeAccents(message) + " |" + '\n');
    }
}