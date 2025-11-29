package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

public class SelfNode extends OperandNode {

    /**
     * Token que representa a self
     */
    private Token token;
    /**
     * String del nombre de la clase que representa self
     */
    private String classId;

    private ChainedNode chainedNode;

    public SelfNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Constructor de la clase SelfNode
     * @param token
     * @param classId
     */
    public SelfNode(Token token, String classId) {
        this.token = token;
        this.classId = classId;
    }

    /**
     * Metodo para chequear el tipo
     * @return
     */
    @Override
    public Type check() {
        return null;
    }
}
