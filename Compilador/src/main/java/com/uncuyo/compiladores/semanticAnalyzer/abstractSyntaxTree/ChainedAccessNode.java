package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

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
     * Constructor de ChainedAccessNode
     * @param name Token del nombre de la variable accedida en el encadenamiento
     */
    public ChainedAccessNode(Token name) {
        this.name = name;
    }

    public Token getToken() {
        return name;
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
     * @param lastClass String con el tipo de la clase anterior o el tipo de retorno del anterior metodo
     */
    public Type checkNames(String lastClass) throws SemanticASTException {
        Type finalType;
        //Obtengo la clase
        Class baseClass = SymbolTable.getClass(lastClass);

        if (baseClass == null) {
            throw new SemanticASTException(name, "La clase " + lastClass +
                    " no ha sido declarada en este contexto.");
        }

        //Busco el atributo en esa clase
        Attribute attribute = baseClass.getAttributes().get(name.getLexeme());

        if (!attribute.getIsPublic()) {
            throw new SemanticASTException(name,
                    "El atributo '" + name.getLexeme() + "' no es público y " +
                            "no puede ser accedido desde esta clase.");
        }


        if (attribute == null) {
            throw new SemanticASTException(name, "El atributo " + name.getLexeme() +
                    " no ha sido declarado en la clase " + baseClass.getName());
        }

        //Obtengo el tipo del atributo
        finalType = attribute.getType();

        //verifico que el tipo exista
        Class classType = SymbolTable.getClass(finalType.getName());
        if (classType == null) {
            throw new SemanticASTException(name, "El tipo " + finalType.getName() +
                    " no existe");
        }

        //Verifico si hay más encadenados:
        if (this.chainedNode != null) {
            finalType = this.chainedNode.checkNames(finalType.getName());
        }

        return finalType;
    }

    @Override
    public Type checkNames(Type lastType) throws SemanticASTException {
        return null;
    }

}
