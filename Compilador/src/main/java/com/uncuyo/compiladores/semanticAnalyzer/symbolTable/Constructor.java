package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

/**
 * Clase correspondiente al constructor
 * @author Paulina Suden y Tomas Rando
 */
public class Constructor extends Method {

    public Constructor(Type type) {
        super(".", null, type, false);

    }
}

