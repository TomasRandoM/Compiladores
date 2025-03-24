package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.ReaderException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

/**
 * Clase para modificar el reader de java y hacer más amigable su utilización
 * @author Tomás Rando
 */
public class ModifiedFileReader {
    private PushbackReader fileReader;

    /**
     * Constructor del ModifiedFileReader
     * @throws ReaderException Excepción del Reader
     * @param inputFile String. Archivo de entrada
     * @author Tomás Rando
     */
    public ModifiedFileReader(String inputFile) throws ReaderException {
        try {
            this.fileReader =  new PushbackReader(new FileReader(inputFile), 1);
        } catch (FileNotFoundException e) {
            throw new ReaderException("ARCHIVO NO ENCONTRADO. REINTENTE");
        }
    }

    /**
     * Lee un caracter y lo devuelve
     * @return Character
     * @throws ReaderException Exception para errores de lectura
     * @author Tomás Rando
     */
    public Character readChar() throws ReaderException {
        int e;
        try {
            e = fileReader.read();
            if (e == -1) {
                return null;
            } else {
                return (char) e;
            }
        } catch (IOException ex) {
            throw new ReaderException("ERROR LEYENDO CARACTER");
        }
    }

    /**
     * Nos permite deshacer la lectura de un caracter
     * @param c Caracter leido anteriormente
     * @throws ReaderException Exception por si hubiese algun error
     * @author Tomás Rando
     */
    public void unreadChar(char c) throws ReaderException {
        try {
            fileReader.unread(c);
        } catch (IOException ex) {
            throw new ReaderException("ERROR DESLEYENDO CARACTER");
        }
    }

}
