package com.uncuyo.compiladores.utils;

import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonASTGenerator {

    public static void printAST(String path) throws WriterException {
        StringBuilder builder = new StringBuilder();
        int level = 1;
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"blockNodes\": [");

        if (AST.getBlockNodes().isEmpty()) {
            builder.append("]\n");
        } else {
            builder.append("\n");
            level++;
            int count = 0;
            for (BlockNode blockNode : AST.getBlockNodes()) {
                addTab(builder, level);
                blockNode(blockNode, builder, level);
                count++;
                if (count == AST.getBlockNodes().size()) {
                    builder.append("\n");
                    level--;
                    addTab(builder, level);
                    builder.append("]\n");
                } else {
                    builder.append(",\n");
                }
            }
        }

        builder.append("}");

        try {
            Path path1 = Paths.get(path);
            Files.writeString(path1, builder.toString());
        } catch (IOException e) {
            throw new WriterException("Error escribiendo el archivo de salida.");
        }
    }

    private static void blockNode(BlockNode node, StringBuilder builder, int level) {
        if (node == null) {
            builder.append("null");
        } else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"type\": \"BlockNode\",\n");
            addTab(builder, level);
            builder.append("\"className\": ").append(node.getClassName() != null ? "\"" + node.getClassName() + "\"" : "null").append(",\n");
            addTab(builder, level);
            builder.append("\"methodName\": ").append(node.getMethodName() != null ? "\"" + node.getMethodName() + "\"" : "null").append(",\n");
            addTab(builder, level);
            builder.append("\"methodBlock\": ").append(node.isMethodBlock()).append(",\n");
            addTab(builder, level);
            builder.append("\"sentences\": [");

            if (node.getSentences().isEmpty()) {
                builder.append("]\n");
            } else {
                builder.append("\n");
                level++;
                int count = 0;
                for (SentenceNode sentence : node.getSentences()) {
                    addTab(builder, level);
                    sentenceNode(sentence, builder, level);
                    count++;
                    if (count == node.getSentences().size()) {
                        builder.append("\n");
                        level--;
                        addTab(builder, level);
                        builder.append("]\n");
                    } else {
                        builder.append(",\n");
                    }
                }
            }

            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void sentenceNode(SentenceNode node, StringBuilder builder, int level) {
        if (node == null) {
            builder.append("null");
        } else if (node instanceof ReturnNode) {
            returnNode((ReturnNode) node, builder, level);
        } else if (node instanceof AssignmentNode) {
            assignmentNode((AssignmentNode) node, builder, level);
        } else if (node instanceof IfThenElseNode) {
            ifThenElseNode((IfThenElseNode) node, builder, level);
        } else if (node instanceof WhileNode) {
            whileNode((WhileNode) node, builder, level);
        } else if (node instanceof BlockNode) {
            blockNode((BlockNode) node, builder, level);
        } else if (node instanceof SimpleSentenceNode) {
            simpleSentenceNode((SimpleSentenceNode) node, builder, level);
        } else {
            builder.append("{\"type\": \"UnknownSentence\"}");
        }
    }

    private static void returnNode(ReturnNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ReturnNode\",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void assignmentNode(AssignmentNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"AssignmentNode\",\n");
        addTab(builder, level);
        builder.append("\"leftNode\": ");
        expressionNode(node.getLeftNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"rightNode\": ");
        expressionNode(node.getRightNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void ifThenElseNode(IfThenElseNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"IfThenElseNode\",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"sentenceNode\": ");
        sentenceNode(node.getSentenceNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"elseSentenceNode\": ");
        sentenceNode(node.getElseSentenceNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void whileNode(WhileNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"WhileNode\",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"sentenceNode\": ");
        sentenceNode(node.getSentenceNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void simpleSentenceNode(SimpleSentenceNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"SimpleSentenceNode\",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void expressionNode(ExpressionNode node, StringBuilder builder, int level) {
        if (node == null) {
            builder.append("null");
        } else if (node instanceof BinaryExpressionNode) {
            binaryExpressionNode((BinaryExpressionNode) node, builder, level);
        } else if (node instanceof UnaryExpressionNode) {
            unaryExpressionNode((UnaryExpressionNode) node, builder, level);
        } else if (node instanceof ParenthesizedExpressionNode) {
            parenthesizedExpressionNode((ParenthesizedExpressionNode) node, builder, level);
        } else if (node instanceof LiteralNode) {
            literalNode((LiteralNode) node, builder, level);
        } else if (node instanceof VariableNode) {
            variableNode((VariableNode) node, builder, level);
        } else if (node instanceof SelfNode) {
            selfNode((SelfNode) node, builder, level);
        } else if (node instanceof NewNode) {
            newNode((NewNode) node, builder, level);
        } else if (node instanceof MethodCallNode) {
            methodCallNode((MethodCallNode) node, builder, level);
        } else if (node instanceof ArrayAccessNode) {
            arrayAccessNode((ArrayAccessNode) node, builder, level);
        } else if (node instanceof ChainedAccessNode) {
            chainedAccessNode((ChainedAccessNode) node, builder, level);
        } else if (node instanceof ChainedCallNode) {
            chainedCallNode((ChainedCallNode) node, builder, level);
        } else if (node instanceof ChainedArrayAccessNode) {
            chainedArrayAccessNode((ChainedArrayAccessNode) node, builder, level);
        } else {
            builder.append("{\"type\": \"UnknownExpression\"}");
        }
    }

    private static void binaryExpressionNode(BinaryExpressionNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"BinaryExpressionNode\",\n");
        addTab(builder, level);
        builder.append("\"operator\": ");
        token(node.getOperator(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"left\": ");
        expressionNode(node.getLeft(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"right\": ");
        expressionNode(node.getRight(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void unaryExpressionNode(UnaryExpressionNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"UnaryExpressionNode\",\n");
        addTab(builder, level);
        builder.append("\"operator\": ");
        token(node.getOperator(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void parenthesizedExpressionNode(ParenthesizedExpressionNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ParenthesizedExpressionNode\",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void literalNode(LiteralNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"LiteralNode\",\n");
        addTab(builder, level);
        builder.append("\"option\": \"").append(node.getOption()).append("\",\n");
        addTab(builder, level);
        builder.append("\"token\": ");
        token(node.getToken(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void variableNode(VariableNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"VariableNode\",\n");
        addTab(builder, level);
        builder.append("\"token\": ");
        token(node.getToken(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"currentClass\": ").append(node.getCurrentClass() != null ? "\"" + node.getCurrentClass() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"currentMethod\": ").append(node.getCurrentMethod() != null ? "\"" + node.getCurrentMethod() + "\"" : "null").append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void selfNode(SelfNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"SelfNode\",\n");
        addTab(builder, level);
        builder.append("\"token\": ");
        token(node.getToken(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"className\": ").append(node.getClassName() != null ? "\"" + node.getClassName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void newNode(NewNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"NewNode\",\n");
        addTab(builder, level);
        builder.append("\"option\": \"").append(node.getOption()).append("\",\n");
        addTab(builder, level);
        builder.append("\"typeToken\": ");
        token(node.getType(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"parameterList\": [");

        if (node.getParameterList().isEmpty()) {
            builder.append("],\n");
        } else {
            builder.append("\n");
            level++;
            int count = 0;
            for (ExpressionNode expr : node.getParameterList()) {
                addTab(builder, level);
                expressionNode(expr, builder, level);
                count++;
                if (count == node.getParameterList().size()) {
                    builder.append("\n");
                    level--;
                    addTab(builder, level);
                    builder.append("],\n");
                } else {
                    builder.append(",\n");
                }
            }
        }

        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void methodCallNode(MethodCallNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"MethodCallNode\",\n");
        addTab(builder, level);
        builder.append("\"token\": ");
        token(node.getToken(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"isStatic\": ").append(node.isStatic()).append(",\n");
        addTab(builder, level);
        builder.append("\"className\": ").append(node.getClassName() != null ? "\"" + node.getClassName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"parameterList\": [");

        if (node.getParameterList().isEmpty()) {
            builder.append("],\n");
        } else {
            builder.append("\n");
            level++;
            int count = 0;
            for (ExpressionNode expr : node.getParameterList()) {
                addTab(builder, level);
                expressionNode(expr, builder, level);
                count++;
                if (count == node.getParameterList().size()) {
                    builder.append("\n");
                    level--;
                    addTab(builder, level);
                    builder.append("],\n");
                } else {
                    builder.append(",\n");
                }
            }
        }

        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void arrayAccessNode(ArrayAccessNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ArrayAccessNode\",\n");
        addTab(builder, level);
        builder.append("\"token\": ");
        token(node.getToken(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"expressionNode\": ");
        expressionNode(node.getExpressionNode(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void chainedNode(ChainedNode node, StringBuilder builder, int level) {
        if (node == null) {
            builder.append("null");
        } else if (node instanceof ChainedAccessNode) {
            chainedAccessNode((ChainedAccessNode) node, builder, level);
        } else if (node instanceof ChainedCallNode) {
            chainedCallNode((ChainedCallNode) node, builder, level);
        } else if (node instanceof ChainedArrayAccessNode) {
            chainedArrayAccessNode((ChainedArrayAccessNode) node, builder, level);
        } else {
            builder.append("{\"type\": \"UnknownChained\"}");
        }
    }

    private static void chainedAccessNode(ChainedAccessNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ChainedAccessNode\",\n");
        addTab(builder, level);
        builder.append("\"name\": ");
        token(node.getName(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"className\": ").append(node.getClassName() != null ? "\"" + node.getClassName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"methodName\": ").append(node.getMethodName() != null ? "\"" + node.getMethodName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void chainedCallNode(ChainedCallNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ChainedCallNode\",\n");
        addTab(builder, level);
        builder.append("\"name\": ");
        token(node.getToken(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"parameterList\": [");

        if (node.getParameterList().isEmpty()) {
            builder.append("],\n");
        } else {
            builder.append("\n");
            level++;
            int count = 0;
            for (ExpressionNode expr : node.getParameterList()) {
                addTab(builder, level);
                expressionNode(expr, builder, level);
                count++;
                if (count == node.getParameterList().size()) {
                    builder.append("\n");
                    level--;
                    addTab(builder, level);
                    builder.append("],\n");
                } else {
                    builder.append(",\n");
                }
            }
        }

        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void chainedArrayAccessNode(ChainedArrayAccessNode node, StringBuilder builder, int level) {
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"type\": \"ChainedArrayAccessNode\",\n");
        addTab(builder, level);
        builder.append("\"name\": ");
        token(node.getName(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"className\": ").append(node.getClassName() != null ? "\"" + node.getClassName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"methodName\": ").append(node.getMethodName() != null ? "\"" + node.getMethodName() + "\"" : "null").append(",\n");
        addTab(builder, level);
        builder.append("\"expression\": ");
        expressionNode(node.getExpression(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"chainedNode\": ");
        chainedNode(node.getChainedNode(), builder, level + 1);
        builder.append("\n");
        addTab(builder, level - 1);
        builder.append("}");
    }

    private static void addTab(StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) {
            builder.append("\t");
        }
    }

    private static void token(Token token, StringBuilder builder, int level) {
        if (token == null) {
            builder.append("null");
        } else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(token.getName().toString()).append("\",\n");
            addTab(builder, level);
            builder.append("\"lexeme\": \"").append(token.getLexeme()).append("\",\n");
            addTab(builder, level);
            builder.append("\"valor\": ").append(token.getValor()).append(",\n");
            addTab(builder, level);
            builder.append("\"column\": ").append(token.getColumn()).append(",\n");
            addTab(builder, level);
            builder.append("\"row\": ").append(token.getRow()).append("\n");
            addTab(builder, level - 1);
            builder.append("}");
        }
    }
}