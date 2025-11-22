package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabla de simbolos
 * Contiene todas las clases
 * @author Paulina Suden y Tomás Rando
 */
public class SymbolTable {

    private Map<String, Class> classes = new HashMap<>();

    /**
     * Agrega una clase a la tabla de símbolos.
     * @throws SemanticException si la clase ya estaba declarada
     */
    public void addClass(Class c) throws SemanticException {
        if (classes.containsKey(c.getName())) {
            throw new SemanticException(
                    c.getToken(),
                    "La clase '" + c.getName() + "' ya fue declarada."
            );
        }
        classes.put(c.getName(), c);
    }

    /**
     * Obtiene una clase por nombre.
     * @return Class o null si no existe
     */
    public Class getClass(String name) {
        return classes.get(name);
    }

    /**
     * Verifica si existe una clase
     */
    public boolean existsClass(String name) {
        return classes.containsKey(name);
    }

    /**
     * Devuelve todas las clases
     */
    public Map<String, Class> getClasses() {
        return classes;
    }
}
