package com.uncuyo.compiladores.lexicalAnalyzer;

/**
 * Clase que representa al token devuelto. Posee el name (el tipo de token), el lexema, el valor, la columna y la fila (donde fueron encontrados)
 * @author Paulina Suden y Tomás Rando
 */
public class Token {
    private TokenTypes name;
    private String lexeme;
    private Number valor = null;
    private int column;
    private int row;

    /**
     * Constructor del Token
     * @param name TokenTypes (Tipo del token)
     * @param lexeme String con el lexema
     * @param valor Number con el valor del mismo
     * @param column int con la columna donde se encontró
     * @param row int con la fila donde se encontró
     * @author Paulina Suden y Tomás Rando
     */
    public Token(TokenTypes name, String lexeme, Number valor, int row, int column) {
        this.name = name;
        this.lexeme = lexeme;
        this.valor = valor;
        this.column = column;
        this.row = row;
    }

    //Getter del lexema
    public String getLexeme() {
        return lexeme;
    }
    //Setter del lexema
    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
    //Getter del valor
    public Number getValor() {
        return valor;
    }
    //Setter del valor
    public void setValor(Number valor) {
        this.valor = valor;
    }
    //Getter del token (tipo)
    public TokenTypes getName() {
        return name;
    }
    //Setter del nombre (o tipo) del token
    public void setName(TokenTypes name) {
        this.name = name;
    }
    //Setter de la columna
    public void setColumn(int column) {
        this.column = column;
    }
    //Getter de la columna
    public int getColumn() {
        return column;
    }
    //Setter de la fila
    public void setRow(int row) {
        this.row = row;
    }
    //Geter de la fila
    public int getRow() {
        return row;
    }

}
