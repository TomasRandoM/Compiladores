package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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
    public Type checkNames(Type lastType) throws SemanticASTException {
        //la clase debe ser array
        if (!lastType.getName().equals("Array")) {
            throw new SemanticASTException(name, "Se intentó acceder a un índice de un " +
                    "elemento de tipo " + lastType.getName() + " el cual no es un Array.");
        }

        //el indice debe ser una expresion (no a[])
        if (expression == null) {
            throw new SemanticASTException(name, "Se esperaba una expresión del índice del" +
                    " Array");
        }

        //obtengo el tipo del indice
        Type intType = expression.check();

        if (!intType.getName().equals("Int")) {
            throw new SemanticASTException(name, "El índice debe ser de tipo Int. Se encontró: " +
                    intType.getName());
        }

        //obtengo el tipo que devuelve el array
        Type arrType = lastType.getArrType();

        if (arrType == null) {
            throw new SemanticASTException(name, "El tipo del array es null.");
        }

        //verifico que el tipo exista
        Class classType = SymbolTable.getClass(arrType.getName());
        if (classType == null) {
            throw new SemanticASTException(name, "El tipo " + arrType.getName() +
                    " no existe");
        }

        if (chainedNode != null) {
            throw new SemanticASTException(name, "Un array no puede tener un encadenamiento.");

        }

        return arrType;
    }

}
