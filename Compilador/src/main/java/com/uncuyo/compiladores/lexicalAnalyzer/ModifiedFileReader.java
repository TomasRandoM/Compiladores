package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.ReaderException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class ModifiedFileReader {
    private PushbackReader fileReader;

    public ModifiedFileReader() throws ReaderException {
        try {
            this.fileReader =  new PushbackReader(new FileReader("entrada.txt"), 1);
        } catch (FileNotFoundException e) {
            throw new ReaderException("File not found. Please try again.");
        }
    }

    /**
     * Lee un caracter y lo devuelve
     * @return Character
     * @throws ReaderException Exception para errores de lectura
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
            throw new ReaderException("Error in reading char");
        }
    }

    /**
     * Nos permite deshacer la lectura de un caracter
     * @param c Caracter leido anteriormente
     * @throws ReaderException Exception por si hubiese algun error
     */
    public void unreadChar(char c) throws ReaderException {
        try {
            fileReader.unread(c);
        } catch (IOException ex) {
            throw new ReaderException("Error in unreading char");
        }
    }

}
