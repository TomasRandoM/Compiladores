package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;

/**
 * Clase que representa el ejecutor de la etapa 2
 *
 * @author Tomás Rando
 */
public class Etapa2 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException, SyntacticException {

        //Chequeo de recibimiento de parámetros
        if (args.length < 1) {
            throw new WriterException("ERROR: D%EBE INDICAR AL MENOS UN ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        try {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(args[0]);
            syntacticAnalyzer.program();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
