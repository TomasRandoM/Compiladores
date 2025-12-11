package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

/**
 * Clase que representa un acceso encadenado desde un acceso a un array.
 * Extiende {@link ChainedAccessNode}
 */
public class ChainedArrayAccessNode extends ChainedAccessNode {

    /**
     * Representa la expresion que indica la posicion del array
     */
    private ExpressionNode expression;

    /**
     * Constructor de ChainedArrayAccess
     * @param name
     */
    public ChainedArrayAccessNode(Token name, String className, String methodName) {
        super(name, className, methodName);
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    /**
     * Metodo para manejar la resolucion de nombres. Recibe el nombre de la clase anterior. Se
     * fija en la tabla de simbolos si esa clase posee el metodo actual. Luego, el retorno de
     * ese metodo se convierte en el siguiente lastClass y se llama a chequear el siguiente
     * encadenamiento.
     * Ejemplo: Si tengo el caso A.b().[i] Al principio se llamara a chequear b() con el lastClass A.
     * @param lastType String con el tipo de la clase anterior o el tipo de retorno del anterior metodo
     */
    @Override
    public Type checkNames(Type lastType) throws SemanticASTException {
        parentType = lastType;

        Type finalType;
        // si es el primer nodo
        if (lastType == null) {;
            finalType = getArrayAccessChainedNodeVariableType(name);
            lastType = finalType; // ACTUALIZAR lastType para evitar null
        }
        else {
            if (lastType.getName().equals("Array")) {
                throw new SemanticASTException(name, "No se permite " +
                        "que una variable de tipo Array tenga encadenamiento");
            }

            if (lastType.getName().equals("void")) {
                throw new SemanticASTException(getToken(), "Void no " +
                        "puede aparecer en un encadenamiento");
            }
            Class classA = SymbolTable.getClass(lastType.getName());

            if (classA.getAttributes().get(getToken().getLexeme()) == null) {
                throw new SemanticASTException(getToken(), "No existe el atributo " +
                        getToken().getLexeme() + "  de tipo Array " +
                        "en la clase "+ classA.getName());
            }

            Attribute attr = classA.getAttributes().get(getToken().getLexeme());

            if (!attr.getIsPublic()) {
                if (!lastType.getName().equals(getClassName())) {
                    if (getClassName() == null) {
                        throw new SemanticASTException(name, "No se puede acceder a " +
                                "un atributo privado (" + attr.getName() +
                                ") desde el método start");
                    }
                    throw new SemanticASTException(name, "No se puede acceder a " +
                            "un atributo privado (" + attr.getName() +
                            ") desde otra clase (" + className + ").");
                }
            }

            // expresión índice
            if (expression == null) {
                throw new SemanticASTException(name,
                        "Se esperaba una expresión de índice para el Array.");
            }

            // índice Int
            Type indexType = expression.check();
            if (!indexType.getName().equals("Int")) {
                throw new SemanticASTException(expression.getToken(),
                        "El índice del Array debe ser Int. Se encontró: " + indexType.getName());
            }

            // tipo resultante
            finalType = attr.getType().getArrType();

        }

        if (finalType == null) {
            throw new SemanticASTException(name, "El tipo del array es null.");
        }

        if (this.chainedNode != null) {

            throw new SemanticASTException(name,
                    "Un array no puede tener un encadenamiento después del índice.");
        }
        this.nodeType = finalType;
        return finalType;
    }

    /**
     * Se verifica el tipo del primer encadenado
     * @param token Token del encadenado
     * @return Type del primer nodo del encadenado
     * @throws SemanticASTException
     */
    public Type getArrayAccessChainedNodeVariableType(Token token) throws SemanticASTException {
        Method method;
        Type type;
        if (methodName == null) {
            if (className == null) {
                //Este error no debería aparecer
                throw new SemanticASTException(token, "El método y " +
                        "la clase actuales son nulos");
            }
            method = SymbolTable.getClass(className).getConstructor();
        }
        else {
            if (methodName.equals("start")) {
                method = SymbolTable.getStartMethodStored();
            }
            else {
                method = SymbolTable.getClass(className).getMethods().get(methodName);
            }
        }

        if (method.getParameters().get(token.getLexeme()) != null) {
            type = method.getParameters().get(token.getLexeme()).getType();
        }
        else {
            if (method.getVariables().get(token.getLexeme()) != null) {
                type =  method.getVariables().get(token.getLexeme()).getType();
            }
            else {
                if ((className != null) && (SymbolTable.getClass(className).getAttributes().get(token.getLexeme()) != null)) {
                    type = SymbolTable.getClass(className).getAttributes().get(token.getLexeme()).getType();
                }
                else {
                    throw new SemanticASTException(token, "La variable " +
                            token.getLexeme() + " no se encuentra " +
                            "debidamente inicializada en este ámbito");
                }
            }
        }
        type.setToken(token);
        return type;
    }

    /**
     * Generacion de codigo para el acceso a array encadenado. Cabe destacar que este no es el nodo utilizado
     * en una asignacion
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        Class class1 = SymbolTable.getClass(parentType.getName());
        Attribute att = class1.getAttributes().get(name.getLexeme());
        int attributeOffset = class1.getAttributeOffset(name.getLexeme());
        string.append("#Guardamos a0 en la pila, que es el padre\n");
        string.append("beq $a0, $zero, variableNotInitialized \n");
        string.append("sw $a0, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        expression.codeGen(string);
        //Queda en a0 el resultado de la expresión
        checkChained(string, expression);

        if (expression instanceof ArrayAccessNode) {
            string.append("#Se obtiene el valor del array desde la direccion \n");
            if (expression.nodeType.getName().equals("Double")) {
                //Aca no deberia entrar
                string.append("l.d $f0, 0($a0) \n");
            }
            else {
                string.append("lw $a0, 0($a0) \n");
            }
        }

        string.append("#Restauramos el self que habiamos dejado en la pila \n");
        string.append("addiu $sp $sp 4 \n");
        string.append("lw $t0, 0($sp) \n");
        string.append("#Obtenemos la direccion del array \n");
        string.append("lw $t0, ").append(attributeOffset).append("($t0) \n");
        string.append("beq $t0, $zero, variableNotInitialized \n");
        string.append("#En $a0 tengo el indice del array\n");
        string.append("#Si el índice es negativo salto a la excepcion\n");
        string.append("bltz $a0, negativeArrayIndexException\n");
        string.append("#Obtengo la longitud del array\n");
        string.append("lw $t1, 4($t0)\n");
        string.append("#Si el indice es mayor o igual a la longitud salto a la excepcion\n");
        string.append("bge $a0, $t1, arrayIndexOutOfRangeException\n");
        string.append("#Calculamos el offset usando la posicion (en a0) y\n");
        string.append("#el espacio que ocupan los elementos del array\n");
        if (att.getType().getArrType().getName().equals("Double")) {
            string.append("li $t1, 8 \n");
        }
        else {
            string.append("li $t1, 4\n");
        }
        string.append("mul $a0, $a0, $t1 \n");
        string.append("#sumamos 8 debido a que el array posee vtable y la longitud del mismo \n");
        string.append("addiu $a0 $a0 8 \n");
        string.append("#le sumamos a la direccion del array el offset y obtenemos la direccion del elemento \n");
        string.append("add $t0 $t0 $a0 \n");
        if (att.getType().getArrType().getName().equals("Double")) {
            string.append("#cargamos el elemento en f0 \n");
            string.append("lw $f0 0($t0) \n");
        }
        else {
            string.append("#cargamos el elemento en a0 \n");
            string.append("lw $a0 0($t0) \n");
        }
    }

    /**
     * Este metodo se fija si el ultimo encadenado es un ChainedAccessNode
     * para asi cargar su valor (desde la direccion que retorna).
     * @param string StringBuilder
     * @param expressionNode ExpressionNode
     */
    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();
        //contempla el caso de chainedNode1 == null.
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
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
        }
    }

}
