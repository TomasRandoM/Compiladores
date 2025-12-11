package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Metodo que representa una expresion parentizada
 * Extiende {@link ExpressionNode}
 */
public class ParenthesizedExpressionNode extends ExpressionNode {

    /**
     * Representa la expresion
     */
    private ExpressionNode expressionNode;
    /**
     * Representa el encadenamiento
     */
    private ChainedNode chainedNode;

    public ParenthesizedExpressionNode() {
    }

    public Token getToken() throws SemanticASTException {
        return expressionNode.getToken();
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Metodo para chequear el tipo de la expresion
     * @return
     */
    public Type check() throws SemanticASTException {
        Type type;
        if (expressionNode instanceof ChainedNode) {
            type = ((ChainedNode) this.expressionNode).checkNames(null);
        }
        else {
            type = this.expressionNode.check();
        }
        if (this.chainedNode != null) {
            type = this.chainedNode.checkNames(type);
        }

        this.nodeType = type;
        return type;
    }

    public void codeGen(StringBuilder string) {
        expressionNode.codeGen(string);
        checkChained(string, expressionNode);
        if (expressionNode instanceof ArrayAccessNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (expressionNode.nodeType.getName().equals("Double")) {
                string.append("l.d $f0, 0($a0) \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }
    }

    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            if (!isClassOrArray(expressionNode.nodeType.getName())) {
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0, 0($a0) \n");
                }
                else {
                    string.append("lw $a0, 0($a0) \n");
                }
            }
        }
    }

    public boolean isClassOrArray(String type) {
        if (type.equals("Int") ||
                type.equals("void") ||
                type.equals("Bool") ||
                type.equals("Str") ||
                type.equals("Char") ||
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
        }
    }

    public ChainedNode getLastChainedNode() {
        if (chainedNode != null) {
            return chainedNode.getLastChainedNode();
        }
        else {
            return expressionNode.getLastChainedNode();
        }
    }

}
