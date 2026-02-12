package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.sql.SQLOutput;

/**
 * Clase que representa los nodos de asignación
 * Extiende {@link SentenceNode}
 */
public class AssignmentNode extends SentenceNode {
    /**
     * Nodo izquierdo de la asignacion
     */
    private ExpressionNode leftNode;
    /**
     * Nodo derecho de la asignacion
     */
    private ExpressionNode rightNode;

    /**
     * Constructor de AssignmentNode
     * @param leftNode ExpressionNode
     * @param rightNode ExpressionNode
     */
    public AssignmentNode(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    /**
     * Metodo para checkear semanticamente
     * Verifica que ambos lados de la asignación tengan el mismo tipo
     */
    public void check() throws SemanticASTException {
        Type leftType;
        if (leftNode instanceof ChainedAccessNode) {
            ChainedAccessNode auxNode = (ChainedAccessNode) leftNode;
            leftType = auxNode.checkNames(null);
        }
        else {
            if (leftNode instanceof SelfNode) {
                if (((SelfNode) leftNode).getChainedNode() == null) {
                    throw new SemanticASTException(leftNode.getToken(), "Self " +
                            "no puede ser asignado");
                }
            }
            leftType = leftNode.check();
        }

        Type rightType;
        if (rightNode instanceof ChainedAccessNode) {
            ChainedAccessNode auxNode = (ChainedAccessNode) rightNode;
            rightType = auxNode.checkNames(null);
        }
        else {
            rightType = rightNode.check();
        }

        if (!leftType.getName().equals(rightType.getName())) {
            if (!rightType.getName().equals("nil")) {
                if ((rightType.getName().equals("void")) || !(SymbolTable.getClass(rightType.getName()).isInheritedClass(leftType.getName()))) {
                    throw new SemanticASTException(rightNode.getToken(), "El tipo asignado " +
                            "es incorrecto. " +
                            "Se esperaba: " + leftType.getName() +
                            ". Se obtuvo: " + rightType.getName() + ".");
                }
            }
            else {
                if (!isClassOrArray(leftType.getName())) {
                    throw new SemanticASTException(rightNode.getToken(), "Se le asigna un nil " +
                            "a un tipo primitivo. " +
                            "Se esperaba: " + leftType.getName() +
                            ". Se obtuvo: nil.");
                }
            }
        }
        else {
            if (leftType.getName().equals("Array")) {
                if (!leftType.getArrType().getName().equals(rightType.getArrType().getName())) {
                    throw new SemanticASTException(rightNode.getToken(), "Se asigna un tipo de array incorrecto. Se " +
                            "esperaba: " + leftType.getArrType().getName() + " y se encontró: " +
                            rightType.getArrType().getName() + ".");
                }
            }
        }
    }

    /**
     * Generacion de codigo para la asignacion. Llama a codeGen de right, luego left y realiza la asignacion
     * en el espacio de memoria asignado
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#ASIGNACION \n");
        boolean isAttribute = true;
        rightNode.codeGen(string);
        checkChained(string, rightNode);
        if (rightNode instanceof ArrayAccessNode || rightNode instanceof VariableNode) {
            if (rightNode instanceof VariableNode) {
                isAttribute = ((VariableNode) rightNode).isAttribute;
            }
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (rightNode.nodeType.getName().equals("Double")) {
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
                if (!(rightNode instanceof ArrayAccessNode && ((ArrayAccessNode) rightNode).getLastChainedNode() != null)) {
                    string.append("lw $a0, 0($a0) \n");
                }
            }
        }
        if (rightNode.nodeType.getName().equals("Double")) {
            string.append("mfc1 $t0, $f0 \n");
            string.append("mfc1 $t1, $f1 \n");
            string.append("sw $t0, 0($sp)\n");
            string.append("addiu $sp, $sp, -4\n");
            string.append("sw $t1, 0($sp)\n");
            string.append("addiu $sp, $sp, -4\n");
        }
        else {
            string.append("sw $a0, 0($sp) \n");
            string.append("addiu $sp $sp -4 \n");
        }
        //esta en el $a0 la direccion de la variable
        leftNode.codeGen(string);
        isAttribute = true;
        if (leftNode instanceof VariableNode) {
            isAttribute = ((VariableNode) leftNode).isAttribute;
        }

        if (rightNode.nodeType.getName().equals("Double")) {
            string.append("#Se saca el double de la pila y se guarda en f0 \n");
            string.append("addiu $sp $sp 8 \n");
            string.append("lw $t0, 0($sp) \n");
            string.append("lw $t1, -4($sp) \n");
            string.append("mtc1 $t0, $f0 \n");
            string.append("mtc1 $t1, $f1 \n");
            string.append("#Se guarda el double del lado derecho en la direccion de a0 \n");
            if (isAttribute) {
                string.append("sw $t0, 0($a0) \n");
                string.append("sw $t1, 4($a0) \n");
            }
            else {
                string.append("sw $t0, 0($a0) \n");
                string.append("sw $t1, -4($a0) \n");
            }
        }
        else {
            string.append("addiu $sp $sp 4 \n");
            string.append("#Cargamos el valor del lado derecho \n");
            string.append("lw $t0, 0($sp) \n");
            string.append("#Se guarda lo del lado derecho en la direccion de a0 \n");
            string.append("sw $t0, 0($a0) \n");
        }

    }

    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
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

    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(ExpressionNode leftNode) {
        this.leftNode = leftNode;
    }

    public ExpressionNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(ExpressionNode rightNode) {
        this.rightNode = rightNode;
    }
}

