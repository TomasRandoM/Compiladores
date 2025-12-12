package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

import java.sql.SQLOutput;

/**
 * Clase que representa el acceso encadenado.
 * Extiende {@link ChainedNode}
 */
public class ChainedAccessNode extends ChainedNode {
    /**
     * Token con el nombre de la variable a la cual es accedida
     */
    protected Token name;

    /**
     * Clase en la cual se encuentra el metodo que contiene el encadenamiento
     */
    protected String className;

    /**
     * Metodo en el cual se encuentra el encadenado
     */
    protected String methodName;

    /**
     * Constructor de ChainedAccessNode
     * @param name Token del nombre de la variable accedida en el encadenamiento
     */
    public ChainedAccessNode(Token name, String className, String methodName) {
        this.name = name;
        this.className = className;
        this.methodName = methodName;
    }

    public Token getToken() {
        return name;
    }

    @Override
    public void codeGen(StringBuilder string) {
        string.append("#CHAINED ACCESS NODE \n");
        if (getParentType() == null) {

            Class currentClass = null;
            if (this.className != null) {
                currentClass = SymbolTable.getClass(this.className);
            }
            Method currentMethod;
            if (this.methodName == null) {
                currentMethod = currentClass.getConstructor();
            } else {
                if (this.methodName.equals("start")) {
                    currentMethod = SymbolTable.getStartMethodStored();
                } else {
                    currentMethod = currentClass.getMethods().get(this.methodName);
                }
            }
            //Calculamos el offset, dependiendo de si está en el parámetro, variable o atributo.
            int offset = 0;
            boolean isAttribute = false;
            if (currentMethod.getParameters().get(name.getLexeme()) != null) {
                offset = currentMethod.getParameterOffset(name.getLexeme());
            } else {
                if (currentMethod.getVariables().get(name.getLexeme()) != null) {
                    offset = currentMethod.getVariableOffset(name.getLexeme());
                } else {
                    if ((currentClass != null) && (currentClass.getAttributes().get(name.getLexeme()) != null)) {
                        offset = currentClass.getAttributeOffset(name.getLexeme());
                        isAttribute = true;
                    }
                }
            }
            string.append("#Carga de variable \n");
            if (isAttribute) {
                int parameterSize = currentMethod.getParameterMemory();
                string.append("#Cargamos la direccion del atributo en a0 utilizando la \n");
                string.append("#cantidad de parametros para acceder a self, y de ahi al atributo\n");
                string.append("lw $a0, ").append(parameterSize).append("($fp)\n");
                //string.append("la $a0, ").append(offset).append("($a0) \n");
                string.append("addiu $a0 $a0 ").append(offset).append("\n");
            }
            else {
                string.append("#Cargamos la direccion de la variable o parametro en a0 utilizando el \n");
                string.append("#offset con el fp \n");
                string.append("addiu $a0 $fp ").append(offset).append("\n");
                //string.append("la $a0, ").append(offset).append("($fp) \n");
            }
            string.append("lw $a0 0($a0) \n");
        }
        else {
            //Tenemos en a0 el self del padre. Entonces, necesitariamos buscar en su CIR el atributo
            //accedido actualmente
            string.append("beq $a0, $zero, variableNotInitialized \n");
            Class currentClass = SymbolTable.getClass(getParentType().getName());
            int offset = currentClass.getAttributeOffset(name.getLexeme());
            Type attributeType = currentClass.getAttributes().get(name.getLexeme()).getType();
            string.append("#Cargamos la direccion del atributo en a0. Recordamos que en a0 venia el self anterior\n");
            string.append("lw $a0, ").append(offset).append("($a0) \n");
        }
        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }
    }

    public void setName(Token name) {
        this.name = name;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public Token getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Chequea la semántica
     * @return Type
     */
    public Type check() {
        return null;
    }

    /**
     * Metodo para manejar la resolucion de nombres. Recibe el nombre de la clase anterior. Se
     * fija en la tabla de simbolos si esa clase posee el metodo actual. Luego, el retorno de
     * ese metodo se convierte en el siguiente lastClass y se llama a chequear el siguiente
     * encadenamiento.
     * Ejemplo: Si tengo el caso A.b().c(). Al principio se llamara a chequear b() con el lastClass A.
     * @param lastType String con el tipo de la clase anterior o el tipo de retorno del anterior metodo
     */
    public Type checkNames(Type lastType) throws SemanticASTException {
        parentType = lastType;
        Type finalType;

        //primer acceso
        if (lastType == null) {
            finalType = getAccessChainedNodeVariableType(name);
            lastType = finalType;
        }
        else {
            if (lastType.getName().equals("Array")) {
                throw new SemanticASTException(name, "No se permite " +
                        "que una variable de tipo Array tenga encadenamiento");
            }

            //verifico que no sea void (no se puede en un encadenado)
            if (lastType.getName().equals("void")){
                throw new SemanticASTException(name, "El tipo void no está permitido en un encadenamiento");
            }

            //Obtengo la clase
            Class baseClass = SymbolTable.getClass(lastType.getName());

            if (baseClass == null) {
                throw new SemanticASTException(name, "La clase " + lastType.getName() +
                        " no ha sido declarada en este contexto.");
            }
            //Busco el atributo en esa clase
            Attribute attribute = baseClass.getAttributes().get(name.getLexeme());

            if (attribute == null) {
                throw new SemanticASTException(name, "El atributo " + name.getLexeme() +
                        " no ha sido declarado en la clase " + baseClass.getName());
            }

            if (!attribute.getIsPublic()) {
                if(!baseClass.getName().equals(getClassName())) {
                    if (getClassName() == null) {
                        throw new SemanticASTException(name, "No se puede acceder a un " +
                                "atributo privado (" + attribute.getName() +
                                ") desde el método start");
                    }
                    throw new SemanticASTException(name, "No se puede acceder a un " +
                            "atributo privado (" + attribute.getName() +
                            ") desde otra clase (" + baseClass.getName() + ").");
                }
            }

            /**
             if (!attribute.getIsPublic()) {
             throw new SemanticASTException(name,
             "El atributo '" + name.getLexeme() + "' no es público y " +
             "no puede ser accedido desde esta clase.");
             }
             **/

            //Obtengo el tipo del atributo
            finalType = attribute.getType();

            //verifico que el tipo exista
            Class classType = SymbolTable.getClass(finalType.getName());
            if (classType == null) {
                throw new SemanticASTException(name, "El tipo " + finalType.getName() +
                        " no existe");
            }

        }
        //Verifico si hay más encadenados:
        if (this.chainedNode != null) {
            finalType = this.chainedNode.checkNames(finalType);
        }
        this.nodeType = finalType;
        return finalType;
    }


    public Type getAccessChainedNodeVariableType(Token token) throws SemanticASTException {
        Method method;
        Type type;
        if (methodName == null) {
            if (className == null) {
                //Este error no debería aparecer
                throw new SemanticASTException(token, "El método y " +
                        "la clase actuales son nulos");
            }
            method = SymbolTable.getClass(className).getConstructor();
        }
        else {
            if (methodName.equals("start")) {
                method = SymbolTable.getStartMethodStored();
            }
            else {
                method = SymbolTable.getClass(className).getMethods().get(methodName);
            }
        }

        if (method.getParameters().get(token.getLexeme()) != null) {
            type = method.getParameters().get(token.getLexeme()).getType();
        }
        else {
            if (method.getVariables().get(token.getLexeme()) != null) {
                type =  method.getVariables().get(token.getLexeme()).getType();
            }
            else {
                if ((className != null) && (SymbolTable.getClass(className).getAttributes().get(token.getLexeme()) != null)) {
                    type = SymbolTable.getClass(className).getAttributes().get(token.getLexeme()).getType();
                }
                else {
                    throw new SemanticASTException(token, "La variable " +
                            token.getLexeme() + " no se encuentra " +
                            "debidamente inicializada en este ámbito");
                }
            }
        }
        type.setToken(token);
        return type;
    }
}
