package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import com.uncuyo.compiladores.utils.JsonASTGenerator;
import com.uncuyo.compiladores.utils.JsonGenerator;

/**
 * Clase que representa el ejecutor de la etapa 3
 *
 * @author Tomás Rando
 */
public class Etapa3 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException, SyntacticException {

        //Chequeo de recibimiento de parámetros
        if (args.length < 1) {
            throw new WriterException("ERROR: DEBE INDICAR AL MENOS UN ARGUMENTO (INPUT FILE)");
        }
        if (args.length > 1) {
            throw new WriterException("ERROR: DEBE INDICAR COMO MAXIMO UN ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        try {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(args[0]);
            syntacticAnalyzer.program();
            String path = args[0];
            String outputPath = path.substring(0, path.length() - 1) + "ts.json";
            JsonGenerator.printSymbolTable(outputPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
