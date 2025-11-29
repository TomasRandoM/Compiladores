package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import java.util.ArrayList;
import java.util.List;

public class AST {
    private static String currentClass;

    private static String currentMethod;

    private static List<BlockNode> blockNodes = new ArrayList<>();



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
