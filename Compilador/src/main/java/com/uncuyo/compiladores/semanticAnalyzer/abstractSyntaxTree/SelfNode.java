package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

public class SelfNode extends OperandNode {

    /**
     * Token que representa a self
     */
    private Token token;
    /**
     * String del nombre de la clase que representa self
     */
    private String className;
    /**
     * String con el nombre del metodo en el que se llama a self
     */
    private String methodName;
    /**
     * ChainedNode por si self tiene un encadenamiento
     */
    private ChainedNode chainedNode;

    public Token getToken() {
        return token;
    }

    /**
     *
     * @param string
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#Self \n");
        int selfOffset = SymbolTable.getClass(className).
                getMethods().get(methodName).getParameterMemory();
        string.append("lw $a0, ").append(selfOffset).append("($fp) \n");

        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Constructor de la clase SelfNode
     * @param token Token de la palabra reservada self
     * @param className String con el nombre de la clase
     * @param methodName String con el nombre del metodo desde donde es llamado self
     */
    public SelfNode(Token token, String className, String methodName) {
        this.token = token;
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Metodo para chequear el tipo de self
     * @return
     */
    @Override
    public Type check() throws SemanticASTException {
        Type type;
        Method method;
        if (methodName.equals("start")) {
            throw new SemanticASTException(token, "Self no puede " +
                    "ser referenciado dentro del punto de entrada start");
        }
        Class class1 = SymbolTable.getClass(className);
        if (methodName == null) {
            method = class1.getConstructor();
        }
        else {
            method = class1.getMethods().get(methodName);
        }

        if (method.isStaticMethod()) {
            throw new SemanticASTException(token, "self está siendo referenciado en un contexto estático");
        }
        if (chainedNode == null) {
            type = new Type(token, "class");
            type.setName(className);
        }
        else {
            Type typeAux = new Type(token, "class");
            typeAux.setName(className);
            type = chainedNode.checkNames(typeAux);

        }
        type.setToken(token);
        this.nodeType = type;
        return type;
    }


    public ChainedNode getLastChainedNode() {
        ChainedNode chainedNode1;
        if (chainedNode != null) {
            chainedNode1 = chainedNode.getLastChainedNode();
        }
        else {
            chainedNode1 = null;
        }
        return chainedNode1;
    }
}
