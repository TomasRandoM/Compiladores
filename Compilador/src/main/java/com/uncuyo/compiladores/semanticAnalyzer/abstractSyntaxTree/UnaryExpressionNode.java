package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Metodo que representa una expresion unaria
 * Extiende {@link ExpressionNode}
 */
public class UnaryExpressionNode extends ExpressionNode {

    /**
     * Representa la expresion
     */
    private ExpressionNode expressionNode;
    /**
     * Token que representa el operador y sirve para guardar la linea
     */
    Token operator;

    public UnaryExpressionNode(ExpressionNode expressionNode, Token operator) {
        this.expressionNode = expressionNode;
        this.operator = operator;
    }

    /**
     * Metodo para chequear el tipo de la expresion unaria
     *
     * @return
     */
    public Type check() throws SemanticASTException {
        Type auxType;
        Type type;
        boolean chained = false;
        if (expressionNode instanceof ChainedNode) {
            auxType = ((ChainedNode) expressionNode).checkNames(null);
            chained = true;
        } else {
            auxType = expressionNode.check();
        }

        if (auxType.getName().equals("Array")) {
            if (expressionNode instanceof VariableNode || chained) {
                if (operator.getName() == TokenTypes.op_not) {
                    throw new SemanticASTException(expressionNode.getToken(), "Se " +
                            "esperaba un Bool. Se encontró Array");
                }
                throw new SemanticASTException(expressionNode.getToken(), "Se " +
                        "esperaba un Int o Double. Se encontró Array");
            }
            auxType = auxType.getArrType();
        }

        if (operator.getName() == TokenTypes.op_increment ||
                operator.getName() == TokenTypes.op_decrement ||
                operator.getName() == TokenTypes.op_sub ||
                operator.getName() == TokenTypes.op_sum ||
                operator.getName() == TokenTypes.pint
        ) {
            if (auxType.getName().equals("Int") || auxType.getName().equals("Double")) {
                if (operator.getName() == TokenTypes.pint) {
                    type = new Type(operator, "Int");
                } else {
                    type = auxType;
                }
            } else {
                throw new SemanticASTException(auxType.getToken(), "Se esperaba tipo " +
                        "Int o Double. Se encontró: " +
                        auxType.getName());
            }
        } else {
            if (operator.getName() == TokenTypes.op_not) {
                if (auxType.getName().equals("Bool")) {
                    type = auxType;
                } else {
                    throw new SemanticASTException(auxType.getToken(), "Se esperaba tipo Bool. " +
                            "Se encontró: " + auxType.getName());
                }
            } else {
                throw new SemanticASTException(operator, "Operador incorrecto");
            }
        }
        type.setToken(operator);
        this.nodeType = type;
        return type;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public Token getToken() {
        return operator;
    }

    /**
     * Generacion de codigo para expresion unaria
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#Expresion unaria \n");
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
        switch (operator.getName()) {
            case op_decrement:
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f2, addOne \n");
                    string.append("sub.d $f0, $f0, $f2 \n");
                }
                else {
                    string.append("addi $a0, $a0, -1 \n");
                }
                break;
            case op_increment:
                if (expressionNode.nodeType.getName().equals("Int")) {
                    string.append("addi $a0, $a0, 1 \n");
                }
                else {
                    string.append("l.d $f2, addOne \n");
                    string.append("add.d $f0, $f0, $f2 \n");
                }
                break;

            case op_sum:
                break;
            case op_sub:
                if (expressionNode.nodeType.getName().equals("Int")) {
                    string.append("sub $a0, $zero, $a0 \n");
                }
                else {
                    //zeroDouble: .double 0.0
                    string.append("l.d $f2, zeroDouble \n");
                    string.append("sub.d $f0, $f2, $f0 \n");
                }
                    break;
            case pint:
                if (!expressionNode.nodeType.getName().equals("Int")) {
                    string.append("#Se castea a Int el Double y se deja en f0 \n");
                    //Transformo el double de f0 a entero y lo dejo en f2
                    string.append("cvt.w.d $f2, $f0 \n");
                    //Muevo el entero de f2 a a0
                    string.append("mfc1 $a0, $f2 \n");
                }
                break;
            case op_not:
                string.append("nor $a0, $a0, $zero");
                break;
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

    @Override
    public ChainedNode getLastChainedNode() {
        return null;
    }

}
