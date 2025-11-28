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
