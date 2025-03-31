package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.lexicalAnalyzer.LexicalAnalyzer;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

/**
 * Clase que representa el ejecutor de la etapa 1
 * @author Tomás Rando
 */
public class Etapa1 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException {

        if (args.length != 2) {
            throw new WriterException("ERROR: DEBE INDICAR 2 ARGUMENTOS (INPUT FILE) (OUTPUT FILE)");
        }
        
        boolean stop = false;
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
        Token token;

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(args[1]));
            writer.println("CORRECTO: ANALISIS LEXICO");
            writer.println("| TOKEN | LEXEMA |  NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) |");
            while (!stop) {
                token = lexicalAnalyzer.nextToken();
                writer.println("| " + token.getName().name() + " | " + token.getLexeme() + " | LINEA " + token.getRow() + " (COLUMNA " + token.getColumn() + ") |");

                if (token.getName() == TokenTypes.end_of_file) {
                    stop = true;
                }
            }
        } catch (IOException ex) {
            throw new WriterException("NO SE PUEDE ESCRIBIR EL ARCHIVO " + args[1]);
        } catch (LexicalException | ReaderException ex) {
            throw ex;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
