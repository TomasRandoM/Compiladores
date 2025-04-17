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

/**
 * Clase que representa el ejecutor de la etapa 1
 *
 * @author Tomás Rando
 */
public class Etapa1 {
    public static void main(String[] args) throws ReaderException, LexicalException, WriterException {
        String fileName;
        Path outputPath;
        Path tempPath;
        File tempFile;
        Token token;

        //Chequeo de recibimiento de parámetros
        if (args.length < 1) {
            throw new WriterException("ERROR: DEBE INDICAR AL MENOS UN ARGUMENTO (INPUT FILE)");
        }

        //Chequeo de extensión
        if (!args[0].endsWith(".s")) {
            throw new WriterException("ERROR: LA ENTRADA DEBE SER UN ARCHIVO .s");
        }

        //Se definen los paths y files.
        Path inputPath = Paths.get(args[0]);
        Path dirPath = inputPath.getParent();
        if (args.length == 2) {
            outputPath = dirPath.resolve(args[1] + ".asm");
        }
        else {
            fileName = inputPath.getFileName().toString();
            outputPath = dirPath.resolve(fileName.substring(0, fileName.length() - 2) + ".asm");
        }
        tempPath = dirPath.resolve("tempFile.tmp");
        tempFile = tempPath.toFile();

        boolean stop = false;
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(tempFile));
            writer.println("CORRECTO: ANALISIS LEXICO");
            writer.println("| TOKEN | LEXEMA |  NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) |");
            while (!stop) {
                token = lexicalAnalyzer.nextToken();
                writer.println("| " + token.getName().name() + " | " + token.getLexeme() + " | LINEA " + token.getRow() + " (COLUMNA " + token.getColumn() + ") |");

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
            throw ex;

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
}
