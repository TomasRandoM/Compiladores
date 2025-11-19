package com.uncuyo.compiladores.syntacticAnalyzer;

import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.lexicalAnalyzer.LexicalAnalyzer;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;

public class SyntacticAnalyzer {

    private LexicalAnalyzer lexicalAnalyzer;
    private Token lookahead;

    public SyntacticAnalyzer(String inputFile) throws ReaderException{
        try {
            lexicalAnalyzer = new LexicalAnalyzer(inputFile);
        } catch (ReaderException ex) {
            throw ex;
        }
    }
/*
    public void program() throws LexicalException, ReaderException {
        try {
            lookahead = lexicalAnalyzer.nextToken();
        } catch (LexicalException | ReaderException ex) {
            throw ex;
        }
        listaDefiniciones();
        start();
    }

    public void start() LexicalException, ReaderException, SyntacticException {
        match(TokenTypes.pstart);
        bloque_metodo();
    }

    public void listaDefiniciones() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pclass) {
            class1();
            listaDefiniciones();
        } else if  (lookahead.getName() == TokenTypes.pimpl){
            impl();
            listaDefiniciones();
        } else if (lookahead.getName() == TokenTypes.pstart){
            return;
        } else {
            throw new SyntacticException("Se esperaba otra cosa");
        }

    }

    public void class1() {

    }

 */

    public void visibilidad() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.ppub);
    }

    public void formaMetodo() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pst);
    }

    public void tipoPrimitivo() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case pstr -> match(TokenTypes.pstr);
            case pbool -> match(TokenTypes.pbool);
            case pint -> match(TokenTypes.pint);
            case pdouble -> match(TokenTypes.pdouble);
            default -> throw new SyntacticException("Se esperaba un tipo primitivo (str, bool, int, double)");
        }
    }

    public void tipo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parray) {
            match(TokenTypes.parray);
            tipoPrimitivo();
        } else if (lookahead.getName() == TokenTypes.id_class) {
            match(TokenTypes.id_class);
        } else if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                   lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble) {
            tipoPrimitivo();
        } else {
            throw new SyntacticException("Se esperaba otra cosa");
        }
    }

    public void tipoMetodo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pvoid) {
            match(TokenTypes.pvoid);
        } else if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray || lookahead.getName() == TokenTypes.id_class) {
            tipo();
        } else {
            throw new SyntacticException("Se esperaba otra cosa");
        }
    }

    public void opIgual() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_rel_equal) {
            match(TokenTypes.op_rel_equal);
        } else if (lookahead.getName() == TokenTypes.op_rel_notequal) {
            match(TokenTypes.op_rel_notequal);
        } else {
            throw new SyntacticException("Se esperaba otra cosa");
        }
    }

    public void opCompuesto() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_rel_less -> match(TokenTypes.op_rel_less);
            case op_rel_greater -> match(TokenTypes.op_rel_greater);
            case op_rel_greaterequal -> match(TokenTypes.op_rel_greaterequal);
            case op_rel_lessequal -> match(TokenTypes.op_rel_lessequal);
            default -> throw new SyntacticException("Se esperaba un operador relacional (>, >=, <, <=)");
        }
    }

    public void opAdd() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_sum) {
            match(TokenTypes.op_sum);
        } else if (lookahead.getName() == TokenTypes.op_sub) {
            match(TokenTypes.op_sub);
        } else {
            throw new SyntacticException("Se esperaba un operador de suma o resta");
        }
    }

    public void opUnario() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_sum -> match(TokenTypes.op_sum);
            case op_sub -> match(TokenTypes.op_sub);
            case op_not -> match(TokenTypes.op_not);
            case op_decrement -> match(TokenTypes.op_decrement);
            case op_increment -> match(TokenTypes.op_increment);
            default -> throw new SyntacticException("Se esperaba un operador unario");
        }
    }

    public void opMul() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_mult -> match(TokenTypes.op_mult);
            case op_div -> match(TokenTypes.op_div);
            case op_mod -> match(TokenTypes.op_mod);
            case pdiv -> match(TokenTypes.pdiv);
            default -> throw new SyntacticException("Se esperaba un operador aritmético??");
        }
    }

    public void literal() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case pnil -> match(TokenTypes.pnil);
            case ptrue -> match(TokenTypes.ptrue);
            case pfalse -> match(TokenTypes.pfalse);
            case const_int -> match(TokenTypes.const_int);
            case const_string -> match(TokenTypes.const_string);
            case const_double -> match(TokenTypes.const_double);
            default -> throw new SyntacticException("Se esperaba un literal??");
        }
    }

    public void argumentosFormales() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.parentheses1);
        argumentosFormales2();
    }

    public void argumentosFormales2() throws LexicalException, SyntacticException, ReaderException {
        // chequeamos los primeros de 'argumentoFormal' por las reglas 41, 43 y 46
        if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray || lookahead.getName() == TokenTypes.id_class) {

            listaArgumentosFormales();
            match(TokenTypes.parentheses2);
        }
        // (cuando viene ')')
        else if (lookahead.getName() == TokenTypes.parentheses2) {
            match(TokenTypes.parentheses2);
        } else {
            throw new SyntacticException("Se esperaba un tipo o ')'");
        }
    }

    public void listaArgumentosFormales2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaArgumentosFormales();
        } else if (lookahead.getName() == TokenTypes.parentheses2) {
            return; // lambda
        } else {
            throw new SyntacticException("Se esperaba ',' o ')'");
        }
    }

    public void listaArgumentosFormales() throws LexicalException, SyntacticException, ReaderException {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    public void argumentoFormal() throws LexicalException, SyntacticException, ReaderException {
        tipo();
        if (lookahead.getName() == TokenTypes.id_obj) {
            match(TokenTypes.id_obj);
        } else {
            throw new SyntacticException("Se esperaba un identificador de objeto");
        }
    }



    public void match(TokenTypes tokenType) throws LexicalException, ReaderException, SyntacticException {

        if (lookahead.getName() == tokenType) {
            lookahead = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntacticException("Se esperaba " + tokenType + " y se encontró " + lookahead.getName());
        }
    }


}
