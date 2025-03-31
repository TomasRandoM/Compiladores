package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.ReaderException;

import java.io.*;

/**
 * Clase para modificar el reader de java y hacer más amigable su utilización
 * @author Tomás Rando
 */
public class ModifiedFileReader {
    private BufferedReader fileReader;

    /**
     * Constructor del ModifiedFileReader
     * @throws ReaderException Excepción del Reader
     * @param inputFile String. Archivo de entrada
     * @author Tomás Rando
     */
    public ModifiedFileReader(String inputFile) throws ReaderException {
        try {
            this.fileReader =  new BufferedReader(new FileReader(inputFile));
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
            fileReader.mark(1);
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
    public void unreadChar() throws ReaderException {
        try {
            fileReader.reset();
        } catch (IOException ex) {
            throw new ReaderException("ERROR DESLEYENDO CARACTER");
        }
    }

}
