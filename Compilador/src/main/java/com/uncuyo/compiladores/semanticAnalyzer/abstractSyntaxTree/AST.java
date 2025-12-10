package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.WriterException;

import java.io.FileWriter;
import java.io.IOException;
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

    private static List<String> vtablesMade = new ArrayList<>();
    /**
     * Sirve para chequear en el chequeo de sentencias si el metodo actualmente analizado tiene un return
     */
    private static boolean isReturnPresent = false;

    /**
     * Comienza el chequeo de sentencias. Llama a chequear todas las sentencias de la lista
     */
    public static void check() throws SemanticASTException {
        for (BlockNode blockNode : blockNodes) {
            blockNode.check();
        }
    }

    public static void codeGen(String pathName) throws SemanticASTException, WriterException {
        StringBuilder string = new StringBuilder();
        string.append(".globl main \n");
        for (BlockNode blockNode : blockNodes) {
            blockNode.codeGen(string);
        }
        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(string.toString());
        } catch (IOException e) {
            throw new WriterException("ERROR AL ESCRIBIR EL ARCHIVO ASM: " + e.getMessage());
        }
    }



    public static void resetAST() {
        currentClass = null;
        currentMethod = null;
        blockNodes.clear();
    }

    public static boolean isReturnPresent() {
        return isReturnPresent;
    }

    public static List<String> getVtablesMade() {
        return vtablesMade;
    }

    public static void setVtablesMade(List<String> vtablesMade) {
        AST.vtablesMade = vtablesMade;
    }

    public static void setIsReturnPresent(boolean isReturnPresent) {
        AST.isReturnPresent = isReturnPresent;
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

    public static boolean isIsReturnPresent() {
        return isReturnPresent;
    }

}
