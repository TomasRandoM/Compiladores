package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.ReaderException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ModifiedFileReader {
    private FileReader fileReader;

    public ModifiedFileReader() throws ReaderException {
        try {
            this.fileReader = new FileReader("entrada.txt");
        } catch (FileNotFoundException e) {
            throw new ReaderException("File not found. Please try again.");
        }
    }

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

}
