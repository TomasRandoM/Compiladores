package com.uncuyo.compiladores.lexicalAnalyzer;

public class Token {
    private TokenTypes name;
    private String lexeme;
    private Number valor = null;
    private int column;
    private int row;

    Token(TokenTypes name, String lexeme, Number valor, int column, int row) {
        this.name = name;
        this.lexeme = lexeme;
        this.valor = valor;
        this.column = column;
        this.row = row;
    }

    public String getLexeme() {
        return lexeme;
    }
    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
    public Number getValor() {
        return valor;
    }
    public void setValor(Number valor) {
        this.valor = valor;
    }
    public TokenTypes getName() {
        return name;
    }
    public void setName(TokenTypes name) {
        this.name = name;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public int getColumn() {
        return column;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getRow() {
        return row;
    }

}
