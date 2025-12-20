package com.uncuyo.compiladores;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.WriterException;
import com.uncuyo.compiladores.lexicalAnalyzer.LexicalAnalyzer;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el ejecutor de la etapa 1
 *
 * @author Tomás Rando
 */
public class Etapa1 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException {
        Path outputPath;
        Path tempPath;
        File tempFile;
        Token token;

        //Chequeo de recibimiento de parámetros
        if (args.length < 1) {
            throw new WriterException("ERROR: DEBE INDICAR AL MENOS UN ARGUMENTO (INPUT FILE)");
        }

        if (args.length > 2) {
            throw new WriterException("ERROR: DEBE INDICAR COMO MAXIMO 2 ARGUMENTOS (INPUT FILE) (OUTPUT FILE)");
        }
        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        //Se definen los paths y files.
        Path inputPath = Paths.get(args[0]);
        Path dirPath = inputPath.getParent();
        if (args.length == 2) {
            outputPath = dirPath.resolve(args[1]);
            tempPath = dirPath.resolve("tempFile.tmp");
            tempFile = tempPath.toFile();

            boolean stop = false;
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter(tempFile));
                writer.println("CORRECTO: ANALISIS LEXICO");
                writer.println("| TOKEN | LEXEMA |  NUMERO DE LINEA (NUMERO DE COLUMNA) |");
                while (!stop) {
                    token = lexicalAnalyzer.nextToken();
                    writer.println("| " + token.getName().name() + " | " +
                            token.getLexeme() +
                            " | LINEA " + token.getRow() +
                            " (COLUMNA " + token.getColumn() + ") |");

                    if (token.getName() == TokenTypes.end_of_file) {
                        stop = true;
                    }
                }

                writer.close();
                //El archivo temporal lo renombramos al normal
                Files.move(tempPath, outputPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (LexicalException | ReaderException ex) {
                writer.close();
                try {
                    //Borramos el archivo temporal
                    Files.deleteIfExists(tempPath);
                } catch (IOException e) {
                    //
                }
                System.out.println(ex.getMessage());

            } catch (IOException ex) {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    //Borramos el archivo temporal
                    Files.deleteIfExists(tempPath);
                } catch (IOException e) {
                    //
                }
                throw new WriterException("NO SE PUEDE ESCRIBIR EL ARCHIVO " + outputPath);
            }
        }
        else {
            boolean stop = false;
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
            StringBuilder string = new StringBuilder();
            try {
                string.append("CORRECTO: ANALISIS LEXICO \n");
                string.append("| TOKEN | LEXEMA |  NUMERO DE LINEA (NUMERO DE COLUMNA) | \n");
                while (!stop) {
                    token = lexicalAnalyzer.nextToken();
                    string.append("| " + token.getName().name() + " | " +
                            token.getLexeme() + " | LINEA " + token.getRow() +
                            " (COLUMNA " + token.getColumn() + ") | \n");

                    if (token.getName() == TokenTypes.end_of_file) {
                        stop = true;
                    }
                }
                System.out.println(string.toString());

            } catch (LexicalException | ReaderException ex) {
                System.out.println(ex.getMessage());

            }
        }
    }

    /**
     * Nos sirve para los tests JUnit, en lugar de escribir en un archivo, devuelve una lista
     * de tokens.
     * @param input Path del archivo de entrada.
     * @return ArrayList con los tokens encontrados
     * @throws ReaderException Excepción de lectura
     * @throws LexicalException Excepción léxica
     * @author Tomás Rando
     */
    public static List<Token> getAllTokens(String input) throws ReaderException, LexicalException {
        boolean stop = false;
        Token token;
        List<Token> tokenList = new ArrayList<>();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(input);

        //Chequeo de extensión
        if (!input.endsWith(".s")) {
            throw new ReaderException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        try {
            while (!stop) {
                token = lexicalAnalyzer.nextToken();
                tokenList.add(token);

                if (token.getName() == TokenTypes.end_of_file) {
                    stop = true;
                }
            }

        } catch (LexicalException | ReaderException ex) {
            throw ex;
        }
        return tokenList;
    }
}
