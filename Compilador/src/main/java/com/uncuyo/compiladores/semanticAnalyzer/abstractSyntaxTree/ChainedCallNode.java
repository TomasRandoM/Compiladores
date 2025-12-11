package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una llamada encadenada.
 * Extiende {@link ChainedNode}
 */
public class ChainedCallNode extends ChainedNode {
    /**
     * Token que representa el nombre del metodo llamado
     */
    private Token name;

    /**
     * Lista de parametros (expresiones)
     */
    private List<ExpressionNode> parameterList = new ArrayList<>();

    public Token getToken() {
        return name;
    }

    /**
     * Generacion de codigo para llamadas encadenadas
     * @param string
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#ChainedCallNode \n");
        //en a0 tenemos el objeto que nos dejo ChainedAccessNode (u otro).
        Class class1 = SymbolTable.getClass(parentType.getName());
        int offset = class1.getMethodOffset(name.getLexeme());
        string.append("sw $fp, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        string.append("#Guardamos el self en la pila. Es el que venia del anterior encadenado \n");
        string.append("beq $a0, $zero, variableNotInitialized \n");
        string.append("sw $a0, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        int memory = codeGenParameters(string, 8);
        string.append("#Cargamos el self en a0 \n");
        string.append("lw $a0, ").append(memory - 4).append("($sp) \n");
        string.append("#Cargamos la direccion de la vtable de self en a0 \n");
        string.append("lw $a0, 0($a0) \n");
        string.append("#Buscamos la direccion del metodo (usando el offset) \n");
        string.append("addiu $a0, $a0, ").append(offset).append("\n");
        string.append("#Cargamos la direccion del metodo en el a0\n");
        string.append("lw $a0, 0($a0)\n");
        string.append("#Saltamos al metodo y el retorno lo traemos en a0");
        string.append("jalr $a0 \n");
        string.append("addiu $sp $sp ").append(memory).append("\n");
        string.append("lw fp 0($sp) \n");
        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }

    }

    /**
     * Carga los parametros del metodo actual en la pila
     * @param string StringBuilder
     * @param memory int con el espacio en la pila utilizado hasta el momento
     * @return int con la memoria utilizada en la pila
     */
    public int codeGenParameters(StringBuilder string, int memory) {
        string.append("#Cargamos los parámetros a la pila \n");
        for (ExpressionNode expressionNode : parameterList.reversed()) {
            expressionNode.codeGen(string);
            checkChained(string, expressionNode);
            if (expressionNode instanceof ArrayAccessNode) {
                string.append("#Se obtiene el valor del array desde la direccion \n");
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0, 0($a0) \n");
                }
                else {
                    string.append("lw $a0, 0($a0) \n");
                }
            }

            if (expressionNode.nodeType.getName().equals("Double")) {
                string.append("s.d $f0, 0($sp) \n");
                string.append("addiu $sp $sp -8 \n");
                memory += 8;
            } else {
                string.append("sw $a0, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
                memory += 4;
            }
        }
        return memory;
    }


    /**
     * Este metodo se fija si el ultimo encadenado es un ChainedAccessNode
     * para asi cargar su valor (desde la direccion que retorna).
     * @param string StringBuilder
     * @param expressionNode ExpressionNode
     */
    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            if (!isClassOrArray(expressionNode.nodeType.getName())) {
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0 0($a0)");
                }
                else {
                    string.append("lw $a0 0($a0)");
                }
            }
        }
    }

    public boolean isClassOrArray(String type) {
        if (type.equals("Int") ||
                type.equals("void") ||
                type.equals("Bool") ||
                type.equals("Str") ||
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
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

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }

    /**
     * Constructor de ChainedCallNode
     * @param name Token
     */
    public ChainedCallNode(Token name) {
        this.name = name;
    }

    /**
     * Chequea la semantica
     * @return Type
     */
    public Type check() {
        //Pendiente
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
        //verifico que no sea void (no se puede en un encadenado)
        /*
        if (lastType.getName().equals("Array")) {
            throw new SemanticASTException(name, "No se permite " +
                    "que una variable de tipo Array tenga encadenamiento");
        }
        */
        if (lastType.getName().equals("void")){
            throw new SemanticASTException(name, "El tipo void no está permitido en un encadenamiento");
        }
        //busco la clase base
        Class baseClass = SymbolTable.getClass(lastType.getName());

        if (baseClass == null) {
            throw new SemanticASTException(name, "La clase " + lastType.getName() +
                    " no ha sido declarada en este contexto.");
        }
        //busco el metodo en esa clase
        Method method = baseClass.getMethods().get(name.getLexeme());

        if (method == null) {
            throw new SemanticASTException(name, "El método " + name.getLexeme() +
                    " no ha sido declarado en la clase " + baseClass.getName() + ".");
        }

        //chequeo que la cantidad de parámetros coincida
        if (method.getParameters().size() != parameterList.size()) {
            throw new SemanticASTException(name, "La cantidad de parámetros no es la correcta. Se requieren "
            + method.getParameters().size() + " parámetros.");
        }

        //chequeo que el tipo de los parámetros coincida
        int i = 0;
        for (Parameter correctParam: method.getParameters().values()) {
            Type correctType = correctParam.getType();
            ExpressionNode expressionNode = parameterList.get(i);
            Type actualType;
            if (expressionNode instanceof ChainedNode) {
                actualType = ((ChainedNode) expressionNode).checkNames(null);
            }
            else {
                actualType = expressionNode.check();
            }

            if (!correctType.getName().equals(actualType.getName())) {
                if (actualType.getName().equals("nil") && !(isClassOrArray(correctType.getName()))) {
                    throw new SemanticASTException(actualType.getToken(), "El parámetro " +
                            parameterList.get(i).getToken().getLexeme() +
                            " es de tipo incorrecto. Se obtuvo " +
                            actualType.getName() +
                            " y se esperaba " + correctType.getName());
                }
                if (actualType.getName().equals("void") || !(SymbolTable.getClass(actualType.getName()).isInheritedClass(correctType.getName()))) {
                    throw new SemanticASTException(actualType.getToken(), "El parámetro " +
                            parameterList.get(i).getToken().getLexeme() +
                            " es de tipo incorrecto. Se obtuvo " +
                            actualType.getName() +
                            " y se esperaba " + correctType.getName());
                    }
            }
            else {
                if (correctType.getName().equals("Array")) {
                    if (!correctType.getArrType().getName().equals(actualType.getArrType().getName())) {
                        throw new SemanticASTException(name, "El subtipo esperado del array es " + correctType.getArrType().getName() +
                                " pero se encontró el tipo " + actualType.getArrType().getName());
                    }
                }
            }
            i++;

        }

        //el tipo es el tipo de retorno del método actual

        finalType = method.getType();

        /*
        if (finalType.getName().equals("void")) {
            throw new SemanticASTException(name, "Un método con retorno void no puede ser encadenado. ");
        }
        */

        //verifico que la clase exista
        Class classType = SymbolTable.getClass(finalType.getName());

        if (classType == null && !finalType.getName().equals("void")) {
            throw new SemanticASTException(name, "El tipo " + finalType.getName() +
                    " no existe");
        }

        if (chainedNode != null) {
            finalType = chainedNode.checkNames(finalType);
        }
        this.nodeType = finalType;
        return finalType;
    }
}
