package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase correspondiente al constructor
 * @author Paulina Suden y Tomas Rando
 */
public class Constructor extends Method {

    public Constructor(Type type, Token token) {
        super(".", token, type, false);

    }
}

