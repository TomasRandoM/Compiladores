package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
    Token name;
    /**
     * Nodo encadenado si hubiese
     */
    ChainedNode chainedNode;
    /**
     * Lista de parametros (expresiones)
     */
    List<ExpressionNode> parameterList = new ArrayList<>();

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
}
