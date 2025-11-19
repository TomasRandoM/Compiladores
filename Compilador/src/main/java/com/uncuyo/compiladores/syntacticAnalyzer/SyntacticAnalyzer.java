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
    private Token auxLookahead = null;

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

    public void listaDeclaracionVariables() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.id_obj);
        listaDeclaracionVariables2();
    }

    public void listaDeclaracionVariables2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaDeclaracionVariables();
        } else if (lookahead.getName() == TokenTypes.semicolon) {
            return; // lambda
        } else {
            throw new SyntacticException("Se esperaba ',' o ';'");
        }
    }

    public void herencia() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.colon);
        tipo();
    }

    public void atributo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.ppub) {
            visibilidad();
            tipo();
            listaDeclaracionVariables();
            match(TokenTypes.semicolon);
        } else if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray || lookahead.getName() == TokenTypes.id_class) {
            tipo();
            listaDeclaracionVariables();
            match(TokenTypes.semicolon);
        }
    }

    public void atributos() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.ppub || lookahead.getName() == TokenTypes.pstr ||
                lookahead.getName() == TokenTypes.pbool || lookahead.getName() == TokenTypes.pint ||
                lookahead.getName() == TokenTypes.pdouble || lookahead.getName() == TokenTypes.parray ||
                lookahead.getName() == TokenTypes.id_class) {
            atributo();
            atributos();
        } else if (lookahead.getName() == TokenTypes.braces2) {
            return; //lambda
        }

    }

    public void class2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.colon) {
            herencia();
        }
        if (lookahead.getName() == TokenTypes.braces1) {
                match(TokenTypes.braces1);
                atributos();
                if (lookahead.getName() == TokenTypes.braces2) {
                    match(TokenTypes.braces2);
                } else {
                    throw new SyntacticException("Se esperaba '}'");
                }
        } else {
            throw new SyntacticException ("Se esperaba '{'");
        }
    }

    public void class1() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pclass);
        match(TokenTypes.id_class);
        class2();
    }

    public void impl() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pimpl);
        match(TokenTypes.id_class);
        match(TokenTypes.braces1);
        miembro();
        miembros();
        match(TokenTypes.braces2);
    }

    public void miembro() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pfn || lookahead.getName() == TokenTypes.pst) {
            metodo();
        } else if (lookahead.getName() == TokenTypes.dot) {
            constructor();
        }
    }

    public void miembros() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pfn || lookahead.getName() == TokenTypes.pst ||
                lookahead.getName() == TokenTypes.dot) {
            miembro();
            miembros();
        } else if (lookahead.getName() == TokenTypes.braces2) {
            return; //lambda
        } else {
            throw new SyntacticException("Se esperaba '}' o 'fn' o 'st' o '.'");
        }
    }

    public void bloqueMetodo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.braces1) {
            match(TokenTypes.braces1);
            masDeclVarLocales();
            sentencias();
        }
        else {
            throw new SyntacticException("Se esperaba '{'");
        }
    }

    public void sentencias() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.braces2) {
            match(TokenTypes.braces2);
        }
        else {
            if (lookahead.getName() == TokenTypes.braces1 ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pret ||
            lookahead.getName() == TokenTypes.pself ||
            lookahead.getName() == TokenTypes.id_class ||
            lookahead.getName() == TokenTypes.id_obj ||
            lookahead.getName() == TokenTypes.pwhile ||
            lookahead.getName() == TokenTypes.pif ||
            lookahead.getName() == TokenTypes.semicolon) {
                sentencia();
                sentencias();
            }
            else {
                throw new SyntacticException("Se esperaba '{', '(', 'ret', " +
                        "'self', 'while', 'if', ';' o identificador");
            }
        }
    }

    public void masDeclVarLocales() throws LexicalException, ReaderException, SyntacticException {
        if (lookahead.getName() == TokenTypes.id_class) {
            auxLookahead = lexicalAnalyzer.nextToken();
            if (auxLookahead.getName() == TokenTypes.id_obj) {
                declVarLocales();
                masDeclVarLocales();
            }
            else {
                if (auxLookahead.getName() == TokenTypes.brackets1 ||
                    auxLookahead.getName() == TokenTypes.dot ||
                    auxLookahead.getName() == TokenTypes.op_equal
                ) {
                    //Retorna, pues es lambda, pero se coloca al final del metodo
                }
                else {
                    throw new SyntacticException("Se esperaba '.', '[', '==' o identificador de objeto");
                }
            }
        }
        else {
            if (lookahead.getName() == TokenTypes.pstr ||
                    lookahead.getName() == TokenTypes.pbool ||
                    lookahead.getName() == TokenTypes.pint ||
                    lookahead.getName() == TokenTypes.pdouble ||
                    lookahead.getName() == TokenTypes.parray
            ) {
                declVarLocales();
                masDeclVarLocales();
            }
            else {
                if (lookahead.getName() == TokenTypes.semicolon ||
                        lookahead.getName() == TokenTypes.pif ||
                        lookahead.getName() == TokenTypes.pwhile ||
                        lookahead.getName() == TokenTypes.pself ||
                        lookahead.getName() == TokenTypes.pret ||
                        lookahead.getName() == TokenTypes.parentheses1 ||
                        lookahead.getName() == TokenTypes.braces1 ||
                        lookahead.getName() == TokenTypes.braces2 ||
                        lookahead.getName() == TokenTypes.id_obj
                ) {
                    //Retorna, pues es lambda, pero se coloca al final del metodo
                }
                else {
                    throw new SyntacticException("Se esperaba 'str', " +
                            "'bool', 'int', 'double', 'array', ';', 'while, " +
                            "'self', 'ret', '(', '{', '}' o identificador de objeto");
                }
            }
        }
        return;
    }

    public void declVarLocales() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pstr ||
            lookahead.getName() == TokenTypes.pbool ||
            lookahead.getName() == TokenTypes.pint ||
            lookahead.getName() == TokenTypes.pdouble ||
            lookahead.getName() == TokenTypes.parray ||
            lookahead.getName() == TokenTypes.id_class
        ) {
            tipo();
            listaDeclaracionVariables();
            match(TokenTypes.semicolon);
        }
        else {
            throw new SyntacticException("Se esperaba 'str', " +
                    "'bool', 'int', 'double', 'array' o identificador de clase");
        }
    }

    public void metodo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pfn) {
            match(TokenTypes.pfn);
            metodo2();
        }
        else {
            if (lookahead.getName() == TokenTypes.pst) {
                formaMetodo();
                match(TokenTypes.pfn);
                metodo2();
            }
            else {
                throw new SyntacticException("Se esperaba 'fn' o 'st'");
            }
        }
    }


    public void metodo2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pstr ||
            lookahead.getName() == TokenTypes.pbool ||
            lookahead.getName() == TokenTypes.pint ||
            lookahead.getName() == TokenTypes.pdouble ||
            lookahead.getName() == TokenTypes.parray ||
            lookahead.getName() == TokenTypes.id_class ||
            lookahead.getName() == TokenTypes.pvoid
        ) {
            tipoMetodo();
            match(TokenTypes.id_obj);
            argumentosFormales();
            bloqueMetodo();
        }
        else {
            if (lookahead.getName() == TokenTypes.id_obj) {
                match(TokenTypes.id_obj);
                argumentosFormales();
                bloqueMetodo();
            }
            else {
                throw new SyntacticException("Se esperaba 'str', 'bool', " +
                        "'int, 'double, 'array', 'void o identificador");
            }
        }
    }

    public void constructor() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            match(TokenTypes.dot);
            argumentosFormales();
            bloqueMetodo();
        }
        else {
            throw new SyntacticException("Se esperaba '.'");
        }
    }

    public void expresionParentizada2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pself ||
            lookahead.getName() == TokenTypes.id_class ||
            lookahead.getName() == TokenTypes.id_obj ||
            lookahead.getName() == TokenTypes.pnew ||
            lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub ||
            lookahead.getName() == TokenTypes.op_not ||
            lookahead.getName() == TokenTypes.op_increment ||
            lookahead.getName() == TokenTypes.op_decrement
        ) {
            expOr();
            match(TokenTypes.parentheses2);
            expresionParentizada3();
        }
        else {
            throw new SyntacticException("Se esperaba 'nil', 'true', " +
                    "'false', ')', 'self', '+', '-', '!', '++', " +
                    "'--', identificadores o constantes");
        }
    }


    public void expresionParentizada3() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void accesoSelf() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            match(TokenTypes.pself);
            accesoSelf2();
        }
        else {
            throw new SyntacticException("Se esperaba 'self'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    public void accesoSelf2() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void accesoVar2() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.brackets1) {
            match(TokenTypes.brackets1);
            expOr();
            match(TokenTypes.brackets2);
            accesoVar3();
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                    primarioFollows()
            ) {
                accesoVar3();
            }
            else {
                throw new SyntacticException("Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.', '['." +
                        " Se encontró: " + lookahead.getName());
            }
        }
    }

    public void accesoVar3() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }


    public void encadenado() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            match(TokenTypes.dot);
            if (lookahead.getName() == TokenTypes.id_obj) {
                match(TokenTypes.id_obj);
            }
            else {
                if (lookahead.getName() == TokenTypes.id_class) {
                    match(TokenTypes.id_class);
                }
                else {
                    throw new SyntacticException("Se esperaba identificador. " +
                            "Se encontró: " + lookahead.getName());
                }
            }
            encadenado2();
        }
        else {
            throw new SyntacticException("Se esperaba '.'. " +
                    "Se encontró: "  + lookahead.getName());
        }
    }

    public void encadenado2() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            llamadaMetodoEncadenado();
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.brackets1 ||
                primarioFollows()
            ) {
                accesoVariableEncadenado();
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'," +
                        " '[', '('. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void llamadaMetodoEncadenado() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            argumentosActuales();
            llamadaMetodoEncadenado2();
        }
        else {
            throw new SyntacticException("Se esperaba '('. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    public void llamadaMetodoEncadenado2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void accesoVariableEncadenado() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.brackets1) {
            match(TokenTypes.brackets1);
            expOr();
            match(TokenTypes.brackets2);
            accesoVariableEncadenado3();
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                    primarioFollows()
            ) {
                accesoVariableEncadenado3();
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void accesoVariableEncadenado3() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
            }
            else {
                throw new SyntacticException("Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    public void argumentosActuales() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            match(TokenTypes.parentheses1);
            argumentosActuales2();
        }
        else {
            throw new SyntacticException("Se esperaba '('. " +
                    "Se encontró: " +  lookahead.getName());
        }
    }

    public void argumentosActuales2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pself ||
            lookahead.getName() == TokenTypes.id_class ||
            lookahead.getName() == TokenTypes.id_obj ||
            lookahead.getName() == TokenTypes.pnew ||
            lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub ||
            lookahead.getName() == TokenTypes.op_not ||
            lookahead.getName() == TokenTypes.op_increment ||
            lookahead.getName() == TokenTypes.op_decrement
        ) {
            listaExpresiones();
            match(TokenTypes.parentheses2);
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses2) {
                match(TokenTypes.parentheses2);
            }
            else {
                throw new SyntacticException("Se esperaba 'nil', " +
                        "'true', 'false', '(', 'self', 'new', " +
                        "'+', '-', '!', '++', '--', ')', " +
                        "identificadores o constantes. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }

    public void listaExpresiones() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
                lookahead.getName() == TokenTypes.ptrue ||
                lookahead.getName() == TokenTypes.pfalse ||
                lookahead.getName() == TokenTypes.const_int ||
                lookahead.getName() == TokenTypes.const_string ||
                lookahead.getName() == TokenTypes.const_double ||
                lookahead.getName() == TokenTypes.parentheses1 ||
                lookahead.getName() == TokenTypes.pself ||
                lookahead.getName() == TokenTypes.id_class ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pnew ||
                lookahead.getName() == TokenTypes.op_sum ||
                lookahead.getName() == TokenTypes.op_sub ||
                lookahead.getName() == TokenTypes.op_not ||
                lookahead.getName() == TokenTypes.op_increment ||
                lookahead.getName() == TokenTypes.op_decrement
        ) {
            expOr();
            listaExpresiones2();
        }
        else {
            throw new SyntacticException("Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', 'new', " +
                    "'+', '-', '!', '++', '--', " +
                    "identificadores o constantes. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    public void listaExpresiones2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaExpresiones();
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses2) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException("Se esperaba ',', ')'." +
                        " Se encontró: " + lookahead.getName());
            }
        }
    }

    public void operando() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double
        ) {
            literal();
        }
        else {
            if (lookahead.getName() == TokenTypes.pself ||
                lookahead.getName() == TokenTypes.id_class ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pnew
            ) {
                primario();
            }
            else {
                throw new SyntacticException("Se esperaba 'nil', " +
                        "'true', 'false', 'self', 'new', " +
                        "identificadores o constantes. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    
    public void primario() {

    }


    public void primario3() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //Retorna, pues es lambda
            }
            else {
                throw new SyntacticException("Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    public boolean primarioFollows() {
        return (lookahead.getName() == TokenTypes.op_mult ||
                lookahead.getName() == TokenTypes.op_div ||
                lookahead.getName() == TokenTypes.op_mod ||
                lookahead.getName() == TokenTypes.pdiv ||
                lookahead.getName() == TokenTypes.op_sum ||
                lookahead.getName() == TokenTypes.op_sub ||
                lookahead.getName() == TokenTypes.op_rel_less ||
                lookahead.getName() == TokenTypes.op_rel_greater ||
                lookahead.getName() == TokenTypes.op_rel_lessequal ||
                lookahead.getName() == TokenTypes.op_rel_greaterequal ||
                lookahead.getName() == TokenTypes.op_rel_equal ||
                lookahead.getName() == TokenTypes.op_rel_notequal ||
                lookahead.getName() == TokenTypes.op_and ||
                lookahead.getName() == TokenTypes.op_or ||
                lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
        );
    }
    public void match(TokenTypes tokenType) throws LexicalException, ReaderException, SyntacticException {

        if (lookahead.getName() == tokenType) {
            if (auxLookahead != null) {
                lookahead = auxLookahead;
                auxLookahead = null;
            }
            else {
                lookahead = lexicalAnalyzer.nextToken();
            }
        } else {
            throw new SyntacticException("Se esperaba " + tokenType + " y se encontró " + lookahead.getName());
        }
    }


}
