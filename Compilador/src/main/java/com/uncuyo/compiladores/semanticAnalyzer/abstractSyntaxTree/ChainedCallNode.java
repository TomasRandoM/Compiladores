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

    @Override
    public void codeGen(StringBuilder string) {

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
