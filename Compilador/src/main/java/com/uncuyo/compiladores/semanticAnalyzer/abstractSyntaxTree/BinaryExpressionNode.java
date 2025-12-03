package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

public class BinaryExpressionNode extends ExpressionNode {
    /**
     * Token que representa el operador y sirve para guardar la linea
     */
    Token operator;
    /**
     * Expresion de la izquierda
     */
    ExpressionNode left;
    /**
     * Expresion de la derecha
     */
    ExpressionNode right;

    /**
     * Constructor de BinaryExpressionNode
     * @param left ExpressionNode
     * @param right ExpressionNode
     * @param operator Token del operador
     */
    public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, Token operator) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    /**
     * Metodo para chequear la semantica
     * @return Type
     */
    @Override
    public Type check() throws SemanticASTException {
        Type type = null;
        Type leftNode;
        boolean chained = false;
        if (left instanceof ChainedNode) {
            leftNode = ((ChainedNode) left).checkNames(null);
            chained = true;
        }
        else {
            leftNode = left.check();
        }
        Type rightNode;
        if (right instanceof ChainedNode) {
            rightNode = ((ChainedNode) right).checkNames(null);
            chained = true;
        }
        else {
            rightNode = right.check();
        }

        //Cambio los nodos si son array
        if (leftNode.getName().equals("Array")) {
            if (left instanceof VariableNode || chained) {
                throw new SemanticASTException(left.getToken(), "Se " +
                        "esperaba un Int o Double. Se encontró Array");
            }
            leftNode = leftNode.getArrType();
        }
        if (rightNode.getName().equals("Array")) {
            if (left instanceof VariableNode || chained) {
                throw new SemanticASTException(right.getToken(), "Se " +
                        "esperaba un Int o Double. Se encontró Array");
            }
            rightNode = rightNode.getArrType();
        }

        // Caso +, -, * y %

        if (operator.getName() == TokenTypes.op_sum ||
            operator.getName() == TokenTypes.op_sub ||
            operator.getName() == TokenTypes.op_mult ||
            operator.getName() == TokenTypes.op_mod
        ) {
            if (leftNode.getName().equals(rightNode.getName())) {
                if (leftNode.getName().equals("Int") ||
                    leftNode.getName().equals("Double")) {
                    type = leftNode;
                }
                else {
                    throw new SemanticASTException(leftNode.getToken(), "Se esperaba Int o Double. " +
                            "Se encontró: " + leftNode.getName());
                }
            }
            else {
                if( (leftNode.getName().equals("Int") && rightNode.getName().equals("Double")
                || leftNode.getName().equals("Double") && rightNode.getName().equals("Int"))) {
                    if (leftNode.getName().equals("Double")) {
                        type = leftNode;
                    }
                    else {
                        type = rightNode;
                    }
                }
                else {
                    throw new SemanticASTException(operator, "Se esperaba Int o Double. " +
                            "Se encontró: " + leftNode.getName() + " y " + rightNode.getName());
                }
            }
        }
        else {
            // caso /
            if (operator.getName() == TokenTypes.op_div) {
                if (rightNode.getName().equals("Int") || rightNode.getName().equals("Double")) {
                    if (leftNode.getName().equals("Int") || leftNode.getName().equals("Double")) {
                            type = new Type(operator, "Double");
                    }
                    else {
                        throw new SemanticASTException(operator, "Se esperaban " +
                                "tipos Int o Double. " +
                                "Se encontró: " + leftNode.getName());
                    }
                }
                else {
                    throw new SemanticASTException(operator, "Se esperaba " +
                            "tipo Int o Double. " +
                            "Se encontró: " + rightNode.getName());
                }
            }
            // caso div
            else {
                if (operator.getName() == TokenTypes.pdiv) {
                    if (leftNode.getName().equals(rightNode.getName())) {
                        if (leftNode.getName().equals("Int")) {
                            type = leftNode;
                        }
                        else {
                            throw new SemanticASTException(operator, "Los tipos de los operandos " +
                                    "deben ser ambos Int. Se encontró: " + leftNode.getName());
                        }
                    }
                    else {
                        throw new SemanticASTException(operator, "Los tipos de los operandos " +
                                "deben ser ambos Int." +
                                " Se encontró: " + leftNode.getName() +
                                " y " + rightNode.getName());
                    }
                }
                else {
                    // Casos <, <=, >, >=
                    if (operator.getName() == TokenTypes.op_rel_greater ||
                        operator.getName() == TokenTypes.op_rel_greaterequal ||
                        operator.getName() == TokenTypes.op_rel_less ||
                        operator.getName() == TokenTypes.op_rel_lessequal
                    ) {
                        if (leftNode.getName().equals("Int") || leftNode.getName().equals("Double")) {
                            if (rightNode.getName().equals("Int") || rightNode.getName().equals("Double")) {
                                type = new Type(operator, "Bool");
                            }
                            else {
                                throw new SemanticASTException(operator, "Se esperaban " +
                                        "tipos Double o Int. Se encontró: "
                                        + rightNode.getName() + ".");
                           }
                        }
                        else {
                            throw new SemanticASTException(operator, "Se esperaban " +
                                    "tipos Double o Int. Se encontró: " +
                                    leftNode.getName() + ".");
                        }
                    }
                    else {
                        // Caso || y &&
                        if (operator.getName() == TokenTypes.op_and ||
                            operator.getName() == TokenTypes.op_or
                        ) {
                            if (leftNode.getName().equals("Bool") && rightNode.getName().equals("Bool")) {
                                type = leftNode;
                            }
                            else {
                                throw new SemanticASTException(operator, "Se esperaba que ambos" +
                                        " operadores fueran Bool." +
                                        " Se encontró: "  + leftNode.getName() +
                                        " y " + rightNode.getName());
                            }
                        }
                        else {
                            if (operator.getName() == TokenTypes.op_rel_equal ||
                                operator.getName() == TokenTypes.op_rel_notequal) {
                                if (leftNode.getName().equals(rightNode.getName()) ||
                                    leftNode.getName().equals("nil") ||
                                    rightNode.getName().equals("nil")
                                ) {
                                    type = new Type(operator, "Bool");
                                }
                                else {
                                    if ((leftNode.getName().equals("Int") &&
                                            rightNode.getName().equals("Double")) ||
                                            (rightNode.getName().equals("Int") &&
                                            leftNode.getName().equals("Double"))
                                    ) {
                                        type = new Type(operator, "Bool");
                                    }
                                    else {
                                        throw new SemanticASTException(operator, "Se esperaba que ambos" +
                                                " operadores fueran del mismo " +
                                                "tipo o tipo " +
                                                "Int y Double. Se encontró: " + leftNode.getName() +
                                                " y " + rightNode.getName());
                                    }
                                }
                            }
                            else {
                                throw new SemanticASTException(operator, "Operador no permitido: " + operator.getLexeme());
                            }
                        }
                    }
                }
            }
        }


        type.setToken(operator);
        return type;
    }

    public Token getToken() {
        return operator;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }
}
