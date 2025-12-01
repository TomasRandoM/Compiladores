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

    public abstract ChainedNode getChainedNode();

    public abstract Type checkNames(String lastClass) throws SemanticASTException;
    public abstract Type checkNames(Type lastType) throws SemanticASTException;


}
