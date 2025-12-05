package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import com.uncuyo.compiladores.utils.JsonASTGenerator;

/**
 * Clase del ejecutor de la Etapa 4.
 * @author Paulina Suden
 */
public class Etapa4 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException, SyntacticException {

        //Chequeo de recibimiento de parámetros
        if (args.length < 1) {
            throw new WriterException("ERROR: DEBE INDICAR AL MENOS UN ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        try {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(args[0]);
            syntacticAnalyzer.program();
            JsonASTGenerator.printAST("JsonAST.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
