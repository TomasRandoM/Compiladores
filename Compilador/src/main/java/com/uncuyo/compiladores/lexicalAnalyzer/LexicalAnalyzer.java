package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.ReaderException;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class LexicalAnalyzer {

    private HashMap<String, TokenTypes> keywords;
    private ModifiedFileReader fileReader;
    private int row;
    private int column;

    public LexicalAnalyzer() throws ReaderException {
        keywords = new HashMap<>();
        initializeHash(keywords);
        fileReader = new ModifiedFileReader();
    }

    public Token nextToken(Character caracter) throws ReaderException {
        Character c = fileReader.readChar();
        while (c != null) {
            c = fileReader.readChar();

            if ( c == '/') {
                findComment();
            } else if (Character.isUpperCase(c)) {
                findIdClass();
            } else if (Character.isLowerCase(c)) {
                findIdentifierKeyword(); //objeto, variable, funcion. Se verifica también si es palabra reservada
            } else if (Character.isDigit(c)) {
                findNumber(); //entero o decimal
            } else if ( c == '"') {
                findStringConstant(); // termine "
            } else {
                if (c == ' ') {
                    //
                }
                if (c == '\n') {
                    column = 0;
                    row++;
                }

        }

    }
        return null;
    }

    private HashMap<String, TokenTypes> initializeHash(HashMap<String, TokenTypes> hash) {
        List<String> plist = List.of(
                "start", "class", "impl", "else", "false", "if", "ret", "while",
                "true", "nil", "new", "fn", "st", "pub", "self", "div",
                "Bool", "Str", "Int", "Double", "Object", "IO", "Array", "void"
        );

        List<TokenTypes> tokentypesList = List.of(
                TokenTypes.pstart, TokenTypes.pclass, TokenTypes.pimpl, TokenTypes.pelse, TokenTypes.pfalse,
                TokenTypes.pif, TokenTypes.pret, TokenTypes.pwhile, TokenTypes.ptrue, TokenTypes.pnil,
                TokenTypes.pnew, TokenTypes.pfn, TokenTypes.pst, TokenTypes.ppub, TokenTypes.pself,
                TokenTypes.pdiv, TokenTypes.pbool, TokenTypes.pstr, TokenTypes.pint, TokenTypes.pdouble,
                TokenTypes.pobject, TokenTypes.pio, TokenTypes.parray, TokenTypes.pvoid
        );

        for (int i = 0; i < plist.size(); i++) {
            hash.put(plist.get(i), tokentypesList.get(i));
        }
        plist = null;
        tokentypesList = null;
        return hash;
    }
}
