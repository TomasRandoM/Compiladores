package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.*;
import com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree.AST;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import com.uncuyo.compiladores.utils.JsonASTGenerator;
import com.uncuyo.compiladores.utils.JsonGenerator;

/**
 * Clase del ejecutor de la Etapa 5.
 * @author Paulina Suden
 */
public class Etapa5 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException, SyntacticException {

        //Chequeo de recibimiento de parámetros
        if (args.length != 1) {
            throw new WriterException("ERROR: DEBE INDICAR UN SOLO ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }
        String path = args[0];
        String outputTSPath = path.substring(0, path.length() - 1) + "ts.json";
        String outputASTPath = path.substring(0, path.length() - 1) + "ast.json";
        String outputASMPath = path.substring(0, path.length() - 1) + "asm";
        try {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(args[0]);
            syntacticAnalyzer.program();
            //JsonGenerator.printSymbolTable(outputTSPath);
            //JsonASTGenerator.printAST(outputASTPath);
            AST.codeGen(outputASMPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Ejecuta el compilador sobre un archivo .s que se le pasa como parametro. Se utiliza para JUnit
     * path String con el path del archivo fuente
     */
    public static void execute(String path) throws LexicalException, SyntacticException, SemanticASTException, ReaderException, SemanticException, WriterException {
        String outputPath = path.substring(0, path.length() - 1) + "asm";
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(path);
        syntacticAnalyzer.program();
        AST.codeGen(outputPath);
    }
}
