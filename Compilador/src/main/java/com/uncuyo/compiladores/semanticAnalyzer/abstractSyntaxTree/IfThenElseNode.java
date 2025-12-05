package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
     * Chequea que el tipo de la expresion sea Bool, y la sentencia del if y del else si hay.
     */
    public void check() throws SemanticASTException {
        Type expressionType = expressionNode.check();

        if (!expressionType.getName().equals("Bool")) {
            throw new SemanticASTException(expressionNode.getToken(), "La condición del if debe devolver " +
                    "un booleano (true o false). Se encontró " + expressionType.getName() + ".");
        }

        sentenceNode.check();

        if (elseSentenceNode != null) {
            elseSentenceNode.check();
        }
    }

    @Override
    public void codeGen(StringBuilder string) {

    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public SentenceNode getSentenceNode() {
        return sentenceNode;
    }

    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
    }

    public SentenceNode getElseSentenceNode() {
        return elseSentenceNode;
    }

    public void setElseSentenceNode(SentenceNode elseSentenceNode) {
        this.elseSentenceNode = elseSentenceNode;
    }
}
