package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
     * Nodo del siguiente encadenamiento si lo hubiera. Puede ser null
     */
    protected ChainedNode chainedNode;

    /**
     * Constructor de ChainedAccessNode
     * @param name Token del nombre de la variable accedida en el encadenamiento
     */
    public ChainedAccessNode(Token name) {
        this.name = name;
    }

    public Token getName() {
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


}
