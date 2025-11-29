package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;

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
    public ChainedArrayAccessNode(Token name) {
        super(name);
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
     * Ejemplo: Si tengo el caso A.b().c(). Al principio se llamara a chequear b() con el lastClass A.
     * @param lastClass String con el tipo de la clase anterior o el tipo de retorno del anterior metodo
     */
    public void checkNames(String lastClass) {
        //Resolucion de nombres
    }

}
