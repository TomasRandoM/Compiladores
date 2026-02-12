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
        boolean isAttribute = true;
        string.append("#EXPRESION PARENTIZADA\n");
        string.append("#CODE GEN DE LA EXPRESION\n");
        expressionNode.codeGen(string);
        string.append("#CONTINUA EXPRESION PARENTIZADA\n");
        checkChained(string, expressionNode);
        if (expressionNode instanceof ArrayAccessNode || expressionNode instanceof VariableNode) {
            if (expressionNode instanceof VariableNode) {
                isAttribute = ((VariableNode) expressionNode).isAttribute;
            }
            string.append("move $a3, $a0 \n");
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (expressionNode.nodeType.getName().equals("Double")) {
                if (isAttribute) {
                    string.append("lw $t0, 0($a0) \n");
                    string.append("lw $t1, 4($a0) \n");
                }
                else {
                    string.append("lw $t0, 0($a0) \n");
                    string.append("lw $t1, -4($a0) \n");
                }
                string.append("mtc1 $t0, $f0 \n");
                string.append("mtc1 $t1, $f1 \n");
            }
            else {
                if (!(expressionNode instanceof ArrayAccessNode && ((ArrayAccessNode) expressionNode).getLastChainedNode() != null)) {
                    string.append("lw $a0, 0($a0) \n");
                }
            }
        }
        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }
        string.append("#FIN EXPRESION PARENTIZADA\n");
    }

    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            string.append("move $a3, $a0 \n");
            if (expressionNode.nodeType.getName().equals("Double")) {
                string.append("lw $t0, 0($a0) \n");
                string.append("lw $t1, 4($a0) \n");
                string.append("mtc1 $t0, $f0 \n");
                string.append("mtc1 $t1, $f1 \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
    }

    public boolean isClassOrArray(String type) {
        if (type.equals("Int") ||
                type.equals("void") ||
                type.equals("Bool") ||
                type.equals("Str") ||
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

    public boolean isVariable() {
        if (chainedNode != null) {
            if (chainedNode.getLastChainedNode() instanceof ChainedCallNode) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            if (expressionNode instanceof BinaryExpressionNode ||
                expressionNode instanceof UnaryExpressionNode ||
                expressionNode instanceof LiteralNode ||
                expressionNode instanceof MethodCallNode) {
                return false;
            }
            else {
                if (expressionNode instanceof ParenthesizedExpressionNode) {
                    return ((ParenthesizedExpressionNode) expressionNode).isVariable();
                }
            }
        }
        if (expressionNode.getLastChainedNode() instanceof ChainedCallNode) {
            return false;
        }
        return true;
    }

}
