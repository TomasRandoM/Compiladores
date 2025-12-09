package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

/**
 * Clase que representa el acceso a un array
 * Extiende {@link OperandNode}
 */
public class ArrayAccessNode extends OperandNode{
    /**
     * Token que representa al array
     */
    private Token token;
    /**
     * Representa la expresion dentro del array
     */
    private ExpressionNode expressionNode;
    /**
     * Representa encadenamiento en un array
     */
    private ChainedNode chainedNode;
    /**
     * Representa la clase del metodo en donde esta el Array
     */
    private String className;
    /**
     * Representa el metodo donde esta el Array.
     */
    private String methodName;

    public ArrayAccessNode(Token token, String className, String methodName) {
        this.token = token;
        this.className = className;
        this.methodName = methodName;
    }

    public Token getToken() {
        return token;
    }

    /**
     * Generacion de codigo para el acceso a array encadenado. Cabe destacar que este no es el nodo utilizado
     * en una asignacion
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        Class currentClass = null;
        if (className != null) {
            currentClass = SymbolTable.getClass(className);
        }
        Method currentMethod;
        if (this.methodName == null) {
            currentMethod = currentClass.getConstructor();
        } else {
            if (this.methodName.equals("start")) {
                currentMethod = SymbolTable.getStartMethodStored();
            } else {
                currentMethod = currentClass.getMethods().get(this.methodName);
            }
        }
        //Calculamos el offset, dependiendo de si está en el parámetro, variable o atributo.
        Type arrayType = null;
        int offset = 0;
        boolean isAttribute = false;
        if (currentMethod.getParameters().get(token.getLexeme()) != null) {
            offset = currentMethod.getParameterOffset(token.getLexeme());
            arrayType = currentMethod.getParameters().get(token.getLexeme()).getType().getArrType();
        } else {
            if (currentMethod.getVariables().get(token.getLexeme()) != null) {
                offset = currentMethod.getVariableOffset(token.getLexeme());
                arrayType = currentMethod.getVariables().get(token.getLexeme()).getType().getArrType();
            } else {
                if ((currentClass != null) && (currentClass.getAttributes().get(token.getLexeme()) != null)) {
                    offset = currentClass.getAttributeOffset(token.getLexeme());
                    arrayType = currentClass.getAttributes().get(token.getLexeme()).getType().getArrType();
                    isAttribute = true;
                }
            }
        }
        string.append("#Carga de variable \n");
        if (isAttribute) {
            int parameterSize = currentMethod.getParameterMemory();
            string.append("#Cargamos el atributo en a0 utilizando la \n");
            string.append("#cantidad de parametros para acceder a self, y de ahi al atrubuto\n");
            string.append("lw $a0, ").append(parameterSize).append("($fp)\n");
            string.append("addiu $a0, $a0 ").append(offset).append("\n");
        }
        else {
            string.append("#Cargamos la variable en a0 utilizando el \n");
            string.append("offset con el fp \n");
            string.append("lw $a0, ").append(offset).append("($fp) \n");
        }

        string.append("#Guardamos a0 en la pila, que es la direccion del array\n");
        string.append("sw $a0, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
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
        string.append("#Restauramos la direccion en t0 que habiamos dejado en la pila \n");
        string.append("addiu $sp $sp 4 \n");
        string.append("lw $t0, 0($sp) \n");
        string.append("#Calculamos el offset usando la posicion (en a0) y\n");
        string.append("#el espacio que ocupan los elementos del array\n");
        if (arrayType.getName().equals("Double")) {
            string.append("li $t1, 8 \n");
        }
        else {
            string.append("li $t1, 4\n");
        }
        string.append("mul $a0, $a0, $t1 \n");
        string.append("#sumamos 8 debido a que el array posee vtable y la longitud del mismo \n");
        string.append("addiu $a0 $a0 8 \n");
        string.append("#le sumamos a la direccion del array el offset y obtenemos la direccion del elemento \n");
        string.append("addiu $t0 $t0 $a0 \n");
        string.append("move $a0, $t0 \n");
    }

    /**
     * Este metodo se fija si el ultimo encadenado es un ChainedAccessNode
     * para asi cargar su valor (desde la direccion que retorna).
     * @param string StringBuilder
     * @param expressionNode ExpressionNode
     */
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
        return chainedNode.getLastChainedNode();
    }

    public void setToken(Token token) {
        this.token = token;
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
     * Metodo que chequea el tipo del Array y de su indice
     * @return
     */
    public Type check() throws SemanticASTException {
        Class currentClass;
        Method currentMethod;

        // obtener la clase y el metodo actual
        if (methodName == null) {
            if (className == null) {
                //Este error no debería aparecer
                throw new SemanticASTException(token, "El método y " +
                        "la clase actuales son nulos");
            }
            currentClass = SymbolTable.getClass(className);
            currentMethod = currentClass.getConstructor();
        }
        else {
            if (methodName.equals("start")) {
                currentMethod = SymbolTable.getStartMethodStored();
            }
            else {
                currentMethod = SymbolTable.getClass(className).getMethods().get(methodName);
            }
        }

        Type arrayType;

        // buscar primero en parámetros
        if (currentMethod.getParameters().containsKey(token.getLexeme())) {
            arrayType = currentMethod.getParameters().get(token.getLexeme()).getType();
        }
        // buscar en variables locales
        else {
            if (currentMethod.getVariables().containsKey(token.getLexeme())) {
                arrayType = currentMethod.getVariables().get(token.getLexeme()).getType();
            }
            else {
                if ((className != null) && (SymbolTable.getClass(className).getAttributes().containsKey(token.getLexeme()))) {

                    Attribute attr = SymbolTable.getClass(className).getAttributes().get(token.getLexeme());
                    //verifico si es privado
                    /*
                    if (!attr.getIsPublic()) {
                        if (!SymbolTable.getClass(className).getName().equals(this.className)) {
                            throw new SemanticASTException(token, "No se puede acceder al atributo privado "
                                    + attr.getName() + " desde la clase " + this.className);
                        }
                    }
                     */
                    arrayType = attr.getType();
                }
                else {
                    throw new SemanticASTException(token,
                            "El identificador '" + token.getLexeme() + "' no ha sido declarado.");
                }
            }
        }

        // Validar que el tipo sea Array
        if (!arrayType.getName().equals("Array")) {
            throw new SemanticASTException(token,
                    "Se intentó indexar '" + token.getLexeme() +
                            "' de tipo " + arrayType.getName() + " pero no es un Array.");
        }

        //el indice debe ser una expresion (no a[])
        if (expressionNode == null) {
            throw new SemanticASTException(token, "Se esperaba una expresión del índice del" +
                    " Array");
        }

        //obtengo el tipo del indice
        Type intType = expressionNode.check();

        if (!intType.getName().equals("Int")) {
            throw new SemanticASTException(token, "El índice debe ser de tipo Int. Se encontró: " +
                    intType.getName());
        }

        //obtengo el tipo que devuelve el array
        Type arrType = arrayType.getArrType();

        if (arrType == null) {
            throw new SemanticASTException(token,
                    "El tipo interno del Array es null.");
        }

        if (chainedNode != null) {
            throw new SemanticASTException(token, "Un array no puede tener un encadenamiento.");

        }

        this.nodeType = arrType;
        return arrType;
    }

}
