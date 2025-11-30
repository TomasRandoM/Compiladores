package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el arbol sintactico abstracto. Guarda la lista de bloques de los metodos
 */
public class AST {
    /**
     * Sirve solo para armar el AST, en el semantico se va guardando la clase que esta siendo
     * analizada
     */
    private static String currentClass;

    /**
     * Sirve solo para armar el AST, en el semantico se va guardando el metodo que esta siendo
     * analizado
     */
    private static String currentMethod;

    /**
     * Lista de bloques de metodos
     */
    private static List<BlockNode> blockNodes = new ArrayList<>();

    /**
     * Comienza el chequeo de sentencias. Llama a chequear todas las sentencias de la lista
     */
    public static void check() throws SemanticASTException {
        for (BlockNode blockNode : blockNodes) {
            blockNode.check();
        }
    }

    public static List<BlockNode> getBlockNodes() {
        return blockNodes;
    }

    public static void addBlockNode(BlockNode blockNode) {
        blockNodes.add(blockNode);
    }

    public static void setBlockNodes(List<BlockNode> blockNodes) {
        AST.blockNodes = blockNodes;
    }

    public static String getCurrentMethod() {
        return currentMethod;
    }

    public static void setCurrentMethod(String currentMethod) {
        AST.currentMethod = currentMethod;
    }

    public static String getCurrentClass() {
        return currentClass;
    }

    public static void setCurrentClass(String currentClass) {
        AST.currentClass = currentClass;
    }
}
