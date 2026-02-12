package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;

import java.util.*;

public class Class {

    private String name;
    private Token token;
    private String parentClass = "Object";
    private List<String> inheritedClasses = new ArrayList<>(List.of("Object"));
    private Map<String, Attribute> attributes = new LinkedHashMap<>();
    private Map<String, Method> methods = new LinkedHashMap<>();
    private Constructor constructor;
    private boolean implInitialized;
    private boolean classInitialized;
    private Token implToken;
    private Token classToken;

    public Class(Token token) {
        this.name = token.getLexeme();
        this.token = token;
    }

    public Class(Token token, String name) {
        this.name = name;
        this.token = token;
    }

    public void resolveInheritance() throws SemanticException {

        LinkedHashMap<String, Method> orderedMethods = new LinkedHashMap<>();
        LinkedHashMap<String, Attribute> orderedAttributes = new LinkedHashMap<>();

        String parent = this.getParentClass();

        // primero voy al padre
        if (!parent.equals("Object")) {
            Class parentClass = SymbolTable.getClass(parent);

            if (parentClass == null) {
                throw new SemanticException(this.getToken(),
                        "La clase: " + this.getName() + " hereda de una clase no declarada: " + parent);
            }

            //llamo recursivamente hasta que parent == null
            parentClass.resolveInheritance();

            // pongo los metodos y atributos en orden
            //orderedAttributes.putAll(parentClass.attributes);
            for (Attribute attribute : parentClass.attributes.values()) {
                orderedAttributes.put(attribute.getName(), attribute);
                /*
                if (attribute.getIsPublic()) {
                    orderedAttributes.put(attribute.getName(), attribute);
                }
                 */
            }
            orderedMethods.putAll(parentClass.methods);
        }

        // agrego los propios de la clase al final
        for (Attribute a : this.getAttributes().values()) {
            orderedAttributes.put(a.getName(), a);
        }
        for (Method m : this.getMethods().values()) {
            // se se redefine un metodo del padre lo pisa
            orderedMethods.put(m.getName(), m);
        }

        // lo actualizamos
        this.attributes = orderedAttributes;
        this.methods = orderedMethods;

    }

    public void addInheritedClass(String inheritedClass) {
        this.inheritedClasses.add(inheritedClass);
    }
    public Token getImplToken() {
        return implToken;
    }
    public void setImplToken(Token implToken) {
        this.implToken = implToken;
    }
    public boolean isInheritedClass(String className) {
        return this.inheritedClasses.contains(className);
    }

    public List<String> getInheritedClasses() {
        return inheritedClasses;
    }

    public void setInheritedClasses(List<String> inheritedClasses) {
        this.inheritedClasses = inheritedClasses;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }

    public Token getClassToken() {
        return classToken;
    }

    public void setClassToken(Token classToken) {
        this.classToken = classToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }



    /**
     * Agrega un atributo al hashmap
     * @param attribute
     * @throws SemanticException Excepcion si el atributo de clase ya ha sido definido en el ambito
     */
    public void addAttributes(Attribute attribute) throws SemanticException {
        if (attributes.containsKey(attribute.getName())) {
            throw new SemanticException(attribute.getToken(),
                    "El atributo de clase " + attribute.getName() + " " +
                            "ya fue definido en este ámbito");
        }
        attributes.put(attribute.getName(), attribute);
    }

    /**
     * Agrega un metodo al hashmap
     * @param method
     * @throws SemanticException Excepcion si el metodo ya ha sido definido en el ambito
     */
    public void addMethods(Method method) throws SemanticException {
        if (methods.containsKey(method.getName())) {
            throw new SemanticException(method.getToken(),
                    "El método " + method.getName() + " " +
                            "ya fue definido en este ámbito");
        }
        methods.put(method.getName(), method);
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setClassInitialized(boolean classInitialized) {
        this.classInitialized = classInitialized;
    }

    public boolean isClassInitialized() {
        return classInitialized;
    }

    public boolean isImplInitialized() {
        return implInitialized;
    }

    public void setImplInitialized(boolean implInitialized) {
        this.implInitialized = implInitialized;
    }

    public void setConstructor(Constructor constructor) throws SemanticException {
        if (this.constructor == null) {
            this.constructor = constructor;
        }
        else {
            throw new SemanticException(constructor.getToken(), "Una clase no puede tener más de un constructor");
        }
    }

    /**
     * Calcula el offset del atributo en la clase. Lo recorre obligatoriamente
     * porque el double ocupa el doble de espacio. Devuelve un offset positivo teniendo
     * en cuenta la estructura del CIR (Class instant record)
     * @param attribute String con el identificador del atributo
     * @return int offset
     */
    public int getAttributeOffset(String attribute) {
        int memory = 4;
        for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
            if (entry.getKey().equals(attribute)) {
                return memory;
            }
            else {
                if (entry.getValue().getType().getName().equals("Double")) {
                    memory += 8;
                }
                else {
                    memory += 4;
                }
            }
        }
        //No se debería llegar a este return, si se llega falló el semántico
        return memory;
    }

    /**
     * Metodo para obtener el offset de uno particular en la VTable
     * @param method String con el identificador del metodo
     * @return Int con el offset dentro de la vtable
     */
    public int getMethodOffset(String method) {
        int memory = 0;
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            if (entry.getKey().equals(method)) {
                return memory;
            }
            else {
                memory += 4;
            }
        }
        //No se debería llegar a este return, si se llega falló el semántico
        return memory;
    }

    public int calculateAttributeMemory() {
        int memory = 0;
        for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
            if (entry.getValue().getType().getName().equals("Double")) {
                memory += 8;
            }
            else {
                memory += 4;
            }
        }
        return memory;
    }
}
