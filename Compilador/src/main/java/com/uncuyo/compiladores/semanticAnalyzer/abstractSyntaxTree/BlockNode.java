package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;

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
     * Nombre del
     * metodo al que pertenece
     */
    private String methodName;
    /**
     * Nombre de la clase a la que pertenece
     */
    private String className;

    public BlockNode(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public void addSentence(SentenceNode sentenceNode) {
        sentences.add(sentenceNode);
    }

    public List<SentenceNode> getSentences() {
        return sentences;
    }

    public void setSentences(List<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    public String getMethod() {
        return methodName;
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Metodo para chequear semanticamente
     */
    public void check() throws SemanticASTException {
        boolean isThereReturnNode = false;
        for (SentenceNode sentence : sentences) {
            if (sentence instanceof ReturnNode) {
                isThereReturnNode = true;
            }
            System.out.println("SENTENCIA BLOCK NODE: " + sentence.toString());
            sentence.check();
        }
        //Se verifica que el metodo posee un ret (si debe)
        if (methodName != null &&
                !methodName.equals("start") &&
                !SymbolTable.getClass(className).getMethods()
                    .get(methodName).getType().getName().equals("void") &&
                !isThereReturnNode
        ) {
            Method method = SymbolTable.getClass(className).getMethods().get(methodName);
            throw new SemanticASTException(method.getToken(), "El " +
                    "tipo de retorno del método " +
                    methodName + " no es " +
                    "void y el método no posee un ret");
        }
    }
}
