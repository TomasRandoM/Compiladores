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

    /**
     * Se guardan las clases
     */
    private static Map<String, Class> classes = new HashMap<>();
    /**
     * Variable para ir almacenando la clase actual. Se utiliza para ir guardando
     * los metodos y demas a la hora de ir conformando la tabla de simbolos
     */
    private static Class currentClass;
    /**
     * Variable para almacenar el metodo start. Se guarda aca debido a que
     * start no pertenece a ninguna clase, por lo que no se guardaria de otra manera
     */
    private static Method startMethodStored;
    /**
     * Variable para ir almacenando el metodo actual. Se utiliza para ir guardando
     * las variables y demas a la hora de ir conformando la tabla de simbolos
     */
    private static Method currentMethod;

    /**
     * Agrega una clase a la tabla de símbolos.
     * @throws SemanticException si la clase ya estaba declarada
     */
    public static Class addClass(Class c, String option) throws SemanticException {
        if (classes.containsKey(c.getName())) {
            Class c1 = classes.get(c.getName());
            if (option.equals("class")) {
                if (c1.isClassInitialized()) {
                    throw new SemanticException(
                            c.getToken(),
                            "La clase '" + c.getName() + "' ya fue declarada."
                    );
                }
            }
            else {
                if (c1.isImplInitialized()) {
                    throw new SemanticException(
                            c.getToken(),
                            "La impl de la clase '" + c.getName() + "' ya fue declarada."
                    );
                }
            }
            return c1;
        }
        classes.put(c.getName(), c);
        return c;
    }

    /**
     * Obtiene una clase por nombre.
     * @return Class o null si no existe
     */
    public static Class getClass(String name) {
        return classes.get(name);
    }

    /**
     * Verifica si existe una clase
     */
    public static boolean existsClass(String name) {
        return classes.containsKey(name);
    }

    /**
     * Devuelve todas las clases
     */
    public static Map<String, Class> getClasses() {
        return classes;
    }

    /**
     * Getter de currentClass
     * @return Class
     */
    public static Class getCurrentClass() {
        return currentClass;
    }

    /**
     * Setter de currentClass
     * @param currentClass Class
     */
    public static void setCurrentClass(Class currentClass) {
        SymbolTable.currentClass = currentClass;
    }

    /**
     * Getter de currentMethod
     * @return Method
     */
    public static Method getCurrentMethod() {
        return currentMethod;
    }

    /**
     * Setter de currentMethod
     * @param currentMethod Method
     */
    public static void setCurrentMethod(Method currentMethod) {
        SymbolTable.currentMethod = currentMethod;
    }
}
