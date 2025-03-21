package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class LexicalAnalyzer {

    private HashMap<String, TokenTypes> keywords;
    private ModifiedFileReader fileReader;
    private int row = 0;
    private int column = 0;

    public LexicalAnalyzer() throws ReaderException {
        keywords = new HashMap<>();
        initializeHash(keywords);
        fileReader = new ModifiedFileReader();
    }

    public Token nextToken(Character caracter) throws ReaderException, LexicalException {
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
                Token token = findStringConstant();
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

    public Token findStringConstant() throws ReaderException, LexicalException {
        int cont = 0;
        StringBuilder lexeme = new StringBuilder();
        Character c = fileReader.readChar();
        sumRowAndColumn(c);
        try {
            while (cont < 1024) {
                if (c == '"') {
                    break;
                }
                lexeme.append(c);
                c = fileReader.readChar();
                sumRowAndColumn(c);
                cont++;
            }
            if (cont == 1024) {
                throw new LexicalException("Error léxico");
            }
            return new Token(TokenTypes.const_string, lexeme.toString(), null, column, row);
        } catch (ReaderException e) {
            throw new ReaderException("Error léxico (del Reader)");
        }
    }

    public void findComment() throws ReaderException, LexicalException {
        Character c = fileReader.readChar();
        if (c == '/') {
            while (c != '\n') {
                c = fileReader.readChar();
            }
            column = 0;
            row++;
            c = fileReader.readChar();
            nextToken(c);
        } else if (c == '*') {
            c = fileReader.readChar();
            while (c != '*') {
                c = fileReader.readChar();
                sumRowAndColumn(c);
            }
            c = fileReader.readChar();
            if (c=='/'){
                column++;
                c = fileReader.readChar();
                sumRowAndColumn(c);
            } else {
                throw new LexicalException("Error léxico");
            }
        } else {
            throw new LexicalException("Error léxico");
        }
    }

    //funcion para que cuente filas y columnas
    public void sumRowAndColumn(Character c) {
        if (c == '\n' || c == '\r') {
            column = 0;
            row++;
        } else if (c == '\t') {
            column += 4;
        } else {
            column++;
        }
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
