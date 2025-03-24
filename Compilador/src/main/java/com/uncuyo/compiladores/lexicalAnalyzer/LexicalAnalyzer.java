package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.utils.Addons;

import java.util.HashMap;
import java.util.List;

/**
 * Clase que representa al analizador léxico
 * @author Paulina Suden y Tomás Rando
 */
public class LexicalAnalyzer {

    private HashMap<String, TokenTypes> keywords;
    private ModifiedFileReader fileReader;
    private int row;
    private int column;

    /**
     * Constructor
     * @throws ReaderException Excepción del reader
     * @param inputFile String Archivo de entrada
     * @author Paulina Suden y Tomás Rando
     */
    public LexicalAnalyzer(String inputFile) throws ReaderException {
        keywords = new HashMap<>();
        initializeHash(keywords);
        fileReader = new ModifiedFileReader(inputFile);
    }

    /**
     * Función que devuelve el siguiente token encontrado en el código fuente
     * @return Token que representa el siguiente token encontrado
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionado por un error léxico
     * @author Paulina Suden y Tomás Rando
     */
    public Token nextToken() throws ReaderException, LexicalException {
        boolean stop = false;
        Token token = null;
        Character c = fileReader.readChar();
        if (c == null) {
            token = new Token(TokenTypes.end_of_file, "", null, row, column);
            stop = true;
        }
        while (!stop) {

            if ( c == '/') {
                token = findComment();
            }
            else if (Addons.isUpperCase(c)) {
                token = findIdClass(c);
            }
            else if (Addons.isLowerCase(c)) {
                token = findIdentifierKeyword(c); //objeto, variable, funcion. Se verifica también si es palabra reservada
            }
            else if (Character.isDigit(c)) {
                token = findNumber(c); //entero o decimal
            }
            else if ( c == '"') {
                token = findStringConstant(); // strings
            }
            else {
                if (c == ' ') {
                    column++;
                    c = fileReader.readChar();
                }
                else if (c == '\n') {
                    column = 0;
                    row++;
                    c = fileReader.readChar();
                }
            }

            if (token != null) {
                stop = true;
            }
        }
        return token;
    }

    /**
     * Metodo para identificar identificadores de objeto/funcion (o ciertas keywords)
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocado por un error léxico
     * @author Tomás Rando
     */
    public Token findIdentifierKeyword(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        Token token = null;
        boolean stop = false;
        lexeme.append(c);
        while (!stop) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
            if (!(Addons.isLetter(c) || Character.isDigit(c) || c == '_')) {
                fileReader.unreadChar(c);
                if (keywords.get(lexeme.toString()) != null) {
                    token = new Token(keywords.get(lexeme.toString()), lexeme.toString(), null, row, column);
                }
                else {
                    keywords.put(lexeme.toString(), TokenTypes.id_obj);
                    token = new Token(TokenTypes.id_obj, lexeme.toString(), null, row, column);
                }
                stop = true;
            }
            else {
                lexeme.append(c);
            }
        }
        return token;
    }
    /*
        POSIBLE IF PARA findIdentifierKeyword() SI NO PUDIESE TERMINAR CON _
        if (Addons.isLetter(lexeme.charAt(lexeme.length() - 1)) || Character.isDigit(c)) { }
     */

    /**
     * Metodo para identificar identificadores de clase (o ciertas keywords)
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocado por un error léxico
     * @author Tomás Rando
     */
    public Token findIdClass(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        boolean stop = false;
        Token token = null;
        lexeme.append(c);
        while (!stop) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
            if (!(Addons.isLetter(c) || Character.isDigit(c) || c == '_')) {
                fileReader.unreadChar(c);
                if (Addons.isLetter(lexeme.charAt(lexeme.length() - 1))) {
                    if (keywords.get(lexeme.toString()) != null) {
                        token = new Token(keywords.get(lexeme.toString()), lexeme.toString(), null, row, column);
                    }
                    else {
                        keywords.put(lexeme.toString(), TokenTypes.id_class);
                        token = new Token(TokenTypes.id_class, lexeme.toString(), null, row, column);
                    }
                    stop = true;
                }
                else {
                    throw new LexicalException("IDENTIFICADOR DE CLASE INCORRECTO ", column, row);
                }
            }
            else {
                lexeme.append(c);
            }
        }
        return token;
    }


    /**
     * Metodo para identificar int o double
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocado por un error léxico
     * @author Tomás Rando
     */
    public Token findNumber(Character c) throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        //1 es para identificar enteros y 2 para doubles
        int tipo = 1;
        boolean stop = false;
        Token token = null;
        lexeme.append(c);

        while (!stop) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
            if (!(Character.isDigit(c) || c == '.')) {
                fileReader.unreadChar(c);
                char aux = lexeme.charAt(lexeme.length() - 1);
                if (tipo == 1) {
                    token = new Token(TokenTypes.const_int, lexeme.toString(), Integer.parseInt(lexeme.toString()),
                            row, column);
                }
                else {
                    token = new Token(TokenTypes.const_double, lexeme.toString(),
                            Double.parseDouble(lexeme.toString()), row, column);
                }
                stop = true;
            }
            else {
                if (c == '.') {
                    if (tipo == 2) {
                        throw new LexicalException("CARACTER ILEGAL: " + c, column, row);
                    }
                    tipo = 2;
                }
                lexeme.append(c);
            }
        }
        return token;
    }

    /**
     * Función para inicializar el hash con las palabras reservadas y sus respectivos tokens
     * @param hash HashMap<String, TokenTypes> anteriormente declarado
     * @return HashMap<String, TokenTypes> inicializado
     * @author Paulina Suden y Tomás Rando
     */
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
