package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una llamada encadenada.
 * Extiende {@link ChainedNode}
 */
public class ChainedCallNode extends ChainedNode {
    /**
     * Token que representa el nombre del metodo llamado
     */
    private Token name;

    /**
     * Lista de parametros (expresiones)
     */
    private List<ExpressionNode> parameterList = new ArrayList<>();

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

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }

    /**
     * Constructor de ChainedCallNode
     * @param name Token
     */
    public ChainedCallNode(Token name) {
        this.name = name;
    }

    /**
     * Chequea la semantica
     * @return Type
     */
    public Type check() {
        //Pendiente
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
