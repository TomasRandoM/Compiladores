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

        Type finalType;
        // si es el primer nodo
        if (lastType == null) {;
            finalType = getArrayAccessChainedNodeVariableType(name);
            lastType = finalType; // ACTUALIZAR lastType para evitar null
        }
        else {
            /*
            if (lastType.getName().equals("Array")) {
                throw new SemanticASTException(name, "No se permite " +
                        "que una variable de tipo Array tenga encadenamiento");
            }
             */
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

        return finalType;
    }

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

}
