package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

/**
 * Clase que representa el acceso encadenado.
 * Extiende {@link ChainedNode}
 */
public class ChainedAccessNode extends ChainedNode {
    /**
     * Token con el nombre de la variable a la cual es accedida
     */
    protected Token name;

    /**
     * Constructor de ChainedAccessNode
     * @param name Token del nombre de la variable accedida en el encadenamiento
     */
    public ChainedAccessNode(Token name) {
        this.name = name;
    }

    public Token getToken() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Chequea la semántica
     * @return Type
     */
    public Type check() {
        return null;
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
