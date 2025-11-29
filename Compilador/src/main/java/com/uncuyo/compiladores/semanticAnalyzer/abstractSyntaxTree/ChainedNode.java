package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Clase abstracta que engloba a los encadenados.
 * Extiende {@link OperandNode}
 */
public abstract class ChainedNode extends OperandNode {
    /**
     * Nodo encadenado si hubiese
     */
    protected ChainedNode chainedNode;

    public abstract ChainedNode getChainedNode();

    public abstract void checkNames(String lastClass);

}
