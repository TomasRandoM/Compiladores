package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase abstracta que engloba a los encadenados.
 * Extiende {@link OperandNode}
 */
public abstract class ChainedNode extends OperandNode {
    /**
     * Nodo encadenado si hubiese
     */
    protected ChainedNode chainedNode;
    protected Type parentType;

    public abstract ChainedNode getChainedNode();

    public abstract Type checkNames(Type lastType) throws SemanticASTException;

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public ChainedNode getLastChainedNode() {
        if (chainedNode != null) {
            return chainedNode.getLastChainedNode();
        }
        else {
            return this;
        }
    }

    public Type getParentType() {
        return parentType;
    }

    public void setParentType(Type parentType) {
        this.parentType = parentType;
    }
}
