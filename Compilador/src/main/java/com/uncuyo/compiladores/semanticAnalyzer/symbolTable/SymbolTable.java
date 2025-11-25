package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;

import java.util.*;

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
    
    public static void resetSymbolTable() {
        setCurrentClass(null);
        setStartMethodStored(null);
        setCurrentMethod(null);
        Map<String, Class> classes1 = new HashMap<>();
        setClasses(classes1);
    }
    
    public static void setClasses(Map<String, Class> classes2) {
        classes = classes2;
    }

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
                c1.setClassInitialized(true);
            }
            else {
                if (c1.isImplInitialized()) {
                    throw new SemanticException(
                            c.getToken(),
                            "La impl de la clase '" + c.getName() + "' ya fue declarada."
                    );
                }
                c1.setImplInitialized(true);
            }
            return c1;
        }

        if (option.equals("class")) {
            c.setClassInitialized(true);
        }
        else {
            c.setImplInitialized(true);
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

    public static void setStartMethodStored(Method start) {
        startMethodStored = start;
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
        checkTypes();
    }

    /**
     * Chequea que no exista herencia circular y que
     * las clases de las cuales otras heredan esten correctamente declaradas. Aprovechando que
     * se recorren todas las clases, tambien se verifica que estas posean un constructor definido y que no
     * hereden de alguna clase base prohibida (Array, Int, Str, Bool, Double)
     * @author Tomas Rando
     * @throws SemanticException Herencia circular, clase ancestra no declarada o constructor no definido
     */
    private static void checkCircularInheritanceAndCorrectClassDeclaration() throws SemanticException {
        for (Class class1 : classes.values()) {
            if (!checkPredefinedClasses(class1.getName())) {
                if (class1.getConstructor() == null) {
                    throw new SemanticException(class1.getToken(), "La " +
                            "clase: " + class1.getName() +
                            " no posee un constructor definido.");
                }
                if (!class1.isClassInitialized()) {
                    throw new SemanticException(class1.getToken(), "La declaración " +
                            "de la clase " + class1.getName() + " no está definida.");
                }
                if (!class1.isImplInitialized()) {
                    throw new SemanticException(class1.getToken(), "La implementación " +
                            "de la clase " + class1.getName() + " no está definida.");
                }

                String parent = class1.getParentClass();
                Class lastClass = class1;

                if (parent != null) {
                    if (parent.equals("Array") ||
                            parent.equals("Int") ||
                            parent.equals("Str") ||
                            parent.equals("Bool") ||
                            parent.equals("Double")
                    ) {
                        throw new SemanticException(class1.getToken(), "La clase: " +
                                class1.getName() + " hereda de la clase: " + parent);
                    }
                }

                Set<String> visitedParents = new HashSet<>(); //guardamos los padres ya visitados

                while (parent != null) {

                    if (visitedParents.contains(parent)) {
                        throw new SemanticException((class1.getToken()), "Herencia circular detectada. " +
                                "Ciclo en: "+ parent);
                    }

                    visitedParents.add(parent);

                    if (parent.equals(class1.getName())) {
                        throw new SemanticException(class1.getToken(), "Herencia circular " +
                                "encontrada en la clase: " + class1.getName());
                    }

                    Class auxClass = getClass(parent);

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
            if (!checkPredefinedClasses(class1.getName())) {
                methods = class1.getMethods();
                attributes = class1.getAttributes();
                parent = class1.getParentClass();
                parentClass = getClass(parent);
                while (parent != null) {
                    for (Method method : parentClass.getMethods().values()) {
                        if (methods.containsKey(method.getName())) {

                            checkRedefinedMethod(class1, methods.get(method.getName()), method, parentClass);
                        }
                    }

                    for (Attribute attribute : parentClass.getAttributes().values()) {
                        if (attributes.containsKey(attribute.getName())) {
                            Attribute a = attributes.get(attribute.getName());
                            throw new SemanticException(a.getToken(),
                                    "La clase: " + class1.getName() + " posee el atributo: " +
                                            a.getName() + " que ya fue definido en una " +
                                            "clase ancestro.");
                        }
                    }
                    parent = parentClass.getParentClass();
                    parentClass = getClass(parent);
                }
            }
        }
    }

    /**
     * Chequea que dos metodos tengan la misma cantidad de parametros y del mismo tipo. Ademas, verifica
     * que el retorno de los metodos sea el mismo y que el método no sea static.
     * @param class1 Class que posee el metodo que sobreescribe al ancestro
     * @param baseMethod Metodo que sobreescribe al ancestro
     * @param parentMethod Metodo que es sobreescrito
     * @throws SemanticException El metodo se encuentra mal redefinido
     */
    private static void checkRedefinedMethod(Class class1, Method baseMethod, Method parentMethod, Class class2) throws SemanticException {

        if (parentMethod.isStaticMethod()) {
            throw new SemanticException(baseMethod.getToken(),
                    "La clase " + class2.getName() +
                            " intenta redefinir el método de clase (static) " +
                            parentMethod.getName() + " de la clase " + class1.getName()
                    + ", lo cual no está permitido.");
        }

        if (baseMethod.isStaticMethod()) {
            throw new SemanticException(baseMethod.getToken(),
                    "La clase " + class2.getName() +
                            " intenta redefinir el método de clase " +
                            parentMethod.getName() + " de la clase " + class1.getName()
                            + " a un método estático, lo cual no está permitido.");
        }

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

    /**
     * Para cada clase verifica los tipos de sus atributos y sus metodos. Maneja el caso especial
     * del start individualmente debido a que no se encuentra dentro de una clase.
     * @throws SemanticException
     */
    private static void checkTypes() throws SemanticException {
        for (Class class1 : classes.values()) {
            if (!checkPredefinedClasses(class1.getName())) {
                checkAttributes(class1.getAttributes());
                checkMethods(class1.getMethods());
                checkConstructor(class1.getConstructor());
            }
        }
        checkVariables(startMethodStored.getVariables());
    }

    /**
     * Verifica si los tipos de las variables de instancia se encuentran declarados
     * @param attributes Map<String, Attribute>
     * @throws SemanticException Si el tipo del atributo no se encuentra declarado
     */
    private static void checkAttributes(Map<String,  Attribute> attributes) throws SemanticException {
        for (Attribute attribute : attributes.values()) {
            if (getClass(attribute.getType().getName()) == null) {
                throw new SemanticException(attribute.getType().getToken(),
                        "El tipo del atributo: " + attribute.getName() +
                                " no se encuentra declarado.");
            }
        }
    }

    /**
     * Verifica que los tipos de los retornos esten declarados o sean void.
     * @param methods Map<String, Method>
     * @throws SemanticException Si el tipo de retorno no se encuentra declarado
     */
    private static void checkMethods(Map<String, Method> methods) throws SemanticException {
        for (Method method : methods.values()) {
            if ((getClass(method.getType().getName()) == null) &&
                    (!method.getType().getName().equals("void"))
            ) {
                throw new SemanticException(method.getType().getToken(),
                        "El tipo de retorno del método: " + method.getName() +
                                " no se encuentra declarado.");
            }
            checkParameters(method.getParameters());
            checkVariables(method.getVariables());
        }
    }

    /**
     * Verifica los parametros y las variables del constructor
     * @param constructor Constructor
     * @throws SemanticException
     */
    private static void checkConstructor(Constructor constructor) throws SemanticException {
        checkParameters(constructor.getParameters());
        checkVariables(constructor.getVariables());
    }

    /**
     * Verifica que los tipos de los parametros sean correctos
     * @param parameters Map<String, Parameter>
     * @throws SemanticException Si el tipo de algun parametro no se encuentra declarado
     */
    private static void checkParameters(Map<String, Parameter> parameters) throws SemanticException {
        for (Parameter parameter : parameters.values()) {
            if (getClass(parameter.getType().getName()) == null) {
                throw new SemanticException(parameter.getType().getToken(),
                        "El tipo " + parameter.getType().getName() +
                                " del paramétro " + parameter.getName() +
                                " no se encuentra definido.");
            }
        }
    }

    /**
     * Verifica que los tipos de las variables locales esten declarados
     * @param variables Map<String, Variable>
     * @throws SemanticException Si el tipo de la variable no se encuentra declarado
     */
    private static void checkVariables(Map<String, Variable> variables) throws SemanticException {
        for (Variable variable : variables.values()) {
            if (getClass(variable.getType().getName()) == null) {
                throw new SemanticException(variable.getType().getToken(),
                        "El tipo de la variable: " + variable.getName() +
                                " no se encuentra declarado.");
            }
        }
    }

    /**
     * Agrega las clases predefinidas a la SymbolTable
     */
    public static void agregarClasesPredefinidas() {
        Class intClass = new Class(null, "Int");
        Class boolClass = new Class(null, "Bool");
        Class strClass = new Class(null, "Str");
        Class doubleClass = new Class(null, "Double");
        Class objectClass = new Class(null, "Object");
        Class IOClass = new Class(null, "IO");

        classes.put("Int", intClass);
        classes.put("Boolean",boolClass );
        classes.put("Str", strClass);
        classes.put("Double", doubleClass);
        classes.put("Object", objectClass);
        classes.put("IO", IOClass);
    }

    private static boolean checkPredefinedClasses(String name) {
        return (name.equals("Int") ||
                name.equals("Bool") ||
                name.equals("Str") ||
                name.equals("Double") ||
                name.equals("Object") ||
                name.equals("IO"));
    }
}
