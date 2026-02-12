package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        List<BlockNode> blockNodesCopy = new ArrayList<>(blockNodes);
        boolean stop = false;
        BlockNode startNode = null;
        int count = 0;
        while (!stop) {
            if (blockNodesCopy.get(count).getClassName() == null) {
                startNode = blockNodesCopy.get(count);
                blockNodesCopy.remove(count);
                stop = true;
            }
            else {
                count++;
            }
        }
        startNode.codeGen(string);
        for (BlockNode blockNode : blockNodesCopy) {
            blockNode.codeGen(string);
        }

        writeInheritedMethods(string);
        String setupCode = readFromResources("codeGeneration/setupCode.asm");
        String exceptionsCode = readFromResources("codeGeneration/runtimeExceptions.asm");
        string.append(setupCode);
        string.append(exceptionsCode);

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(string.toString());
        } catch (IOException e) {
            throw new WriterException("ERROR AL ESCRIBIR EL ARCHIVO ASM: " + e.getMessage());
        }
    }

    public static void resetAST() {
        currentClass = null;
        currentMethod = null;
        vtablesMade.clear();
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
    public static void addClassToVtablesMadeList(String class1) {
        vtablesMade.add(class1);
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

    /**
     * Lee desde /resources el path y lo devuelve como String
     * @param path Path del resource
     * @return String
     * @throws WriterException Excepcion por si ocurriese un error
     */
    private static String readFromResources(String path) throws WriterException {
        try (InputStream input = AST.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new WriterException("ERROR: No se encontró el recurso");
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new WriterException("ERROR: Error leyendo el recurso");
        }
    }

    /**
     * Escribe el codigo de los saltos a los metodos heredados
     * @param string StringBuilder
     */
    private static void writeInheritedMethods(StringBuilder string) {
        string.append(".text \n");
        for (Class c1 : SymbolTable.getClasses().values()) {
            for (Method m1 : c1.getMethods().values()) {
                if (m1.isInheritedMethod(c1.getName())) {
                    string.append(m1.getName() + c1.getName() + ":\n");
                    string.append("j " + m1.getName() + m1.getClassname() + "\n");
                }
            }
        }
    }
}
