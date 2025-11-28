package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo que representa un bloque
 * Extiende {@link SentenceNode}
 */
public class BlockNode extends SentenceNode {
    /**
     * Conjunto de sentencias del bloque
     */
    List<SentenceNode> sentences = new ArrayList<>();

    /**
     * Metodo para chequear semanticamente
     */

    public void check() {
     //Pendiente
    }
}
