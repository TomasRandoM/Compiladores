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
    private HashMap<String, TokenTypes> arithmeticOperator;
    private HashMap<String, TokenTypes> booleanOperator;
    private HashMap<String, TokenTypes> specialSymbol;

    private static ModifiedFileReader fileReader;
    private int row = 0;
    private int column = 0;

    /**
     * Constructor
     * @throws ReaderException Excepción del reader
     * @param inputFile String Archivo de entrada
     * @author Paulina Suden y Tomás Rando
     */
    public LexicalAnalyzer(String inputFile) throws ReaderException {
        keywords = new HashMap<>();
        initializeHash(keywords);
        arithmeticOperator = new HashMap<>();
        arithmeticOperatorHash(arithmeticOperator);
        booleanOperator = new HashMap<>();
        booleanOperatorHash(booleanOperator);
        specialSymbol = new HashMap<>();
        specialSymbolHash(specialSymbol);
        fileReader = new ModifiedFileReader(inputFile);
    }

    /**
     * Método que devuelve el siguiente token encontrado en el código fuente
     * @return Token que representa el siguiente token encontrado
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionado por un error léxico
     * @author Paulina Suden y Tomás Rando
     */
    public Token nextToken() throws ReaderException, LexicalException {
        boolean stop = false;
        Token token = null;
        Character c = fileReader.readChar();
        sumRowAndColumn(c);
        while (!stop) {
            if (c == null) {
                token = new Token(TokenTypes.end_of_file, "", null, row, column - 1);
            }
            else if  ( c == '/') {
                Character cc = fileReader.readChar();
                sumRowAndColumn(cc);
                if (cc == '/' || cc == '*') {
                    findComment(cc);
                    token = nextToken();
                }
                else {
                    unreadChar(cc);
                    token = arithmeticOperation(c);
                }
            }
            else if (Addons.isUpperCase(c)) {
                token = findIdClass(c);
            }
            else if (Addons.isLowerCase(c)) {
                token = findIdentifierKeyword(c);
            }
            else if (Character.isDigit(c)) {
                token = findNumber(c);
            }
            else if ( c == '"') {
                token = findStringConstant();
            }
            else if (Addons.isArithmeticOperator(c)) {
                token = arithmeticOperation(c);
            }
            else if (isRelationalOperator(c)) {
                token = relationalOperation(c);
            }
            else if (Addons.isBooleanOperator(c)) {
                token = booleanOperation(c);
            }
            else if (c == '=') {
                token = assignmentOperator(c);
            }
            else if (Addons.isSpecialSymbol(c)) {
                token = specialSymbol(c);
            }
            else {
                if (c == '\n' || c == ' ' || c == '\r') {
                    c = fileReader.readChar();
                    sumRowAndColumn(c);
                } else {
                    throw new LexicalException("CARACTER ILEGAL", column, row);
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
            /*
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
             */
            if (c == null || (!(Addons.isLetter(c) || Character.isDigit(c) || c == '_'))) {
                unreadChar(c);
                if (keywords.get(lexeme.toString()) != null) {
                    token = new Token(keywords.get(lexeme.toString()), lexeme.toString(),
                            null, row, column - lexeme.length());
                }
                else {
                    keywords.put(lexeme.toString(), TokenTypes.id_obj);
                    token = new Token(TokenTypes.id_obj, lexeme.toString(),
                            null, row, column - lexeme.length());
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
            /*
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
             */
            if (c == null || (!(Addons.isLetter(c) || Character.isDigit(c) || c == '_'))) {
                unreadChar(c);
                if (Addons.isLetter(lexeme.charAt(lexeme.length() - 1))) {
                    if (keywords.get(lexeme.toString()) != null) {
                        token = new Token(keywords.get(lexeme.toString()), lexeme.toString(),
                                null, row, column - lexeme.length());
                    }
                    else {
                        keywords.put(lexeme.toString(), TokenTypes.id_class);
                        token = new Token(TokenTypes.id_class, lexeme.toString(),
                                null, row, column - lexeme.length());
                    }
                    stop = true;
                }
                else {
                    String id = lexeme.toString();
                    throw new LexicalException("IDENTIFICADOR DE CLASE INCORRECTO " + id, column, row);
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
            /*
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO", column, row);
            }
             */
            if (c == null || (!(Character.isDigit(c) || c == '.'))) {
                unreadChar(c);
                if (tipo == 1) {
                    try {
                        int num = Integer.parseInt(lexeme.toString());
                    }
                    catch (NumberFormatException e) {
                        throw new LexicalException("NUMERO FUERA DE LIMITES", column, row);
                    }
                    token = new Token(TokenTypes.const_int, lexeme.toString(),
                            Integer.parseInt(lexeme.toString()),
                            row, column - lexeme.length());
                }
                else {
                    try {
                        double num = Double.parseDouble(lexeme.toString());
                    }
                    catch (NumberFormatException e) {
                        throw new LexicalException("NUMERO FUERA DE LIMITES", column, row);
                    }
                    token = new Token(TokenTypes.const_double, lexeme.toString(),
                            Double.parseDouble(lexeme.toString()), row,
                            column - lexeme.length());
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
     * Método para identificar literales cadenas
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocado por un error léxico
     * @author Paulina Suden
     */
    public Token findStringConstant() throws ReaderException, LexicalException {
        StringBuilder lexeme = new StringBuilder();
        Character c;
        while (lexeme.length() < 1024) {
            c = fileReader.readChar();
            sumRowAndColumn(c);
            if (c == null) {
                throw new LexicalException("END OF FILE INESPERADO. SE ESPERABA CIERRE DE COMILLAS", column, row);
            }

            if (c == '\n') {
                throw new LexicalException("SALTO DE LINEA INESPERADO. SE ESPERABA CIERRE DE COMILLAS", column, row);
            }

            if (!Addons.isInGrammar(c) && c != '\t' && c != ' ') {
                throw new LexicalException("SIMBOLO INVALIDO: " + c, column, row);
            }

            if (c == '"') {
                return new Token(TokenTypes.const_string, lexeme.toString(),
                        null, row, column - lexeme.length() - 2);
            }

            lexeme.append(c);
        }

        throw new LexicalException("SE SUPERO EL TAMAÑO MAXIMO DE CADENA (1024 CARACTERES)",column,row);
    }

    /**
     * Método para identificar comentario simple o multilínea
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocado por un error léxico
     * @author Paulina Suden
     */
    public void findComment(Character c) throws ReaderException, LexicalException {
        if (c == '/') {
            boolean stop = false;
            while (!stop) {
                c = fileReader.readChar();
                column++;
                if (c == null || c == '\n') {
                    stop = true;
                }
                else {
                    if (!Addons.isInGrammar(c) && c != '\t' && c != '\r' && c != ' ') {
                        throw new LexicalException("SIMBOLO INVALIDO: " + c, column, row);
                    }
                }
            }
            column = 0;
            row++;

        }
        else if (c == '*') {
            boolean endFound = false;
            while ((c = fileReader.readChar()) != null) {
                sumRowAndColumn(c);

                if (c == '*' && (c = fileReader.readChar()) == '/') {
                    sumRowAndColumn(c);
                    endFound = true;
                    break;
                }
                else {
                    if (!Addons.isInGrammar(c) && c != '\t' && c != '\n' && c != '\r' && c != ' ') {
                        throw new LexicalException("SIMBOLO INVALIDO: " + c, column, row);
                    }
                }
            }

            if (!endFound) {
                throw new LexicalException("END OF FILE INESPERADO EN COMENTARIO MULTILINEA", column, row);
            }

        }
        else {
            throw new LexicalException("SE ESPERABA '/' O '*'", column, row);
        }
        return;
    }

    /**
     * Metodo para identificar operadores relacionales
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @author Paulina Suden
     */
    public Token relationalOperation(Character c) throws ReaderException {
        Token token = null;

        if (c == '<' || c == '>') {
            Character cc = fileReader.readChar();
            sumRowAndColumn(cc);
            if (cc == '=') { // ++ o --
                token = new Token(c == '<' ? TokenTypes.op_rel_lessequal : TokenTypes.op_rel_greaterequal,
                        c.toString() + cc, null, row,
                        column - (c.toString() + cc).length());
            }
            else {
                unreadChar(cc);
                token = new Token(c == '<' ? TokenTypes.op_rel_less : TokenTypes.op_rel_greater,
                        c.toString() + cc, null, row,
                        column - (c.toString() + cc).length());
            }
        }
        else if (c == '!' || c == '=') {
            Character cc = fileReader.readChar();
            sumRowAndColumn(cc);
            token = new Token(c == '!' ? TokenTypes.op_rel_notequal : TokenTypes.op_rel_equal,
                    c.toString() + cc, null, row,
                    column - (c.toString() + cc).length());
        }
        return token;
    }

    /**
     * Metodo para identificar operadores aritmeticos
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @author Paulina Suden
     */
    public Token arithmeticOperation(Character c) throws ReaderException {
        Token token = null;
        if (c == '+' || c == '-') {
            Character cc = fileReader.readChar();
            sumRowAndColumn(cc);
            if (cc == c) { // ++ o --
                token = new Token(c == '+' ? TokenTypes.op_increment : TokenTypes.op_decrement,
                        c.toString() + cc, null, row, column - (c.toString() + cc).length());
            }
            else {
                if (cc != null) {
                    unreadChar(cc);
                }
                token = new Token(arithmeticOperator.get(c.toString()), c.toString(),
                        null, row, column - c.toString().length());
            }
        }
        else {
            token = new Token(arithmeticOperator.get(c.toString()), c.toString(),
                    null, row, column - c.toString().length());
        }
        return token;
    }

    /**
     * Metodo para identificar operadores booleanos
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocada por un error léxico
     * @author Paulina Suden
     */
    public Token booleanOperation(Character c) throws LexicalException, ReaderException {
        Token token = null;
        if (c == '&' || c == '|') {
            Character cc = fileReader.readChar();
            sumRowAndColumn(cc);
            if (cc == c) {
                token = new Token(c == '&' ? TokenTypes.op_and : TokenTypes.op_or,
                        c.toString() + cc, null, row, column - (c.toString() + cc).length());
            }
            else {
                unreadChar(cc);
                throw new LexicalException("OPERADOR BOOLEANO NO VALIDO: "+c,column,row);
            }
        }
        else if (c == '!') {
            token = new Token(TokenTypes.op_not, c.toString(), null, row,
                    column - c.toString().length());
        }
        else {
            throw new LexicalException("OPERADOR BOOLEANO NO VALIDO: "+c,column,row);
        }
        return token;
    }

    /**
     * Metodo para identificar operador de asignacion
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción provocada por un error léxico
     * @author Paulina Suden
     */
    public Token assignmentOperator(Character c) throws LexicalException, ReaderException {
        Token token = null;
        Character cc = fileReader.readChar();
        sumRowAndColumn(cc);
        if (cc == c) {
            throw new LexicalException("OPERADOR DE ASIGNACIÓN NO VÁLIDO", column, row);
        }
        unreadChar(cc);
        token = new Token(TokenTypes.op_equal,c.toString(),null,row,column - c.toString().length());
        return token;
    }

    /**
     * Metodo para identificar simbolos especiales
     * @param c Character leído anteriormente en nextToken()
     * @return Token con todos los atributos
     * @throws LexicalException Excepción provocada por un error léxico
     * @author Paulina Suden
     */
    public Token specialSymbol(Character c) throws LexicalException {
        String key = c.toString();
        if (specialSymbol.get(key) != null) {
            return new Token(specialSymbol.get(key), key, null, row, column - key.length());
        }
        else {
            throw new LexicalException("SIMBOLO ESPECIAL NO RECONOCIDO: "+c, column, row);
        }
    }

    /**
     * Método para contar las filas y columnas
     * @param c Character
     * @author Paulina Suden
     */
    public void sumRowAndColumn(Character c) {
        if (c == null) {
            column++;
        }
        else if (c == '\n') {
            column = 0;
            row++;
        }
        else if (c == '\t') {
            column += 1;
        }
        else {
            column++;
        }
    }

    /**
     * Gestiona los números de fila y columna a la hora de desleer un carácter.
     * @param c Character
     * @throws ReaderException Excepción que nos dice que el Reader falló al desleer el carácter
     * @author Tomás Rando
     */
    public void unreadChar(Character c) throws ReaderException {
        if (c == null) {
            column--;
        }
        else if (c == '\n') {
            column = 0;
            row--;
        }
        else if (c == '\t') {
            column -= 1;
        }
        else {
            column--;
        }
        fileReader.unreadChar();
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

    /**
     * Función para inicializar el hash con los operadores aritméticos y sus respectivos tokens
     * @param hash HashMap<String, TokenTypes> anteriormente declarado
     * @return HashMap<String, TokenTypes> inicializado
     * @author Paulina Suden
     */
    private HashMap<String, TokenTypes> arithmeticOperatorHash(HashMap<String, TokenTypes> hash) {
        List<String> plist = List.of("*", "+", "-", "/", "%", "++", "--");

        List<TokenTypes> tokentypesList = List.of(
                //op_sum, op_div, op_mult, op_sub, op_divdouble, op_increment, op_decrement,
                TokenTypes.op_mult, TokenTypes.op_sum, TokenTypes.op_sub, TokenTypes.op_div,
                TokenTypes.op_mod, TokenTypes.op_increment, TokenTypes.op_decrement);

        for (int i = 0; i < plist.size(); i++) {
            hash.put(plist.get(i), tokentypesList.get(i));
        }
        plist = null;
        tokentypesList = null;
        return hash;
    }

    /**
     * Función para inicializar el hash con los operadores booleanos y sus respectivos tokens
     * @param hash HashMap<String, TokenTypes> anteriormente declarado
     * @return HashMap<String, TokenTypes> inicializado
     * @author Paulina Suden
     */
    private HashMap<String, TokenTypes> booleanOperatorHash(HashMap<String, TokenTypes> hash) {
        List<String> plist = List.of("&&", "||", "!");
        List<TokenTypes> tokentypesList = List.of(TokenTypes.op_and, TokenTypes.op_or, TokenTypes.op_not);

        for (int i = 0; i < plist.size(); i++) {
            hash.put(plist.get(i), tokentypesList.get(i));
        }
        return hash;
    }

    /**
     * Función para inicializar el hash con los simbolos especiales y sus respectivos tokens
     * @param hash HashMap<String, TokenTypes> anteriormente declarado
     * @return HashMap<String, TokenTypes> inicializado
     * @author Paulina Suden
     */
    private HashMap<String, TokenTypes> specialSymbolHash(HashMap<String, TokenTypes> hash) {
        List<String> plist = List.of("[", "]", "(", ")", "{", "}", ".", ",", ";");
        List<TokenTypes> tokentypesList = List.of(TokenTypes.brackets1, TokenTypes.brackets2,
                TokenTypes.parentheses1, TokenTypes.parentheses2, TokenTypes.braces1,
                TokenTypes.braces2, TokenTypes.dot, TokenTypes.comma, TokenTypes.semicolon);

        for (int i = 0; i < plist.size(); i++) {
            hash.put(plist.get(i), tokentypesList.get(i));
        }
        return hash;
    }




    /**
     * Verifica que un caracter sea un operador relacional
     * @param c Character
     * @return boolean. True si es un operador, false si no
     * @author Tomás Rando y Paulina Suden
     */
    public boolean isRelationalOperator(Character c) throws ReaderException {
        boolean is = false;
        if (c == '<' || c == '>') {
            is = true;
        }
        else if (c == '=' || c == '!'){
            Character cc = fileReader.readChar();
            sumRowAndColumn(cc);
            if (cc == '=') {
                is = true;
                unreadChar(cc);
            }
            else {
                unreadChar(cc);
            }
        }
        return is;
    }
}
