package com.uncuyo.compiladores.syntacticAnalyzer;

import com.uncuyo.compiladores.exceptions.*;
import com.uncuyo.compiladores.lexicalAnalyzer.LexicalAnalyzer;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

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

    /**
     * ⟨Program⟩ ::= ⟨Lista-Definiciones⟩ ⟨Start⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void program() throws LexicalException, ReaderException, SyntacticException, SemanticException {
        lookahead = lexicalAnalyzer.nextToken();
        SymbolTable.addPredefinedClasses();
        listaDefiniciones();
        start();
        match(TokenTypes.end_of_file);
        System.out.println("CORRECTO: ANÁLISIS SINTÁCTICO");
        SymbolTable.checkDeclarations();
        System.out.println("CORRECTO: ANÁLISIS SEMÁNTICO");

    }

    /**
     * ⟨Start⟩ ::= pstart ⟨Bloque-Metodo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void start() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        Method method = new Method(lookahead, null, false);
        SymbolTable.setCurrentMethod(method);
        SymbolTable.setStartMethodStored(method);
        match(TokenTypes.pstart);
        bloqueMetodo();
    }

    /**
     * <Lista-Definiciones> ::= <Class> <Lista-Definiciones>
     * <Lista-Definiciones> ::= <Impl> <Lista-Definiciones>
     * <Lista-Definiciones> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaDefiniciones() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pclass) {
            class1();
            listaDefiniciones();
        } else if  (lookahead.getName() == TokenTypes.pimpl){
            impl();
            listaDefiniciones();
        } else if (lookahead.getName() == TokenTypes.pstart){
            //retorna, pues es lambda
        } else {
            throw new SyntacticException(lookahead, "Se esperaba 'class', 'impl' " +
                    "o 'start'. Se encontró: " + lookahead.getName());
        }

    }

    /**
     * ⟨Visibilidad⟩ ::= ppub
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void visibilidad() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.ppub);
    }

    /**
     * ⟨Forma-Metodo⟩ ::= pst
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void formaMetodo() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pst);
    }

    /**
     * ⟨Tipo-Primitivo⟩ ::= pstr
     * ⟨Tipo-Primitivo⟩ ::= pbool
     * ⟨Tipo-Primitivo⟩ ::= pint
     * ⟨Tipo-Primitivo⟩ ::= pdouble
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public Type tipoPrimitivo() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        switch (lookahead.getName()) {
            case pstr -> {
                match(TokenTypes.pstr);
                return new Type(token,"Str");
            }
            case pbool -> {
                match(TokenTypes.pbool);
                return new Type(token,"Bool");
            }
            case pint -> {
                match(TokenTypes.pint);
                return new Type(token, "Int");
            }
            case pdouble -> {
                match(TokenTypes.pdouble);
                return new Type(token,"Double");
            }
            default -> throw new SyntacticException(lookahead, "Se esperaba un tipo primitivo (str, bool, int, double). Se encontró: "+ lookahead.getName());
        }
    }

    /**
     * ⟨Tipo⟩ ::= ⟨Tipo-Primitivo⟩
     * ⟨Tipo⟩ ::= parray ⟨Tipo-Primitivo⟩
     * ⟨Tipo⟩ ::=  id_class
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public Type tipo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parray) {
            Type type = new Type(lookahead, "Array");
            match(TokenTypes.parray);
            Type arrayType = tipoPrimitivo();
            type.setArrType(arrayType);
            return type;
        }
        else {
            if (idClassSimilars()) {
                return matchIdClassSimilars();
            }
            else {
                if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                        lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble) {
                    return tipoPrimitivo();
                }
                else {
                    throw new SyntacticException(lookahead, "Se esperaba array, identificador, str, bool, int, double. Se encontró: " +lookahead.getName());
                }
            }
        }
    }

    /**
     * ⟨Tipo-Metodo⟩ ::= ⟨Tipo⟩
     * ⟨Tipo-Metodo⟩ ::= pvoid
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public Type tipoMetodo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pvoid) {
            Token token = lookahead;
            match(TokenTypes.pvoid);
            return new Type(token, "void");
        }
        else {
            if (lookahead.getName() == TokenTypes.pstr ||
                    lookahead.getName() == TokenTypes.pbool ||
                    lookahead.getName() == TokenTypes.pint ||
                    lookahead.getName() == TokenTypes.pdouble ||
                    lookahead.getName() == TokenTypes.parray ||
                    idClassSimilars()
            ) {
               return tipo();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'void', 'str', " +
                        "'bool', 'int', 'double', " +
                        "'array' o un identificador. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <OpIgual> ::= op_rel_equal
     * <OpIgual> ::= op_rel_notequal
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void opIgual() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_rel_equal) {
            match(TokenTypes.op_rel_equal);
        }
        else {
            if (lookahead.getName() == TokenTypes.op_rel_notequal) {
                match(TokenTypes.op_rel_notequal);
            }
            else {
                throw new SyntacticException(lookahead, "Se " +
                        "esperaba '==' o '!=. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <OpCompuesto> ::= op_rel_less
     * <OpCompuesto> ::= op_rel_greater
     * <OpCompuesto> ::= op_rel_lessequal
     * <OpCompuesto> ::= op_rel_greaterequal
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void opCompuesto() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_rel_less -> match(TokenTypes.op_rel_less);
            case op_rel_greater -> match(TokenTypes.op_rel_greater);
            case op_rel_greaterequal -> match(TokenTypes.op_rel_greaterequal);
            case op_rel_lessequal -> match(TokenTypes.op_rel_lessequal);
            default -> throw new SyntacticException(lookahead, "Se esperaba un" +
                    " operador relacional (>, >=, <, <=). " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <OpAd> ::= op_sum
     * <OpAd> ::= op_sub
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void opAdd() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_sum) {
            match(TokenTypes.op_sum);
        }
        else {
            if (lookahead.getName() == TokenTypes.op_sub) {
                match(TokenTypes.op_sub);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '+', '-'." +
                        " Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <OpUnario> ::= op_sum
     * <OpUnario> ::= op_sub
     * <OpUnario> ::= op_not
     * <OpUnario> ::= op_increment
     * <OpUnario> ::= op_decrement
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void opUnario() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_sum -> match(TokenTypes.op_sum);
            case op_sub -> match(TokenTypes.op_sub);
            case op_not -> match(TokenTypes.op_not);
            case op_decrement -> match(TokenTypes.op_decrement);
            case op_increment -> match(TokenTypes.op_increment);
            default -> throw new SyntacticException(lookahead, "Se esperaba un operador de +, -, !, ++, --. Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <OpMul> ::= op_mult
     * <OpMul> ::= op_div
     * <OpMul> ::= op_mod
     * <OpMul> ::= pdiv
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void opMul() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case op_mult -> match(TokenTypes.op_mult);
            case op_div -> match(TokenTypes.op_div);
            case op_mod -> match(TokenTypes.op_mod);
            case pdiv -> match(TokenTypes.pdiv);
            default -> throw new SyntacticException(lookahead, "Se " +
                    "esperaba '+', 'div', '/' o '%'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <Literal> ::= pnil
     * <Literal> ::= ptrue
     * <Literal> ::= pfalse
     * <Literal> ::= const_int
     * <Literal> ::= const_string
     * <Literal> ::= const_double
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void literal() throws LexicalException, SyntacticException, ReaderException {
        switch (lookahead.getName()) {
            case pnil -> match(TokenTypes.pnil);
            case ptrue -> match(TokenTypes.ptrue);
            case pfalse -> match(TokenTypes.pfalse);
            case const_int -> match(TokenTypes.const_int);
            case const_string -> match(TokenTypes.const_string);
            case const_double -> match(TokenTypes.const_double);
            default -> throw new SyntacticException(lookahead, "Se esperaba nil, true, " +
                    "false o una constante." +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨Argumentos-Formales⟩::= parentheses1 <Argumentos-Formales2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void argumentosFormales() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        match(TokenTypes.parentheses1);
        argumentosFormales2();
    }

    /**
     * <Argumentos-Formales2> ::= ⟨Lista-Argumentos-Formales⟩ parentheses2
     * <Argumentos-Formales2> ::= parentheses2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void argumentosFormales2() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        // chequeamos los primeros de 'argumentoFormal' por las reglas 41, 43 y 46
        if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray || idClassSimilars()) {

            listaArgumentosFormales();
            match(TokenTypes.parentheses2);
        }
        // (cuando viene ')')
        else {
            if (lookahead.getName() == TokenTypes.parentheses2) {
                match(TokenTypes.parentheses2);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba str, bool, int, double, array o ')'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Lista-Argumentos-Formales2⟩ ::= comma ⟨Lista-Argumentos-Formales⟩
     * ⟨Lista-Argumentos-Formales2⟩ ::=  λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaArgumentosFormales2() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaArgumentosFormales();
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses2) {
                return; // lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se " +
                        "esperaba ',' o ')'. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Lista-Argumentos-Formales⟩ ::= ⟨Argumento-Formal⟩ ⟨Lista-Argumentos-Formales2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaArgumentosFormales() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        argumentoFormal();
        listaArgumentosFormales2();
    }

    /**
     * ⟨Argumento-Formal⟩ ::= ⟨Tipo⟩ id_obj
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void argumentoFormal() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        Type type = tipo();
        if (lookahead.getName() == TokenTypes.id_obj) {
            Parameter parameter = new Parameter(lookahead, type);
            SymbolTable.getCurrentMethod().addParameter(parameter);
            match(TokenTypes.id_obj);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba un identificador de objeto. Se encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨Lista-Declaracion-Variables⟩::= id_obj <Lista-Declaracion-Variables2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaDeclaracionVariables(Type type, boolean visibilidad, String option) throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (option.equals("attribute")) {
            Attribute attribute = new Attribute(lookahead, type, visibilidad);
            SymbolTable.getCurrentClass().addAttributes(attribute);
        } else {
            Variable variable = new Variable(lookahead, type);
            SymbolTable.getCurrentMethod().addVariable(variable);
        }
        match(TokenTypes.id_obj);
        listaDeclaracionVariables2(type, visibilidad, option);
    }

    /**
     * <Lista-Declaracion-Variables2> ::=  comma ⟨Lista-Declaracion-Variables⟩
     * <Lista-Declaracion-Variables2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaDeclaracionVariables2(Type type, boolean visibilidad, String option) throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaDeclaracionVariables(type, visibilidad, option);
        }
        else {
            if (lookahead.getName() == TokenTypes.semicolon) {
                return; // lambda
            } else {
                throw new SyntacticException(lookahead, "Se esperaba ',' o ';'. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Herencia⟩ ::= colon ⟨Tipo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void herencia() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.colon);
        SymbolTable.getCurrentClass().setParentClass(lookahead.getLexeme());
        tipo();
    }

    /**
     * ⟨Atributo⟩ ::= ⟨Visibilidad⟩ ⟨Tipo⟩ ⟨Lista-Declaracion-Variables⟩ semicolon
     * ⟨Atributo⟩ ::= ⟨Tipo⟩ ⟨Lista-Declaracion-Variables⟩ semicolon
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void atributo() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.ppub) {
            visibilidad();
            Type type = tipo();
            listaDeclaracionVariables(type, true, "attribute");
            match(TokenTypes.semicolon);
        }
        else {
            if (lookahead.getName() == TokenTypes.pstr ||
                lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint ||
                lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray ||
                idClassSimilars()) {

                Type type = tipo();
                listaDeclaracionVariables(type, false, "attribute");
                match(TokenTypes.semicolon);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'pub', " +
                        "'str', 'bool', 'int, 'double'," +
                        " 'array' o un identificador de clase. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Atributos> ::= <Atributo> <Atributos>
     * <Atributos> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void atributos() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.ppub ||
            lookahead.getName() == TokenTypes.pstr ||
            lookahead.getName() == TokenTypes.pbool ||
            lookahead.getName() == TokenTypes.pint ||
            lookahead.getName() == TokenTypes.pdouble ||
            lookahead.getName() == TokenTypes.parray ||
            idClassSimilars()
        ) {

            atributo();
            atributos();
        }
        else {
            if (lookahead.getName() == TokenTypes.braces2) {
                return; //lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba " +
                        "pub, str, bool, int, " +
                        "double, array, identificador de clase o " +
                        "'}'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Class2> ::=  ⟨Herencia⟩ braces1 ⟨Atributos⟩ braces2
     * <Class2> ::=  braces1 ⟨Atributos⟩ braces2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void class2() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.colon) {
            herencia();
        }
        if (lookahead.getName() == TokenTypes.braces1) {
            match(TokenTypes.braces1);
        }
        else {
            throw new SyntacticException(lookahead, "Se " +
                    "esperaba '{', ':'. " +
                    "Se encontró: " + lookahead.getName());
            }
        atributos();
        match(TokenTypes.braces2);
    }

    /**
     * ⟨Class⟩ ::= pclass id_class <Class2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void class1() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        match(TokenTypes.pclass);
        Class class1 = new Class(lookahead);
        class1 = SymbolTable.addClass(class1, "class");
        SymbolTable.setCurrentClass(class1);
        matchIdClassSimilars();
        class2();
    }

    /**
     * ⟨Impl⟩ ::= pimpl id_class braces1 ⟨Miembro⟩ <Miembros> braces2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void impl() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        match(TokenTypes.pimpl);
        Class class1 = new Class(lookahead);
        class1 = SymbolTable.addClass(class1, "impl");
        SymbolTable.setCurrentClass(class1);
        matchIdClassSimilars();
        match(TokenTypes.braces1);
        miembro();
        miembros();
        match(TokenTypes.braces2);
    }

    /**
     * ⟨Miembro⟩ ::= ⟨Metodo⟩
     * ⟨Miembro⟩ ::= ⟨Constructor⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void miembro() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pfn || lookahead.getName() == TokenTypes.pst) {
            metodo();
        }
        else {
            if (lookahead.getName() == TokenTypes.dot) {
                constructor();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba fn, st, '.'. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Miembros> ::= <Miembro> <Miembros>
     * <Miembros> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void miembros() throws SyntacticException, LexicalException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pfn ||
            lookahead.getName() == TokenTypes.pst ||
            lookahead.getName() == TokenTypes.dot) {

            miembro();
            miembros();
        }
        else {
            if (lookahead.getName() == TokenTypes.braces2) {
                return; //lambda
            } else {
                throw new SyntacticException(lookahead, "Se esperaba '}' " +
                        "o 'fn' o 'st' o '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Bloque-Metodo⟩ ::= braces1 ⟨Mas-Decl-Var-Locales⟩ ⟨Sentencias⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void bloqueMetodo() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.braces1) {
            match(TokenTypes.braces1);
            masDeclVarLocales();
            sentencias();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '{'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <Sentencias> ::= <Sentencia> <Sentencias>
     * <Sentencias> ::= braces2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void sentencias() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.braces2) {
            match(TokenTypes.braces2);
        }
        else {
            if (lookahead.getName() == TokenTypes.braces1 ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pret ||
            lookahead.getName() == TokenTypes.pself ||
            lookahead.getName() == TokenTypes.id_obj ||
            lookahead.getName() == TokenTypes.pwhile ||
            lookahead.getName() == TokenTypes.pif ||
            lookahead.getName() == TokenTypes.semicolon) {
                sentencia();
                sentencias();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '{', '(', '}', 'ret', " +
                        "'self', 'while', 'if', ';' o identificador de método o variable. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Mas-Decl-Var-Locales⟩ ::=  ⟨Decl-Var-Locales⟩ ⟨Mas-Decl-Var-Locales⟩
     * ⟨Mas-Decl-Var-Locales⟩ ::=   λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void masDeclVarLocales() throws LexicalException, ReaderException, SyntacticException, SemanticException {
        if (lookahead.getName() == TokenTypes.pstr ||
                lookahead.getName() == TokenTypes.pbool ||
                lookahead.getName() == TokenTypes.pint ||
                lookahead.getName() == TokenTypes.pdouble ||
                lookahead.getName() == TokenTypes.parray ||
                idClassSimilars()
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
                throw new SyntacticException(lookahead, "Se esperaba 'str', " +
                        "'bool', 'int', 'double', 'array', ';', 'if', 'while, " +
                        "'self', 'ret', '(', '{', '}' o identificador. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Decl-Var-Locales⟩ ::= ⟨Tipo⟩ ⟨Lista-Declaracion-Variables⟩ semicolon
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void declVarLocales() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pstr ||
            lookahead.getName() == TokenTypes.pbool ||
            lookahead.getName() == TokenTypes.pint ||
            lookahead.getName() == TokenTypes.pdouble ||
            lookahead.getName() == TokenTypes.parray ||
            idClassSimilars()
        ) {
            Type type = tipo();
            listaDeclaracionVariables(type, false, "variable");
            match(TokenTypes.semicolon);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'str', " +
                    "'bool', 'int', 'double', 'array' o identificador de clas. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨Metodo⟩ ::= ⟨Forma-Metodo⟩ pfn <Metodo2>
     * ⟨Metodo⟩ ::= pfn <Metodo2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void metodo() throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pfn) {
            match(TokenTypes.pfn);
            metodo2(false);
        }
        else {
            if (lookahead.getName() == TokenTypes.pst) {
                formaMetodo();
                match(TokenTypes.pfn);
                metodo2(true);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'fn' o 'st'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Metodo2> ::= ⟨Tipo-Metodo⟩ id_obj ⟨Argumentos-Formales⟩ ⟨Bloque-Metodo⟩
     * <Metodo2> ::= id_obj ⟨Argumentos-Formales⟩ ⟨Bloque-Metodo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void metodo2(boolean isStatic) throws LexicalException, SyntacticException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.pstr ||
            lookahead.getName() == TokenTypes.pbool ||
            lookahead.getName() == TokenTypes.pint ||
            lookahead.getName() == TokenTypes.pdouble ||
            lookahead.getName() == TokenTypes.parray ||
            idClassSimilars() ||
            lookahead.getName() == TokenTypes.pvoid
        ) {
            Type type = tipoMetodo();
            Method method = new Method(lookahead, type, isStatic);
            SymbolTable.setCurrentMethod(method);
            SymbolTable.getCurrentClass().addMethods(method);
            match(TokenTypes.id_obj);
            argumentosFormales();
            bloqueMetodo();
        }
        else {
            if (lookahead.getName() == TokenTypes.id_obj) {
                Type type = new Type(lookahead, "void");
                Method method = new Method(lookahead, type, isStatic);
                SymbolTable.setCurrentMethod(method);
                SymbolTable.getCurrentClass().addMethods(method);
                match(TokenTypes.id_obj);
                argumentosFormales();
                bloqueMetodo();
            }
            else {
                throw new SyntacticException(lookahead, "Se " +
                        "esperaba 'str', 'bool', " +
                        "'int, 'double, 'array', 'void " +
                        "o identificador. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Constructor⟩ ::= dot ⟨Argumentos-Formales⟩ ⟨Bloque-Metodo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void constructor() throws SyntacticException, LexicalException, ReaderException, SemanticException {
        if (lookahead.getName() == TokenTypes.dot) {
            Token auxlookahead = lookahead;
            match(TokenTypes.dot);
            Type type = new Type(SymbolTable.getCurrentClass()
                    .getToken(), "class");
            Constructor constructor = new Constructor(type, auxlookahead);
            SymbolTable.setCurrentMethod(constructor);
            SymbolTable.getCurrentClass().setConstructor(constructor);
            argumentosFormales();
            bloqueMetodo();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '.'. Se encontró: " + lookahead.getName());
        }
    }


    /**
     * ⟨ExpresionParentizada2⟩ ::= <ExpOr> parentheses2 <ExpresionParentizada3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expresionParentizada2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pself ||
            idClassSimilars() ||
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
            throw new SyntacticException(lookahead, "Se esperaba 'nil', 'true', " +
                    "'false', ')', 'self', '+', '-', '!', '++', " +
                    "'--', identificadores o constantes. Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpresionParentizada3> ::= <Encadenado>
     * <ExpresionParentizada3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expresionParentizada3() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨AccesoSelf⟩ ::= pself ⟨AccesoSelf2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoSelf() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            match(TokenTypes.pself);
            accesoSelf2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'self'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨AccesoSelf2⟩::= <Encadenado>
     * ⟨AccesoSelf2⟩ ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoSelf2() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨AccesoVar2⟩ ::= <AccesoVar3>
     * ⟨AccesoVar2⟩ ::= brackets1 <ExpOr> brackets2 <AccesoVar3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
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
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.', '['." +
                        " Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <AccesoVar3> ::= <Encadenado>
     * <AccesoVar3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoVar3() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Encadenado⟩ ::= dot id <Encadenado2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void encadenado() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            match(TokenTypes.dot);
            if (lookahead.getName() == TokenTypes.id_obj) {
                match(TokenTypes.id_obj);
                encadenado2();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba identificador de método o variable. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '.'. " +
                    "Se encontró: "  + lookahead.getName());
        }
    }

    /**
     * <Encadenado2> ::= ⟨Llamada-Metodo-Encadenado⟩
     * <Encadenado2> ::= ⟨Acceso-Variable-Encadenado⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
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
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'," +
                        " '[', '('. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Llamada-Metodo-Encadenado⟩ ::=  ⟨Argumentos-Actuales⟩ ⟨Llamada-Metodo-Encadenado2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaMetodoEncadenado() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            argumentosActuales();
            llamadaMetodoEncadenado2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '('. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨Llamada-Metodo-Encadenado2⟩ ::= <Encadenado>
     * ⟨Llamada-Metodo-Encadenado2⟩ ::=  λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaMetodoEncadenado2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Acceso-Variable-Encadenado⟩ ::= <Acceso-Variable-Encadenado3>
     * ⟨Acceso-Variable-Encadenado⟩ ::= brackets1  <ExpOr> brackets2 <Acceso-Variable-Encadenado3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
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
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Acceso-Variable-Encadenado3> ::= <Encadenado>
     * <Acceso-Variable-Encadenado3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoVariableEncadenado3() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Argumentos-Actuales⟩ ::= parentheses1  <Argumentos-Actuales2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void argumentosActuales() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            match(TokenTypes.parentheses1);
            argumentosActuales2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '('. " +
                    "Se encontró: " +  lookahead.getName());
        }
    }

    /**
     * <Argumentos-Actuales2> ::= ⟨Lista-Expresiones⟩ parentheses2
     * <Argumentos-Actuales2> ::= parentheses2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void argumentosActuales2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double ||
            lookahead.getName() == TokenTypes.parentheses1 ||
            lookahead.getName() == TokenTypes.pself ||
            idClassSimilars() ||
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
                throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                        "'true', 'false', '(', 'self', 'new', " +
                        "'+', '-', '!', '++', '--', ')', " +
                        "identificadores o constantes. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Lista-Expresiones⟩ ::= <ExpOr> ⟨Lista-Expresiones2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaExpresiones() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pnil ||
                lookahead.getName() == TokenTypes.ptrue ||
                lookahead.getName() == TokenTypes.pfalse ||
                lookahead.getName() == TokenTypes.const_int ||
                lookahead.getName() == TokenTypes.const_string ||
                lookahead.getName() == TokenTypes.const_double ||
                lookahead.getName() == TokenTypes.parentheses1 ||
                lookahead.getName() == TokenTypes.pself ||
                idClassSimilars() ||
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
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', 'new', " +
                    "'+', '-', '!', '++', '--', " +
                    "identificadores o constantes. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * ⟨Lista-Expresiones2⟩ ::= comma ⟨Lista-Expresiones⟩
     * ⟨Lista-Expresiones2⟩ ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
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
                throw new SyntacticException(lookahead, "Se esperaba ',', ')'." +
                        " Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Operando> ::= <Literal>
     * <Operando> ::= <Primario>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
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
                idClassSimilars() ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pnew
            ) {
                primario();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                        "'true', 'false', 'self', 'new', " +
                        "identificadores o constantes. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }


    /**
     * <Primario> ::= <AccesoSelf>
     * <Primario> ::= id <Primario2>
     * <Primario> ::= <Llamada-Método-Estático>
     * <Primario> ::= <Llamada-Conclassor>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void primario() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            accesoSelf();
        }
        else {
            if (lookahead.getName() == TokenTypes.pnew) {
                llamadaConclassor();
            }
            else {
                if (idClassSimilars()) {
                    llamadaMetodoEstatico();
                }
                else {
                    if (lookahead.getName() == TokenTypes.id_obj) {
                        match(TokenTypes.id_obj);
                        primario2();
                    }
                    else {
                        throw new SyntacticException(lookahead, "Se " +
                                "esperaba 'self', 'new' " +
                                "o un identificador. Se " +
                                "encontró: " + lookahead.getName());
                    }
                }
            }
        }
    }


    /**
     * <Primario2> ::= <Argumentos-Actuales> <Primario3>
     * <Primario2> ::= <AccesoVar2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void primario2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            argumentosActuales();
            primario3();
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.brackets1 ||
                primarioFollows()
            ) {
                accesoVar2();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.', '[', '('. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Primario3> ::= <Encadenado>
     * <Primario3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void primario3() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //Retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Sentencia> ::= semicolon
     * <Sentencia> ::= <Asignación> semicolon
     * <Sentencia> ::= <Sentencia-Simple> semicolon
     * <Sentencia> ::= pif parentheses1 <ExpOr> parentheses2 <Sentencia> <SentenciaIf>
     * <Sentencia> ::= pwhile parentheses1 <ExpOr> parentheses2 <Sentencia>
     * <Sentencia> ::= <Bloque>
     * <Sentencia> ::= ret <SentenciaRet>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void sentencia() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.semicolon) {
            match(TokenTypes.semicolon);
        } else {
            if (lookahead.getName() == TokenTypes.pret) {
                match(TokenTypes.pret);
                sentenciaRet();
            }
            else {
                if (lookahead.getName() == TokenTypes.pif) {
                    match(TokenTypes.pif);
                    match(TokenTypes.parentheses1);
                    expOr();
                    match(TokenTypes.parentheses2);
                    sentencia();
                    sentenciaIf();
                }
                else {
                    if (lookahead.getName() == TokenTypes.pwhile) {
                        match(TokenTypes.pwhile);
                        match(TokenTypes.parentheses1);
                        expOr();
                        match(TokenTypes.parentheses2);
                        sentencia();
                    }
                    else {
                        if (lookahead.getName() == TokenTypes.braces1) {
                            bloque();
                        }
                        else {
                            if (lookahead.getName() == TokenTypes.id_obj ||
                                lookahead.getName() == TokenTypes.pself
                            ) {
                                asignacion();
                                match(TokenTypes.semicolon);
                            }
                            else {
                                if (lookahead.getName() == TokenTypes.parentheses1) {
                                    sentenciaSimple();
                                    match(TokenTypes.semicolon);
                                }
                                else {
                                    throw new SyntacticException(lookahead, "Se esperaba ';', '(', '{', " +
                                            "'ret', 'self', 'while', 'if' " +
                                            "o un identificador de método o variable. Se " +
                                            "encontró: " + lookahead.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <Bloque> ::= braces1 <Sentencias>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void bloque() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.braces1) {
            match(TokenTypes.braces1);
            sentencias();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '{'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <Asignación> ::= <AccesoVar-Simple> op_equal <ExpOr>
     * <Asignación> ::= <AccesoSelf-Simple> op_equal <ExpOr>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void asignacion() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            accesoSelfSimple();
            match(TokenTypes.op_equal);
            expOr();
        }
        else {
            if (lookahead.getName() == TokenTypes.id_obj) {
                accesoVarSimple();
                match(TokenTypes.op_equal);
                expOr();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'self' " +
                        "o un identificador de método o variable. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <AccesoVar-Simple> ::= id <AccesoVar-Simple2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoVarSimple() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.id_obj) {
            match(TokenTypes.id_obj);
            accesoVarSimple2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba un identificador de método o variable. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <SentenciaRet> ::= semicolon
     * <SentenciaRet> ::= <ExpOr> semicolon
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void sentenciaRet() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.semicolon) {
            match(TokenTypes.semicolon);
        }
        else {
            if (expOrFirst()) {
                expOr();
                match(TokenTypes.semicolon);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                        "'true', 'false', '(', 'self', " +
                        "'new', '+', '-', '!', '++', '--', " +
                        "una constante o un identificador. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <SentenciaIf> ::= pelse <Sentencia>
     * <SentenciaIf> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void sentenciaIf() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pelse) {
            match(TokenTypes.pelse);
            sentencia();
        }
        else {
            //EL ELSE NO VA ACA, PUESTO QUE ESTE ES EL CONFLICTO CLASICO DESPLAZAMIENTO/REDUCCION
            //SE ELIGIO DESPLAZAR
            if (lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.pif ||
                lookahead.getName() == TokenTypes.pwhile ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pself ||
                lookahead.getName() == TokenTypes.braces1 ||
                lookahead.getName() == TokenTypes.braces2 ||
                lookahead.getName() == TokenTypes.pret ||
                lookahead.getName() == TokenTypes.parentheses1
            ) {
                //reduce, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba ';', " +
                        "'if', 'while', 'self', '{', '}', " +
                        "'ret', '(', o un identificador de método o variable. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <AccesoVar-Simple2> ::= <Encadenados-Simples>
     * <AccesoVar-Simple2> ::= brackets1 <ExpOr> brackets2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoVarSimple2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.brackets1) {
            match(TokenTypes.brackets1);
            expOr();
            match(TokenTypes.brackets2);
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.op_equal
            ) {
                encadenadosSimples();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '[', " +
                        "'.' o '='. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }


    /**
     * <Encadenados-Simples> ::= <Encadenado-Simple> <Encadenados-Simples>
     * <Encadenados-Simples> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void encadenadosSimples() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenadoSimple();
            encadenadosSimples();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_equal) {
                //Reducir, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '.' " +
                        "o '='. Se encontró: " +
                        lookahead.getName());
            }
        }
    }

    /**
     * <AccesoSelf-Simple> ::= pself <Encadenados-Simples>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void accesoSelfSimple() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            match(TokenTypes.pself);
            encadenadosSimples();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'self'. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <Encadenado-Simple> ::= dot id
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void encadenadoSimple() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.dot);
        match(TokenTypes.id_obj);
    }


    /**
     * <Sentencia-Simple> ::= parentheses1 <ExpOr> parentheses2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void sentenciaSimple() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            match(TokenTypes.parentheses1);
            expOr();
            match(TokenTypes.parentheses2);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '('. " +
                    "Se encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpOr> ::= <ExpAnd> <ExpOr2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expOr() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            expAnd();
            expOr2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpOr2> ::= op_or <ExpAnd> <ExpOr2>
     * <ExpOr2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expOr2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_or) {
            match(TokenTypes.op_or);
            expAnd();
            expOr2();
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpAnd> ::= <ExpIgual> <ExpAnd2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expAnd() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            expIgual();
            expAnd2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpAnd2> ::= op_and <ExpIgual> <ExpAnd2>
     * <ExpAnd2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expAnd2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_and) {
            match(TokenTypes.op_and);
            expIgual();
            expAnd2();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_or ||
                lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '&&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpIgual> ::= <ExpCompuesta> <ExpIgual2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expIgual() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            expCompuesta();
            expIgual2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpIgual2> ::= <OpIgual> <ExpCompuesta> <ExpIgual2>
     * <ExpIgual2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expIgual2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_rel_equal ||
            lookahead.getName() == TokenTypes.op_rel_notequal
        ) {
            opIgual();
            expCompuesta();
            expIgual2();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_and ||
                lookahead.getName() == TokenTypes.op_or ||
                lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '==', '!=', '&&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpCompuesta> ::= <ExpAd> <ExpCompuesta2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expCompuesta() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
           expAd();
           expCompuesta2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpCompuesta2> ::= <OpCompuesto> <ExpAd>
     * <ExpCompuesta2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expCompuesta2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_rel_less ||
            lookahead.getName() == TokenTypes.op_rel_greater ||
            lookahead.getName() == TokenTypes.op_rel_lessequal ||
            lookahead.getName() == TokenTypes.op_rel_greaterequal
        ) {
            opCompuesto();
            expAd();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_rel_equal ||
                lookahead.getName() == TokenTypes.op_rel_notequal ||
                lookahead.getName() == TokenTypes.op_and ||
                lookahead.getName() == TokenTypes.op_or ||
                lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpAd> ::= <ExpMul> <ExpAd2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expAd() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            expMul();
            expAd2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpAd2> ::= <OpAd> <ExpMul> <ExpAd2>
     * <ExpAd2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expAd2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub
        ) {
            opAdd();
            expMul();
            expAd2();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_rel_less ||
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
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '+', '-', '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpMul> ::= <ExpUn> <ExpMul2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expMul() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            expUn();
            expMul2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
    }

    /**
     * <ExpMul2> ::= <OpMul> <ExpUn> <ExpMul2>
     * <ExpMul2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expMul2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_mult ||
            lookahead.getName() == TokenTypes.op_div ||
            lookahead.getName() == TokenTypes.op_mod ||
            lookahead.getName() == TokenTypes.pdiv
        ) {
            opMul();
            expUn();
            expMul2();
        }
        else {
            if (lookahead.getName() == TokenTypes.op_sub ||
                lookahead.getName() == TokenTypes.op_sum ||
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
            ) {
                //retorna, pues es lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <ExpUn> ::= <OpUnario> <ExpUn>
     * <ExpUn> ::= <Operando>
     * <ExpUn> ::= parenthesis1 <ExpUnAux>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expUn() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub ||
            lookahead.getName() == TokenTypes.op_not ||
            lookahead.getName() == TokenTypes.op_increment ||
            lookahead.getName() == TokenTypes.op_decrement
        ) {
            opUnario();
            expUn();
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses1) {
                match(TokenTypes.parentheses1);
                expUnAux();
            }
            else {
                if (lookahead.getName() == TokenTypes.pnil ||
                    lookahead.getName() == TokenTypes.ptrue ||
                    lookahead.getName() == TokenTypes.pfalse ||
                    lookahead.getName() == TokenTypes.const_int ||
                    lookahead.getName() == TokenTypes.const_double ||
                    lookahead.getName() == TokenTypes.const_string ||
                    lookahead.getName() == TokenTypes.pself ||
                    idClassSimilars()||
                    lookahead.getName() == TokenTypes.id_obj ||
                    lookahead.getName() == TokenTypes.pnew
                ) {
                    operando();
                }
                else {
                    throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                            "'true', 'false', '(', 'self', " +
                            "'new', '+', '-', '!', '++', '--', " +
                            "una constante o un identificador. Se " +
                            "encontró: " + lookahead.getName());
                }
            }
        }
    }

    /**
     * <ExpUnAux> ::= pint parentheses2 <ExpUn>
     * <ExpUnAux> ::= <ExpresionParentizada2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void expUnAux() throws LexicalException, SyntacticException, ReaderException {
        if (expOrFirst()) {
            expresionParentizada2();
        }
        else {
            if (lookahead.getName() == TokenTypes.pint) {
                match(TokenTypes.pint);
                match(TokenTypes.parentheses2);
                expUn();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'int', 'nil', " +
                        "'true', 'false', '(', 'self', " +
                        "'new', '+', '-', '!', '++', '--', " +
                        "una constante o un identificador. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * Metodo para reutilizar
     * @author Paulina Suden y Tomás Rando
     */
    public boolean expOrFirst() {
        return (lookahead.getName() == TokenTypes.pnil ||
                lookahead.getName() == TokenTypes.ptrue ||
                lookahead.getName() == TokenTypes.pfalse ||
                lookahead.getName() == TokenTypes.const_int ||
                lookahead.getName() == TokenTypes.const_string ||
                lookahead.getName() == TokenTypes.const_double ||
                lookahead.getName() == TokenTypes.parentheses1 ||
                lookahead.getName() == TokenTypes.pself ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pnew ||
                idClassSimilars() ||
                lookahead.getName() == TokenTypes.op_sum ||
                lookahead.getName() == TokenTypes.op_sub ||
                lookahead.getName() == TokenTypes.op_not ||
                lookahead.getName() == TokenTypes.op_increment ||
                lookahead.getName() == TokenTypes.op_decrement
        );
    }


    /**
     * Metodo para reutilizar
     * @author Tomás Rando
     */
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

    /**
     * Metodo que matchea un token
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void match(TokenTypes tokenType) throws LexicalException, ReaderException, SyntacticException {

        if (lookahead.getName() == tokenType) {
            lookahead = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntacticException(lookahead, "Se esperaba " + tokenType +
                    ". Se encontró " + lookahead.getName());
        }
    }


    /**
     * ⟨Llamada-Metodo⟩ ::= id ⟨Argumentos-Actuales⟩ ⟨Llamada-Metodo2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaMetodo() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.id_obj) {
            match(TokenTypes.id_obj);
        } else {
            throw new SyntacticException(lookahead, "Se esperaba identificador de método o variable. " +
                    "Se encontró: " + lookahead.getName());
        }
        argumentosActuales();
        llamadaMetodo2();
    }

    /**
     * ⟨Llamada-Metodo2⟩ ::= <Encadenado>
     * ⟨Llamada-Metodo2⟩ ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaMetodo2() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                return; //lambda
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * ⟨Llamada-Metodo-Estatico⟩ ::= id_class dot ⟨Llamada-Metodo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaMetodoEstatico() throws LexicalException, SyntacticException, ReaderException {
        matchIdClassSimilars();
        match(TokenTypes.dot);
        llamadaMetodo();
    }

    /**
     * ⟨Llamada-Conclassor⟩ ::= pnew  <Llamada-Conclassor2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void llamadaConclassor() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pnew);
        llamadaConclassor2();
    }

    /**
     * <Llamada-Conclassor2> ::= id_class ⟨Argumentos-Actuales⟩ <Llamada-Conclassor3>
     * <Llamada-Conclassor2> ::= ⟨Tipo-Primitivo⟩ brackets1 <ExpOr> brackets2 <Llamada-Conclassor3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    private void llamadaConclassor2() throws LexicalException, SyntacticException, ReaderException {
        if (idClassSimilars()) {
            matchIdClassSimilars();
            argumentosActuales();
            llamadaConclassor3();
        }
        else {
            if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                    lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble) {
                tipoPrimitivo();
                match(TokenTypes.brackets1);
                expOr();
                match(TokenTypes.brackets2);
                llamadaConclassor3();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba identificador de clase, " +
                        "'str', 'bool', 'int' o 'double'. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * <Llamada-Conclassor3> ::= <Encadenado>
     * <Llamada-Conclassor3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    private void llamadaConclassor3() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda
            } else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
    }

    /**
     * Matchea el token que contiene un identificador de clase con su
     * TokenType correspondiente.
     * @author Paulina Suden y Tomas Rando
     */
    public Type matchIdClassSimilars() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        if (lookahead.getName() == TokenTypes.id_class) {
            match(TokenTypes.id_class);
            return new Type(token,"class");
        }
        else {
            if (lookahead.getName() == TokenTypes.pio) {
                match(TokenTypes.pio);
                return new Type(token,"class");
            }
            else {
                if (lookahead.getName() == TokenTypes.pobject) {
                    match(TokenTypes.pobject);
                    return new Type(token, "class");
                }
                else {
                    throw new SyntacticException(lookahead, "Se esperaba " +
                            "identificador, IO, object. " +
                            "Se encontró: " + lookahead.getName());
                }
            }
        }
    }

    /**
     * Verifica si el lookahead es un identificador de clase
     * @author Paulina Suden y Tomas Rando
     * @return boolean verificando si se encuentra algun identificador de clase en el token
     */
    public boolean idClassSimilars() {
        return (lookahead.getName() == TokenTypes.id_class ||
                lookahead.getName() == TokenTypes.pio ||
                lookahead.getName() == TokenTypes.pobject
                );
    }

}
