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

    /**
     * Si es true se trata de un bloque de metodo, caso contrario, se trata de un
     * bloque de sentencias
     */
    private boolean methodBlock;

    public BlockNode(String className, String methodName, boolean methodBlock) {
        this.className = className;
        this.methodName = methodName;
        this.methodBlock = methodBlock;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isMethodBlock() {
        return methodBlock;
    }

    public void setMethodBlock(boolean methodBlock) {
        this.methodBlock = methodBlock;
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
        for (SentenceNode sentence : sentences) {
            sentence.check();
        }
        //Se verifica que el metodo posee un ret (si debe)
        if (methodName != null &&
                !methodName.equals("start") &&
                !SymbolTable.getClass(className).getMethods()
                    .get(methodName).getType().getName().equals("void") &&
                !AST.isReturnPresent() &&
                methodBlock
        ) {
            Method method = SymbolTable.getClass(className).getMethods().get(methodName);
            throw new SemanticASTException(method.getToken(), "El " +
                    "tipo de retorno del método " +
                    methodName + " no es " +
                    "void y el método no posee un ret");
        }
        //Reseteo el isReturnPresent del AST para el siguiente metodo
        if (methodBlock) {
            AST.setIsReturnPresent(false);
        }
    }

    @Override
    public void codeGen(StringBuilder string) {

    }
}
