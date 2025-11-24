package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * Llama y encadena los chequeos de declaraciones. Los chequeos podrian haber sido
     * incluidos todos en el mismo metodo, pero se separaron para una mayor
     * claridad
     * @throws SemanticException Errores de declaraciones
     */
    public static void checkDeclarations() throws SemanticException {
        checkCircularInheritanceAndCorrectClassDeclaration();
        checkRedefinedMethodsAndInheritedAttributes();
    }

    /**
     * Chequea que no exista herencia circular y que
     * las clases de las cuales otras heredan esten correctamente declaradas. Aprovechando que
     * se recorren todas las clases, tambien se verifica que estas posean un constructor definido
     * @author Tomas Rando
     * @throws SemanticException Herencia circular, clase ancestra no declarada o constructor no definido
     */
    private static void checkCircularInheritanceAndCorrectClassDeclaration() throws SemanticException {
        String parent;
        Class auxClass;
        Class lastClass;
        for (Class class1 : classes.values()) {
            if (class1.getConstructor() == null) {
                throw new SemanticException(class1.getToken(), "La " +
                        "clase: " + class1.getName() +
                        " no posee un constructor definido.");
            }
            parent = class1.getParentClass();
            lastClass = class1;
            while (parent != null) {
                if (parent.equals(class1.getName())) {
                    throw new SemanticException(class1.getToken(), "Herencia circular " +
                            "encontrada en la clase: " + class1.getName());
                }
                auxClass = getClass(parent);
                if (auxClass == null) {
                    throw new SemanticException(lastClass.getToken(), "La clase: " +
                            lastClass.getName() + " hereda " +
                            "de una clase que no fue declarada.");
                }
                lastClass = auxClass;
                parent = auxClass.getParentClass();
            }
        }
    }

    /**
     * Chequea redefiniciones de metodos en clases
     * que heredan y chequea que no se utilicen los
     * nombres de los atributos heredados
     * @author Tomas Rando
     * @throws SemanticException El atributo usa un nombre de un atributo heredado o metodo mal redefinido.
     */
    private static void checkRedefinedMethodsAndInheritedAttributes() throws SemanticException {
        Map<String, Method> methods;
        String parent;
        Class parentClass;
        Map<String, Attribute> attributes;
        for (Class class1 : classes.values()) {
            methods = class1.getMethods();
            attributes = class1.getAttributes();
            parent = class1.getParentClass();
            parentClass = getClass(parent);
            while (parent != null) {
                for  (Method method : parentClass.getMethods().values()) {
                    if (methods.containsKey(method.getName())) {
                        checkRedefinedMethod(class1, methods.get(method.getName()), method);
                    }
                }

                for (Attribute attribute : parentClass.getAttributes().values()) {
                    if (attributes.containsKey(attribute.getName())) {
                        Attribute a = attributes.get(attribute.getName());
                        throw new SemanticException(a.getToken(),
                                "La clase: " + class1 + " posee el atributo: " +
                                        a.getName() + " que ya fue definido en una " +
                                        "clase ancestro.");
                    }
                }
                parent = parentClass.getParentClass();
                parentClass = getClass(parent);
            }
        }
    }

    /**
     * Chequea que dos metodos tengan la misma cantidad de parametros y del mismo tipo. Ademas, verifica
     * que el retorno de los metodos sea el mismo.
     * @param class1 Class que posee el metodo que sobreescribe al ancestro
     * @param baseMethod Metodo que sobreescribe al ancestro
     * @param parentMethod Metodo que es sobreescrito
     * @throws SemanticException El metodo se encuentra mal redefinido
     */
    public static void checkRedefinedMethod(Class class1, Method baseMethod, Method parentMethod) throws SemanticException {

        if (!baseMethod.getType().getName().equals(parentMethod.getType().getName())) {
            throw new SemanticException(baseMethod.getToken(),
                    "La clase: " + class1.getName() + " redefine " +
                            "el método: " + baseMethod.getName() + " incorrectamente. " +
                            "El tipo de retorno no es el mismo.");
        }

        List<Parameter> baseParameters = new ArrayList<>(baseMethod.getParameters().values());
        List<Parameter> parentParameters = new ArrayList<>(parentMethod.getParameters().values());
        if (baseParameters.size() != parentParameters.size()) {
            throw new SemanticException(baseMethod.getToken(),
                    "La clase: " + class1.getName() + " redefine " +
                            "el método: " + baseMethod.getName() + " incorrectamente. " +
                            "La cantidad de parámetros no es la misma.");
        }

        for (int i = 0; i < baseParameters.size(); i++) {
            if (!baseParameters.get(i).getType().getName().equals(parentParameters.get(i).getType().getName())) {
                throw new SemanticException(baseMethod.getToken(), "La clase: " +
                        class1.getName() + " redefine " +
                        "el método: " + baseMethod.getName() + " incorrectamente. " +
                        "Los parámetros son de distinto tipo");
            }
        }
    }
}
