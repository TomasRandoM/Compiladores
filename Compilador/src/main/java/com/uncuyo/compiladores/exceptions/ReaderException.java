package com.uncuyo.compiladores.exceptions;

/**
 * Exception del reader del analizador léxico
 * @version 1.0.0
 */
public class ReaderException extends Exception {
    public ReaderException(String message) {
        super(message);
    }
}