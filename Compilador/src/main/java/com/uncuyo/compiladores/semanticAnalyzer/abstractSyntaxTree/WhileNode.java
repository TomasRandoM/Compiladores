package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el bucle While
 * Extiende {@link SentenceNode}
 */
public class WhileNode extends SentenceNode {

    /**
     * Nodo que representa la condición del while
     */
    private ExpressionNode expressionNode;
    /**
     * Nodo que representa la sentencia dentro del while
     */
    private SentenceNode sentenceNode;

    /**
     * String con el nombre de la clase que contiene al while
     */
    private String className;
    /**
     * String con el nombre del metodo que contiene al while
     */
    private String methodName;
    /**
     * Token de la palabra reservada token
     */
    private Token token;

    public WhileNode(ExpressionNode expressionNode, SentenceNode sentenceNode, String className, String methodName, Token token) {
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
        this.className = className;
        this.methodName = methodName;
        this.token = token;
    }

    /**
     * Metodo que chequear que la expresion del while y la sentencia dentro
     **/
    @Override
    public void check() throws SemanticASTException {
        Type expressionType = expressionNode.check();

        if (!expressionType.getName().equals("Bool")) {
            throw new SemanticASTException(expressionNode.getToken(), "La condición del " +
                    "bucle While debe devolver " +
                    "un booleano (true o false). Se encontró " +
                    expressionType.getName() + ".");
        }

        sentenceNode.check();
    }

    /**
     * Generacion de codigo del while
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#WHILE\n");
        String name = "while_" + methodName + className + token.getRow() + "_" + token.getColumn();
        String endName = "endWhile_" + methodName + className + token.getRow() + "_" + token.getColumn();
        string.append(name).append(":\n");
        string.append("#CODE GEN DE LA EXPRESION\n");
        expressionNode.codeGen(string);
        string.append("#CONTINUA WHILE\n");
        checkChained(string, expressionNode);
        if (expressionNode instanceof ArrayAccessNode || expressionNode instanceof VariableNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (expressionNode.nodeType.getName().equals("Double")) {
                //No debería llegarse a este caso, pero se mantiene por coherencia
                string.append("l.d $f0, 0($a0) \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
        string.append("beq $a0, $zero, ").append(endName).append("\n");
        string.append("#CODE GEN DE LA SENTENCIA\n");
        sentenceNode.codeGen(string);
        string.append("j ").append(name).append("\n");
        string.append(endName).append(":\n");
    }

    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            if (expressionNode.nodeType.getName().equals("Double")) {
                string.append("l.d $f0, 0($a0) \n");
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
}
