package com.uncuyo.compiladores.utils;

import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class JsonGenerator {

    public static void printSymbolTable(String path) throws WriterException {
        StringBuilder builder = new StringBuilder();
        int level = 1;
        builder.append("{\n");
        addTab(builder, level);
        builder.append("\"classes\": {");
        if (SymbolTable.getClasses().isEmpty()) {
            builder.append("}, \n");
        }
        else {
            level++;
            builder.append("\n");

            int count = 0;
            for (Map.Entry<String, Class> e : SymbolTable.getClasses().entrySet()) {
                addTab(builder, level);
                builder.append("\"").append(e.getKey()).append("\": ");
                class1(e.getValue(), builder, level + 1);
                count++;
                if (count == SymbolTable.getClasses().size()) {
                    builder.append("\n");
                    level--;
                    addTab(builder, level);
                    builder.append("},\n");
                }
                else {
                    builder.append(",\n");
                }
            }
        }
        addTab(builder, level);
        builder.append("\"currentClass\": ");
        class1(SymbolTable.getCurrentClass(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"startMethodStored\": ");
        method(SymbolTable.getStartMethodStored(), builder, level + 1);
        builder.append(",\n");
        addTab(builder, level);
        builder.append("\"currentMethod\": ");
        method(SymbolTable.getCurrentMethod(), builder, level + 1);
        builder.append("\n");
        builder.append("}");
        try {
            Path path1 = Paths.get(path);
            Files.writeString(path1, builder.toString());
        } catch (IOException e) {
            throw new WriterException("Error escribiendo el archivo de salida.");
        }

    }

    private static void class1(Class class2, StringBuilder builder, int level) {
        if (class2 == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(class2.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(class2.getToken(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            if (class2.getParentClass() == null) {
                builder.append("\"parentClass\": null,\n");
            }
            else {
                builder.append("\"parentClass\": \"").append(class2.getParentClass()).append("\",\n");
            }
            addTab(builder, level);
            builder.append("\"attributes\": {");
            if (class2.getAttributes().isEmpty()) {
                builder.append("}, \n");
            }
            else {
                builder.append("\n");
                level++;
                int count = 0;
                for (Map.Entry<String, Attribute> e : class2.getAttributes().entrySet()) {
                    addTab(builder, level);
                    builder.append("\"").append(e.getKey()).append("\": ");
                    attribute(e.getValue(), builder, level + 1);
                    count++;
                    if (count == class2.getAttributes().size()) {
                        builder.append("\n");
                        level--;
                        addTab(builder, level);
                        builder.append("},\n");
                    }
                    else {
                        builder.append(",\n");
                    }
                }
            }
            addTab(builder, level);
            builder.append("\"methods\": {");
            if (class2.getMethods().isEmpty()) {
                builder.append("}, \n");
            }
            else {
                builder.append("\n");
                level++;
                int count = 0;
                for (Map.Entry<String, Method> e : class2.getMethods().entrySet()) {
                    addTab(builder, level);
                    builder.append("\"").append(e.getKey()).append("\": ");
                    method(e.getValue(), builder, level + 1);
                    count++;
                    if (count == class2.getMethods().size()) {
                        builder.append("\n");
                        level--;
                        addTab(builder, level);
                        builder.append("},\n");
                    }
                    else {
                        builder.append(",\n");
                    }
                }
            }
            addTab(builder, level);
            builder.append("\"constructor\": ");
            method(class2.getConstructor(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"implInitialized\": ").append(class2.isImplInitialized()).append(",\n");
            addTab(builder, level);
            builder.append("\"classInitialized\": ").append(class2.isClassInitialized()).append(",\n");
            addTab(builder, level);
            builder.append("\"implToken\": ");
            token(class2.getImplToken(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"classToken\": ");
            token(class2.getClassToken(), builder, level + 1);
            builder.append("\n");
            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void method(Method method,  StringBuilder builder, int level) {
        if (method == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(method.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"type\": ");
            type(method.getType(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(method.getToken(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"isStaticMethod\": ").append(method.isStaticMethod()).append(",\n");
            addTab(builder, level);
            builder.append("\"variables\": {");
            if (method.getVariables().isEmpty()) {
                builder.append("}, \n");
            }
            else {
                builder.append("\n");
                level++;
                int count = 0;
                for (Map.Entry<String, Variable> e : method.getVariables().entrySet()) {
                    addTab(builder, level);
                    builder.append("\"").append(e.getKey()).append("\": ");
                    variable(e.getValue(), builder, level + 1);
                    count++;
                    if (count == method.getVariables().size()) {
                        builder.append("\n");
                        level--;
                        addTab(builder, level);
                        builder.append("},\n");
                    }
                    else {
                        builder.append(",\n");
                    }
                }
            }
            addTab(builder, level);
            builder.append("\"parameters\": {");
            if (method.getParameters().isEmpty()) {
                builder.append("} \n");
            }
            else {
                builder.append("\n");
                level++;
                int count = 0;
                for (Map.Entry<String, Parameter> e : method.getParameters().entrySet()) {
                    addTab(builder, level);
                    builder.append("\"").append(e.getKey()).append("\": ");
                    parameter(e.getValue(), builder, level + 1);
                    count++;
                    if (count == method.getParameters().size()) {
                        builder.append("\n");
                        level--;
                        addTab(builder, level);
                        builder.append("}\n");
                    }
                    else {
                        builder.append(",\n");
                    }
                }
            }
            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void type(Type type, StringBuilder builder, int level) {
        if (type == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(type.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"arrType\": ");
            type(type.getArrType(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(type.getToken(), builder, level + 1);
            builder.append("\n");
            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void attribute(Attribute attribute, StringBuilder builder, int level) {
        if (attribute == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(attribute.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"type\": ");
            type(attribute.getType(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(attribute.getToken(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"isPublic\": ").append(attribute.getIsPublic()).append("\n");
            addTab(builder, level - 1);
            builder.append("} ");
        }
    }

    private static void parameter(Parameter parameter, StringBuilder builder, int level) {
        if (parameter == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(parameter.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"type\": ");
            type(parameter.getType(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(parameter.getToken(), builder, level + 1);
            builder.append("\n");
            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void variable(Variable variable, StringBuilder builder, int level) {
        if (variable == null) {
            builder.append("null");
        }
        else {
            builder.append("{\n");
            addTab(builder, level);
            builder.append("\"name\": \"").append(variable.getName()).append("\",\n");
            addTab(builder, level);
            builder.append("\"type\": ");
            type(variable.getType(), builder, level + 1);
            builder.append(",\n");
            addTab(builder, level);
            builder.append("\"token\": ");
            token(variable.getToken(), builder, level + 1);
            builder.append("\n");
            addTab(builder, level - 1);
            builder.append("}");
        }
    }

    private static void addTab(StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) {
            builder.append("\t");
        }
    }

    private static void token(Token token, StringBuilder builder, int level) {
        if (token == null) {
            builder.append("null");
        }
        else {
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
