package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
     * Representa la sentencia del flujo normal
     */
    private SentenceNode sentenceNode;
    /**
     * Representa la sentencia del else, puede ser null
     */
    private SentenceNode elseSentenceNode;
    /**
     * Nombre de la clase
     */
    private String className;
    /**
     * Nombre del metodo
     */
    private String methodName;
    /**
     * Token de la palabra reservada if
     */
    private Token token;

    /**
     * Constructor de IfThenElseNode
     * @param expressionNode ExpressionNode
     * @param sentenceNode SentenceNode
     * @param elseSentenceNode SentenceNode
     */
    public IfThenElseNode(ExpressionNode expressionNode, SentenceNode sentenceNode, SentenceNode elseSentenceNode, String className, String methodName, Token token) {
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
        this.elseSentenceNode = elseSentenceNode;
        this.className = className;
        this.methodName = methodName;
        this.token = token;
    }

    /**
     * Chequea que el tipo de la expresion sea Bool, y la sentencia del if y del else si hay.
     */
    public void check() throws SemanticASTException {
        Type expressionType = expressionNode.check();

        if (!expressionType.getName().equals("Bool")) {
            throw new SemanticASTException(expressionNode.getToken(), "La condición del if debe devolver " +
                    "un booleano (true o false). Se encontró " + expressionType.getName() + ".");
        }

        sentenceNode.check();

        if (elseSentenceNode != null) {
            elseSentenceNode.check();
        }
    }

    /**
     * Generacion de codigo de if
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#If \n");
        String name = "if_" + methodName + className + token.getRow() + token.getColumn();
        String elseName = "elseif_" + methodName + className + token.getRow() + token.getColumn();
        String endName = "endif_" + methodName + className + token.getRow() + token.getColumn();
        string.append(name).append(": \n");
        expressionNode.codeGen(string);
        checkChained(string, expressionNode);
        if (expressionNode instanceof ArrayAccessNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (expressionNode.nodeType.getName().equals("Double")) {
                //No debería llegarse a este caso, pero se mantiene por coherencia
                string.append("l.d $f0, 0($a0) \n");
            } else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
        string.append("#Verifica si la condicion es falsa. Si es falsa salta a la etiqueta else \n");
        string.append("beq $a0, $zero, ").append(elseName).append("\n");
        sentenceNode.codeGen(string);
        string.append("#Al terminar salta a la etiqueta end del if \n");
        string.append("j ").append(endName).append(" \n");
        string.append("#Etiqueta del else. Si no hay else, esta vacia \n");
        string.append(elseName).append(": \n");
        if (elseSentenceNode != null) {
            elseSentenceNode.codeGen(string);
        }
        string.append(endName).append(": \n");

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
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
        }
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

    public SentenceNode getElseSentenceNode() {
        return elseSentenceNode;
    }

    public void setElseSentenceNode(SentenceNode elseSentenceNode) {
        this.elseSentenceNode = elseSentenceNode;
    }
}
