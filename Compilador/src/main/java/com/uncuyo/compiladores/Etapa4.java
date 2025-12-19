package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import com.uncuyo.compiladores.utils.JsonASTGenerator;
import com.uncuyo.compiladores.utils.JsonGenerator;

/**
 * Clase del ejecutor de la Etapa 4.
 * @author Paulina Suden
 */
public class Etapa4 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException, SyntacticException {

        //Chequeo de recibimiento de parámetros
        if (args.length != 1) {
            throw new WriterException("ERROR: DEBE INDICAR UN ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        try {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(args[0]);
            syntacticAnalyzer.program();
            String path = args[0];
            String outputTSPath = path.substring(0, path.length() - 1) + "ts.json";
            String outputASTPath = path.substring(0, path.length() - 1) + "ast.json";
            JsonGenerator.printSymbolTable(outputTSPath);
            JsonASTGenerator.printAST(outputASTPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
