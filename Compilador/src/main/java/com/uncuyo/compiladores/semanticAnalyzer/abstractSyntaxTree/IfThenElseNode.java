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
     * Representa el token de la palabra reservada if, sirve para guardar la linea
     */
    private Token token;
    /**
     * Representa la sentencia del flujo normal
     */
    private SentenceNode sentenceNode;
    /**
     * Representa la sentencia del else, puede ser null
     */
    private SentenceNode elseSentenceNode;
}
