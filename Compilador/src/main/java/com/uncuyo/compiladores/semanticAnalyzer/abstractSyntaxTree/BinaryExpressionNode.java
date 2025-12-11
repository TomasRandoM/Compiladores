package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.CodeGenerationException;
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
     * Metodo para chequear que ambos lados de la expresion binaria coincidan en el tipo
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
                if (!(rightNode.getName().equals("Array") && (right instanceof VariableNode || chained))) {
                    throw new SemanticASTException(left.getToken(), "Se " +
                            "esperaba un Int o Double. Se encontró Array");
                }
                rightNode = rightNode.getArrType();
            }
            leftNode = leftNode.getArrType();
        }
        else {
            if (rightNode.getName().equals("Array")) {
                if (left instanceof VariableNode || chained) {
                    throw new SemanticASTException(right.getToken(), "Se " +
                            "esperaba un Int o Double. Se encontró Array");
                }
                rightNode = rightNode.getArrType();
            }
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
        this.nodeType = type;
        return type;
    }

    public Token getToken() {
        return operator;
    }

    /**
     * Genera el codigo MIPS para las expresiones binarias.
     * Genera el codigo del lado izquierdo, luego el lado derecho. Posteriormente, hace la operacion
     * correspondiente y guarda el resultado en a0 o f0 dependiendo de si es double o no
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        boolean leftIsDouble = false;
        boolean rightIsDouble = false;
        left.codeGen(string);
        checkChained(string, left);
        if (left instanceof ArrayAccessNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (left.nodeType.getName().equals("Double")) {
                string.append("l.d $f0, 0($a0) \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
        if (left.nodeType.getName().equals("Double")) {
            string.append("#Left es double\n");
            string.append("s.d $f0, 0($sp) \n");
            string.append("addiu $sp $sp -8 \n");
            leftIsDouble = true;
        }
        else {
            string.append("sw $a0, 0($sp) \n");
            string.append("addiu $sp $sp -4 \n");
        }
        right.codeGen(string);
        checkChained(string, right);
        if (right instanceof ArrayAccessNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (right.nodeType.getName().equals("Double")) {
                string.append("l.d $f0, 0($a0) \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }
        if (right.nodeType.getName().equals("Double")) {
            rightIsDouble = true;
            if (!leftIsDouble) {
                string.append("#Convertimos el valor de la izquierda en double y \n");
                string.append("#queda guardado en f2 \n");
                string.append("lw $a0, 4($sp) \n");
                string.append("addiu $sp $sp 4\n");
                string.append("mtc1 $a0, $f2\n");
                string.append("cvt.d.w $f2, $f2\n");
                //Ahora tenemos el left en f2 y el right en f0
            }
            else {
                string.append("#Ambos son double asi que no se hace conversion \n");
                string.append("#Se saca de la pila el primer valor y se guarda en f2 \n");
                string.append("El left queda en f2 y el right en f0 \n");
                string.append("l.d $f2, 8($sp) \n");
                string.append("addiu $sp $sp 8\n");
                //left en f2 y right en f0
            }
        }
        else {
            if (leftIsDouble) {
                string.append("#Convertimos right a double \n");
                string.append("l.d $f2, 8($sp) \n");
                string.append("addiu $sp $sp 8\n");
                string.append("mtc1 $a0, $f0\n");
                string.append("#queda guardado en f0 \n");
                string.append("cvt.d.w $f0, $f0\n");
                //left en f2 y right en f0
            }
            else {
                string.append("#Ni left ni right son double\n");
                string.append("#El lado izquierdo queda en el t0 y el lado derecho en el a0 \n");
                string.append("lw $t0, 4($sp) \n");
                string.append("addiu $sp $sp 4\n");
                //right en a0 y left en t0
            }
        }

        //si son int --> left $t0, right $a0
        //si uno o los dos son double: left $f2, right $f0

        if (leftIsDouble || rightIsDouble) {
            string.append("#Ambos lados son de tipo double (tras la posible conversion) \n");
            switch (operator.getName()) {
                case op_sum:
                    string.append("add.d $f0, $f0, $f2\n");
                    break;
                case op_sub:
                    string.append("sub.d $f0, $f2, $f0\n");
                    break;
                case op_div:
                    string.append("l.d $f8, zeroDouble \n");
                    string.append("c.eq.d $f0, $f8 \n");
                    string.append("bc1t divZeroException\n");
                    string.append("div.d $f0, $f2, $f0\n");
                    break;
                case op_mult:
                    string.append("mul.d $f0, $f2, $f0\n");
                    break;
                case op_mod:
                    string.append("l.d $f8, zeroDouble \n");
                    string.append("c.eq.d $f0, $f8 \n");
                    string.append("bc1t modZeroException\n");
                    //No hay operador implementado directamente por lo que se realiza este proceso:
                    // a - int(a/b) * b
                    //Divido left / right y lo guardo en f4
                    string.append("#Se aplica la formula a - int(a/b) * b para el modulo \n");
                    string.append("#a/b \n");
                    string.append("div.d $f4, $f2, $f0\n");
                    //Se convierte a entero (truncandolo) y se guarda en f6
                    string.append("#int(a/b) se guarda en f6 \n");
                    string.append("cvt.w.d $f6, $f4\n");
                    //Se convierte a double nuevamente
                    string.append("#int(a/b) se convierte en double nuevamente y se guarda en f6 \n");
                    string.append("cvt.d.w $f6, $f6\n");
                    //Multiplica el double anterior por right
                    string.append("#int(a/b) * b se guarda en f6 \n");
                    string.append("mul.d $f6, $f6, $f0\n");
                    //Resta el left menos todo lo demas
                    string.append("#a - int(a/b) * b se guarda en f0 \n");
                    string.append("$f0, $f2, $f6 \n");
                    break;

                // relacionales
                case op_rel_less:
                    // LEFT < RIGHT
                    string.append("jal lessDouble \n");
                    break;
                case op_rel_equal:
                    string.append("jal equalDouble \n");
                    break;
                case op_rel_notequal:
                    string.append("jal notEqualDouble \n");
                case op_rel_greaterequal:
                    string.append("jal greaterEqualDouble\n");
                    break;
                case op_rel_lessequal:
                    string.append("jal lessEqualDouble\n");
                    break;
                case op_rel_greater:
                    string.append("jal greaterDouble \n");
                    break;
                default:
                    System.out.println("Error de operador en la generación de código");
                    break;
            }
        }
        else {
            string.append("#Ningun tipo es double\n");
            switch (operator.getName()) {
                case op_sum:
                    string.append("add $a0, $t0, $a0\n");
                    break;
                case op_sub:
                    string.append("sub $a0, $t0, $a0\n");
                    break;
                case op_div:
                    string.append("beq $a0, $zero, divZeroException\n");
                    string.append("#Se convierten los tipos a double para la operacion de division \n");
                    string.append("mtc1 $t0, $f2\n");
                    string.append("mtc1 $a0, $f0\n");
                    string.append("cvt.d.w $f0, $f0\n");
                    string.append("cvt.d.w $f2, $f2\n");
                    string.append("div.d $f0, $f2, $f0\n");
                    break;
                case op_mult:
                    string.append("mul $a0, $t0, $a0\n");
                    break;
                case op_mod:
                    string.append("beq $a0, $zero, modZeroException\n");
                    //Guardamos el resto en a0
                    string.append("div $t0, $a0\n");
                    string.append("mfhi $a0\n");
                    break;
                case pdiv:
                    string.append("div $a0, $t0, $a0\n");
                    break;
                // relacionales
                case op_rel_less:
                    // LEFT < RIGHT
                    string.append("slt $a0, $t0, $a0\n");
                    break;
                case op_rel_equal:
                    string.append("seq $a0, $t0, $a0\n");
                    break;
                case op_rel_notequal:
                    string.append("sne $a0, $t0, $a0\n");
                case op_rel_greaterequal:
                    string.append("sge $a0, $t0, $a0\n");
                    break;
                case op_rel_lessequal:
                    string.append("sle $a0, $t0, $a0\n");
                    break;
                case op_rel_greater:
                    string.append("slt $a0, $a0, $t0\n");
                    break;
                case op_and:
                    string.append("and $a0, $t0, $a0\n");
                    break;
                case op_or:
                    string.append("or $a0, $t0, $a0\n");
                    break;
                default:
                    System.out.println("Error de operador en la generación de código");
                    break;
            }
        }
    }


    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            if (!isClassOrArray(expressionNode.nodeType.getName())) {
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0 0($a0)");
                }
                else {
                    string.append("lw $a0 0($a0)");
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
