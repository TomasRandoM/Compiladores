package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el bucle While
 * Extiende {@link SentenceNode}
 */
public class WhileNode extends SentenceNode {

    /**
     * Nodo que representa la condición del while
     */
    private ExpressionNode expressionNode;
    /**
     * Nodo que representa la sentencia dentro del while
     */
    private SentenceNode sentenceNode;

    public WhileNode(ExpressionNode expressionNode, SentenceNode sentenceNode) {
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
    }

    /**
     * Metodo que chequear que la expresion del while y la sentencia dentro
     **/
    @Override
    public void check() throws SemanticASTException {
        Type expressionType = expressionNode.check();

        if (!expressionType.getName().equals("Bool")) {
            throw new SemanticASTException(expressionNode.getToken(), "La condición del " +
                    "bucle While debe devolver " +
                    "un booleano (true o false). Se encontró " +
                    expressionType.getName() + ".");
        }

        sentenceNode.check();
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
}
