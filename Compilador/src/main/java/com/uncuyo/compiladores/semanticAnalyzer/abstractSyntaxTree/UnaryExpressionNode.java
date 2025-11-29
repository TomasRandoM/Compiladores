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
     * Metodo para chequear los tipos
     * @return
     */
    public Type check() throws SemanticASTException {
        Type auxType = expressionNode.check();
        Type type;
        if (auxType.getName().equals("Array")) {
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
                }
                else {
                    type = auxType;
                }
            }
            else {
                throw new SemanticASTException(auxType.getToken(), "Se esperaba tipo " +
                        "Int o Double. Se encontró: " +
                        auxType.getName());
            }
        }
        else {
            if (operator.getName() == TokenTypes.op_not) {
                if (auxType.getName().equals("Bool")) {
                    type = auxType;
                }
                else {
                    throw new SemanticASTException(auxType.getToken(), "Se esperaba tipo Bool. " +
                            "Se encontró: " + auxType.getName());
                }
            }
            else {
                throw new SemanticASTException(operator, "Operador incorrecto");
            }
        }
        type.setToken(operator);
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
}
