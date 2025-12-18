package com.uncuyo.compiladores.exceptions;

import com.uncuyo.compiladores.utils.Addons;

/**
 * Exception del writer
 * @version 1.0.0
 * @author Tomás Rando
 */
public class WriterException extends Exception {
    /**
     * Constructor
     * @param message String con el error producido
     */
    public WriterException(String message) {
        super("ERROR: WRITER" + '\n' + "| ERROR EN LA ESCRITURA DEL ARCHIVO: " +
                Addons.removeAccents(message) + " |" + '\n');
    }
}