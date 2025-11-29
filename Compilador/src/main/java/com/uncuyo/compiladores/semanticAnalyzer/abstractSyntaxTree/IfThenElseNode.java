package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

/**
 * Nodo que representa un if else
 * Extiende {@link SentenceNode}
 */
public class IfThenElseNode extends SentenceNode {
    /**
     * Representa la expresion que contiene
     */
    private ExpressionNode expressionNode;
    /**
     * Representa la sentencia del flujo normal
     */
    private SentenceNode sentenceNode;
    /**
     * Representa la sentencia del else, puede ser null
     */
    private SentenceNode elseSentenceNode;

    /**
     * Constructor de IfThenElseNode
     * @param expressionNode ExpressionNode
     * @param sentenceNode SentenceNode
     * @param elseSentenceNode SentenceNode
     */
    public IfThenElseNode(ExpressionNode expressionNode, SentenceNode sentenceNode, SentenceNode elseSentenceNode) {
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
        this.elseSentenceNode = elseSentenceNode;
    }

    /**
     * Chequea la semantica
     */
    public void check() {
        //Pendiente
    }
}
