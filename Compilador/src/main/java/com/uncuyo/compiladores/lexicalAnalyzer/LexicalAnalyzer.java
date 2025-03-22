package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.utils.Addons;

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

    public Token nextToken(Character caracter) throws ReaderException, LexicalException {
        Character c = fileReader.readChar();
        while (c != null) {
            c = fileReader.readChar();

            if ( c == '/') {
                findComment();
            } else if (Addons.isUpperCase(c)) {
                findIdClass(c);
            } else if (Addons.isLowerCase(c)) {
                findIdentifierKeyword(c); //objeto, variable, funcion. Se verifica también si es palabra reservada
            } else if (Character.isDigit(c)) {
                findNumber(c); //entero o decimal
            } else if ( c == '"') {
                findStringConstant(); // termine "
            } else {
                if (c == ' ') {
                    column++;
                }
                if (c == '\n') {
                    column = 0;
                    row++;
                }

        }

    }
        return null;
    }

    /**
     * Metodo que identifica keywords o identificadores de objeto, variables o funciones
     * @param c Primer Character leído en nextToken()
     * @return Token resultante
     * @throws ReaderException Excepción causada por la lectura del archivo
     * @throws LexicalException Excepción causada por errores léxicos.
     */
    public Token findIdentifierKeyword(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(c);
        while (c != null) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null || c == ' ' || c == '\t' || c == ')' || c == '(' || c == ';') {
                fileReader.unreadChar(c);
                if (Addons.isLetter(lexeme.charAt(lexeme.length() - 1)) || Character.isDigit(c)) {
                    if (keywords.get(lexeme.toString()) != null) {
                        return (new Token(keywords.get(lexeme.toString()), lexeme.toString(), null, row, column));
                    } else {
                        keywords.put(lexeme.toString(), TokenTypes.id_obj);
                        return (new Token(TokenTypes.id_obj, lexeme.toString(), null, row, column));
                    }
                }
            } else {
                if (c != '\n') {
                    if (Addons.isLetter(c) || Character.isDigit(c) || c == '_') {
                        lexeme.append(c);
                    } else {
                        throw new LexicalException("Lexical Error in row " + row + ", column " + column +
                                ". Illegal character: " + c);
                    }
                }
            }
        }
        throw new LexicalException("Unexpected end of file at row " + row + ", column " + column);
    }

    /**
     * Metodo que identifica identificadores de clase o ciertos keywords (que empiezan con mayuscula)
     * @param c Primer Character leído en nextToken()
     * @return Token resultante
     * @throws ReaderException Excepción causada por la lectura del archivo
     * @throws LexicalException Excepción causada por errores léxicos.
     */
    public Token findIdClass(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(c);
        while (c != null) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null || c == ' ' || c == '\t' || c == ')' || c == '(' || c == ';') {
                fileReader.unreadChar(c);
                if (Addons.isLetter(lexeme.charAt(lexeme.length() - 1))) {
                    if (keywords.get(lexeme.toString()) != null) {
                        return (new Token(keywords.get(lexeme.toString()), lexeme.toString(), null, row, column));
                    } else {
                        keywords.put(lexeme.toString(), TokenTypes.id_class);
                        return (new Token(TokenTypes.id_class, lexeme.toString(), null, row, column));
                    }
                }
            } else {
                if (c != '\n') {
                    if (Addons.isLetter(c) || Character.isDigit(c) ||c == '_') {
                        lexeme.append(c);
                    } else {
                        throw new LexicalException("Lexical Error in row " + row + ", column " + column +
                                ". Illegal character: " + c);
                    }
                }
            }
        }
        throw new LexicalException("Unexpected end of file at row " + row + ", column " + column);
    }


    /**
     * Metodo que identifica constantes enteras y doubles
     * @param c Primer Character leído en nextToken()
     * @return Token resultante
     * @throws ReaderException Excepción causada por la lectura del archivo
     * @throws LexicalException Excepción causada por errores léxicos.
     */
    public Token findNumber(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(c);
        //1 es para identificar enteros y 2 para doubles
        int tipo = 1;
        while (c != null) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null || c == ' ' || c == '\t' || c == ')' || c == '(' || c == ';'
                    || Addons.isArithmeticOperator(c) || Addons.isRelationalOperator(c) || c == '\n') {
                fileReader.unreadChar(c);
                char aux = lexeme.charAt(lexeme.length() - 1);
                if (Addons.isLetter(aux) || Character.isDigit(aux)) {
                    if (tipo == 1) {
                        return (new Token(TokenTypes.const_int, lexeme.toString(), Integer.parseInt(lexeme.toString()),
                                row, column));
                    } else {
                        return (new Token(TokenTypes.const_double, lexeme.toString(),
                                Double.parseDouble(lexeme.toString()), row, column));
                    }

                }
            } else {
                if (Character.isDigit(c) || c == '.') {
                    if (c == '.') {
                        if (tipo == 2) {
                            throw new LexicalException("Lexical Error in row " + row + ", column " + column +
                                    ". Illegal character: " + c);
                        }
                        tipo = 2;
                    }
                    lexeme.append(c);
                } else {
                    throw new LexicalException("Lexical Error in row " + row + ", column " + column +
                            ". Illegal character: " + c);
                }
            }
        }
        throw new LexicalException("Unexpected end of file at row " + row + ", column " + column);
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
