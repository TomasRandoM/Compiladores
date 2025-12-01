package com.uncuyo.compiladores.syntacticAnalyzer;

import com.uncuyo.compiladores.exceptions.*;
import com.uncuyo.compiladores.lexicalAnalyzer.LexicalAnalyzer;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.lexicalAnalyzer.TokenTypes;
import com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.*;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;

import java.util.ArrayList;
import java.util.List;

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
    public void program() throws LexicalException, ReaderException, SyntacticException, SemanticException, SemanticASTException {
        lookahead = lexicalAnalyzer.nextToken();
        SymbolTable.addPredefinedClasses();
        listaDefiniciones();
        start();
        match(TokenTypes.end_of_file);
        System.out.println("CORRECTO: ANÁLISIS SINTÁCTICO");
        SymbolTable.checkDeclarations();
        System.out.println("CORRECTO: ANÁLISIS SEMÁNTICO - DECLARACIONES");
        AST.check();
        System.out.println("CORRECTO: ANÁLISIS SEMÁNTICO - SENTENCIAS");

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
        AST.setCurrentClass(null);
        AST.setCurrentMethod(method.getName());
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
    public Token opIgual() throws LexicalException, SyntacticException, ReaderException {
        Token token;
        if (lookahead.getName() == TokenTypes.op_rel_equal) {
            token = lookahead;
            match(TokenTypes.op_rel_equal);
        }
        else {
            if (lookahead.getName() == TokenTypes.op_rel_notequal) {
                token = lookahead;
                match(TokenTypes.op_rel_notequal);
            }
            else {
                throw new SyntacticException(lookahead, "Se " +
                        "esperaba '==' o '!=. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return token;
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
    public Token opCompuesto() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        switch (lookahead.getName()) {
            case op_rel_less: {
                match(TokenTypes.op_rel_less);
                break;
            }
            case op_rel_greater: {
                match(TokenTypes.op_rel_greater);
                break;
            }
            case op_rel_greaterequal: {
                match(TokenTypes.op_rel_greaterequal);
                break;
            }
            case op_rel_lessequal: {
                match(TokenTypes.op_rel_lessequal);
                break;
            }
            default: {
                throw new SyntacticException(lookahead, "Se esperaba un" +
                        " operador relacional (>, >=, <, <=). " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return token;
    }

    /**
     * <OpAd> ::= op_sum
     * <OpAd> ::= op_sub
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public Token opAdd() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
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
        return token;
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
    public Token opUnario() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        switch (lookahead.getName()) {
            case op_sum -> match(TokenTypes.op_sum);
            case op_sub -> match(TokenTypes.op_sub);
            case op_not -> match(TokenTypes.op_not);
            case op_decrement -> match(TokenTypes.op_decrement);
            case op_increment -> match(TokenTypes.op_increment);
            default -> throw new SyntacticException(lookahead, "Se esperaba un operador de +, -, !, ++, --. Se encontró: " + lookahead.getName());
        }
        return token;
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
    public Token opMul() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        switch (lookahead.getName()) {
            case op_mult -> match(TokenTypes.op_mult);
            case op_div -> match(TokenTypes.op_div);
            case op_mod -> match(TokenTypes.op_mod);
            case pdiv -> match(TokenTypes.pdiv);
            default -> throw new SyntacticException(lookahead, "Se " +
                    "esperaba '+', 'div', '/' o '%'. " +
                    "Se encontró: " + lookahead.getName());
        }
        return token;
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
    public OperandNode literal() throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        OperandNode operandNode;
        switch (lookahead.getName()) {
            case pnil:
                match(TokenTypes.pnil);
                operandNode = new LiteralNode(token, "nil");
                break;
            case ptrue:
                match(TokenTypes.ptrue);
                operandNode  = new LiteralNode(token, "true");
                break;
            case pfalse:
                match(TokenTypes.pfalse);
                operandNode  = new LiteralNode(token, "false");
                break;
            case const_int:
                match(TokenTypes.const_int);
                operandNode = new LiteralNode(token, "const_int");
                break;
            case const_string:
                match(TokenTypes.const_string);
                operandNode  = new LiteralNode(token, "const_string");
                break;
            case const_double:
                match(TokenTypes.const_double);
                operandNode = new LiteralNode(token, "const_double");
                break;
            default:
                throw new SyntacticException(lookahead, "Se esperaba nil, true, " +
                    "false o una constante." +
                    "Se encontró: " + lookahead.getName());
        }
        return operandNode;
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
        AST.setCurrentClass(class1.getName());
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
            BlockNode blockNode = new BlockNode(AST.getCurrentClass(), AST.getCurrentMethod());
            AST.addBlockNode(blockNode);
            sentencias(blockNode);
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
    public void sentencias(BlockNode blockNode) throws LexicalException, SyntacticException, ReaderException {
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
                blockNode.addSentence(sentencia());
                sentencias(blockNode);
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
            AST.setCurrentMethod(method.getName());
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
                AST.setCurrentMethod(method.getName());
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
            AST.setCurrentMethod(null);
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
    public ParenthesizedExpressionNode expresionParentizada2(Token token) throws LexicalException, SyntacticException, ReaderException {
        ParenthesizedExpressionNode parenthesizedExpressionNode;
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
            parenthesizedExpressionNode = new ParenthesizedExpressionNode();
            ExpressionNode expressionNode = expOr();
            parenthesizedExpressionNode.setExpressionNode(expressionNode);
            match(TokenTypes.parentheses2);
            ChainedNode chainedNode = expresionParentizada3();
            parenthesizedExpressionNode.setChainedNode(chainedNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', 'true', " +
                    "'false', ')', 'self', '+', '-', '!', '++', " +
                    "'--', identificadores o constantes. Se encontró: " + lookahead.getName());
        }
        return parenthesizedExpressionNode;
    }

    /**
     * <ExpresionParentizada3> ::= <Encadenado>
     * <ExpresionParentizada3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode expresionParentizada3() throws SyntacticException, LexicalException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero se coloca al final del código
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨AccesoSelf⟩ ::= pself ⟨AccesoSelf2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public SelfNode accesoSelf() throws SyntacticException, LexicalException, ReaderException {
        if (lookahead.getName() == TokenTypes.pself) {
            SelfNode selfNode = new SelfNode(lookahead, AST.getCurrentClass(), AST.getCurrentMethod());
            match(TokenTypes.pself);
            ChainedNode chainedNode = accesoSelf2();
            selfNode.setChainedNode(chainedNode);
            return selfNode;
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
    public ChainedNode accesoSelf2() throws SyntacticException, LexicalException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨AccesoVar2⟩ ::= <AccesoVar3>
     * ⟨AccesoVar2⟩ ::= brackets1 <ExpOr> brackets2 <AccesoVar3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public OperandNode accesoVar2(Token token) throws SyntacticException, LexicalException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.brackets1) {
            match(TokenTypes.brackets1);
            ArrayAccessNode arrayAccessNode = new ArrayAccessNode();
            ExpressionNode expressionNode = expOr();
            arrayAccessNode.setExpressionNode(expressionNode);
            match(TokenTypes.brackets2);
            ChainedNode chainedNode = accesoVar3();
            arrayAccessNode.setChainedNode(chainedNode);
            operandNode = arrayAccessNode;
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                    primarioFollows()
            ) {
                ChainedNode chainedNode = accesoVar3();
                if (chainedNode == null) {
                    operandNode = new VariableNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
                } else {
                    ChainedAccessNode chainedAccessNode = new ChainedAccessNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
                    chainedAccessNode.setChainedNode(chainedNode);
                    operandNode = chainedAccessNode;
                }
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.', '['." +
                        " Se encontró: " + lookahead.getName());
            }
        }
        return operandNode;
    }

    /**
     * <AccesoVar3> ::= <Encadenado>
     * <AccesoVar3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode accesoVar3() throws SyntacticException, LexicalException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨Encadenado⟩ ::= dot id <Encadenado2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode encadenado() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.dot) {
            match(TokenTypes.dot);
            if (lookahead.getName() == TokenTypes.id_obj) {
                Token token = lookahead;
                match(TokenTypes.id_obj);
                return encadenado2(token);
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
    public ChainedNode encadenado2(Token token) throws SyntacticException, LexicalException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.parentheses1) {
            chainedNode = llamadaMetodoEncadenado(token);
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.brackets1 ||
                primarioFollows()
            ) {
                chainedNode = accesoVariableEncadenado(token);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'," +
                        " '[', '('. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨Llamada-Metodo-Encadenado⟩ ::=  ⟨Argumentos-Actuales⟩ ⟨Llamada-Metodo-Encadenado2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedCallNode llamadaMetodoEncadenado(Token token) throws SyntacticException, LexicalException, ReaderException {
        ChainedCallNode chainedCallNode = new ChainedCallNode(token);
        if (lookahead.getName() == TokenTypes.parentheses1) {
            List<ExpressionNode> expressionList = argumentosActuales();
            chainedCallNode.setParameterList(expressionList);
            ChainedNode chainedNode = llamadaMetodoEncadenado2();
            chainedCallNode.setChainedNode(chainedNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '('. " +
                    "Se encontró: " + lookahead.getName());
        }
        return chainedCallNode;
    }

    /**
     * ⟨Llamada-Metodo-Encadenado2⟩ ::= <Encadenado>
     * ⟨Llamada-Metodo-Encadenado2⟩ ::=  λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode llamadaMetodoEncadenado2() throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨Acceso-Variable-Encadenado⟩ ::= <Acceso-Variable-Encadenado3>
     * ⟨Acceso-Variable-Encadenado⟩ ::= brackets1  <ExpOr> brackets2 <Acceso-Variable-Encadenado3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode accesoVariableEncadenado(Token token) throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.brackets1) {
            ChainedArrayAccessNode chainedArrayAccessNode = new ChainedArrayAccessNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
            match(TokenTypes.brackets1);
            ExpressionNode expressionNode = expOr();
            chainedArrayAccessNode.setExpression(expressionNode);
            match(TokenTypes.brackets2);
            ChainedNode chainedNode2 = accesoVariableEncadenado3();
            chainedArrayAccessNode.setChainedNode(chainedNode2);
            chainedNode = chainedArrayAccessNode;
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                    primarioFollows()
            ) {
                chainedNode = accesoVariableEncadenado3();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * <Acceso-Variable-Encadenado3> ::= <Encadenado>
     * <Acceso-Variable-Encadenado3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode accesoVariableEncadenado3() throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                //retorna, pues es lambda, pero el return se coloca al final
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ','. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨Argumentos-Actuales⟩ ::= parentheses1  <Argumentos-Actuales2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public List<ExpressionNode> argumentosActuales() throws LexicalException, SyntacticException, ReaderException {
        List<ExpressionNode> expressionList;
        if (lookahead.getName() == TokenTypes.parentheses1) {
            match(TokenTypes.parentheses1);
            expressionList = argumentosActuales2();
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba '('. " +
                    "Se encontró: " +  lookahead.getName());
        }
        return expressionList;
    }

    /**
     * <Argumentos-Actuales2> ::= ⟨Lista-Expresiones⟩ parentheses2
     * <Argumentos-Actuales2> ::= parentheses2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public List<ExpressionNode> argumentosActuales2() throws LexicalException, SyntacticException, ReaderException {
        List<ExpressionNode> expressionList = new ArrayList<>();
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
            listaExpresiones(expressionList);
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
        return expressionList;
    }

    /**
     * ⟨Lista-Expresiones⟩ ::= <ExpOr> ⟨Lista-Expresiones2⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public void listaExpresiones(List<ExpressionNode> expressionList) throws SyntacticException, LexicalException, ReaderException {
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
            ExpressionNode expressionNode = expOr();
            expressionList.add(expressionNode);
            listaExpresiones2(expressionList);
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
    public void listaExpresiones2(List<ExpressionNode> expressionList) throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.comma) {
            match(TokenTypes.comma);
            listaExpresiones(expressionList);
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
    public OperandNode operando() throws LexicalException, SyntacticException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.pnil ||
            lookahead.getName() == TokenTypes.ptrue ||
            lookahead.getName() == TokenTypes.pfalse ||
            lookahead.getName() == TokenTypes.const_int ||
            lookahead.getName() == TokenTypes.const_string ||
            lookahead.getName() == TokenTypes.const_double
        ) {
            operandNode = literal();
        }
        else {
            if (lookahead.getName() == TokenTypes.pself ||
                idClassSimilars() ||
                lookahead.getName() == TokenTypes.id_obj ||
                lookahead.getName() == TokenTypes.pnew
            ) {
                operandNode = primario();
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                        "'true', 'false', 'self', 'new', " +
                        "identificadores o constantes. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return operandNode;
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
    public OperandNode primario() throws LexicalException, SyntacticException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.pself) {
            operandNode = accesoSelf();
        }
        else {
            if (lookahead.getName() == TokenTypes.pnew) {
                operandNode = llamadaConclassor();
            }
            else {
                if (idClassSimilars()) {
                    operandNode = llamadaMetodoEstatico();
                }
                else {
                    if (lookahead.getName() == TokenTypes.id_obj) {
                        Token token = lookahead;
                        match(TokenTypes.id_obj);
                        operandNode = primario2(token);
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
        return operandNode;
    }


    /**
     * <Primario2> ::= <Argumentos-Actuales> <Primario3>
     * <Primario2> ::= <AccesoVar2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public OperandNode primario2(Token token) throws LexicalException, SyntacticException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.parentheses1) {
            MethodCallNode methodCallNode = new MethodCallNode(AST.getCurrentClass(),
                    AST.getCurrentClass(), AST.getCurrentMethod(), token, false);
            List<ExpressionNode> parameterList = argumentosActuales();
            methodCallNode.setParameterList(parameterList);
            ChainedNode chainedNode = primario3();
            methodCallNode.setChainNode(chainedNode);
            operandNode = methodCallNode;
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.brackets1 ||
                primarioFollows()
            ) {
                operandNode = accesoVar2(token);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.', '[', '('. Se encontró: " + lookahead.getName());
            }
        }
        return operandNode;
    }

    /**
     * <Primario3> ::= <Encadenado>
     * <Primario3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode primario3() throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                //Retorna, pues es lambda
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
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
    public SentenceNode sentencia() throws LexicalException, SyntacticException, ReaderException {
        SentenceNode sentenceNode;
        if (lookahead.getName() == TokenTypes.semicolon) {
            match(TokenTypes.semicolon);
            sentenceNode = null;
        } else {
            if (lookahead.getName() == TokenTypes.pret) {
                match(TokenTypes.pret);
                ExpressionNode expressionNode = sentenciaRet();
                sentenceNode = new ReturnNode(expressionNode);
            }
            else {
                if (lookahead.getName() == TokenTypes.pif) {
                    match(TokenTypes.pif);
                    match(TokenTypes.parentheses1);
                    ExpressionNode expressionNode = expOr();
                    match(TokenTypes.parentheses2);
                    SentenceNode sentenceNode1 = sentencia();
                    SentenceNode elseSentenceNode = sentenciaIf();
                    sentenceNode = new IfThenElseNode(expressionNode, sentenceNode1, elseSentenceNode);
                }
                else {
                    if (lookahead.getName() == TokenTypes.pwhile) {
                        match(TokenTypes.pwhile);
                        match(TokenTypes.parentheses1);
                        ExpressionNode expressionNode = expOr();
                        match(TokenTypes.parentheses2);
                        SentenceNode sentenceNode2 = sentencia();
                        sentenceNode = new WhileNode(expressionNode, sentenceNode2);
                    }
                    else {
                        if (lookahead.getName() == TokenTypes.braces1) {
                            sentenceNode = bloque();
                        }
                        else {
                            if (lookahead.getName() == TokenTypes.id_obj ||
                                lookahead.getName() == TokenTypes.pself
                            ) {
                                AssignmentNode assignmentNode = asignacion();
                                match(TokenTypes.semicolon);
                                sentenceNode = assignmentNode;
                            }
                            else {
                                if (lookahead.getName() == TokenTypes.parentheses1) {
                                    SentenceNode simpleSentenceNode = sentenciaSimple();
                                    match(TokenTypes.semicolon);
                                    sentenceNode = simpleSentenceNode;
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
        return sentenceNode;
    }

    /**
     * <Bloque> ::= braces1 <Sentencias>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public BlockNode bloque() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.braces1) {
            match(TokenTypes.braces1);
            BlockNode blockNode = new BlockNode(AST.getCurrentClass(), AST.getCurrentMethod());
            sentencias(blockNode);
            return blockNode;
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
    public AssignmentNode asignacion() throws LexicalException, SyntacticException, ReaderException {
        AssignmentNode sentenceNode;
        if (lookahead.getName() == TokenTypes.pself) {
            System.out.println(lookahead.getLexeme());
            ExpressionNode leftNode = accesoSelfSimple();
            match(TokenTypes.op_equal);
            ExpressionNode rightNode = expOr();
            sentenceNode = new AssignmentNode(leftNode, rightNode);
        }
        else {
            if (lookahead.getName() == TokenTypes.id_obj) {
                ExpressionNode leftNode = accesoVarSimple();
                match(TokenTypes.op_equal);
                System.out.println(lookahead.getLexeme());
                ExpressionNode rightNode = expOr();
                System.out.println(rightNode);
                sentenceNode = new AssignmentNode(leftNode, rightNode);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'self' " +
                        "o un identificador de método o variable. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
        return sentenceNode;
    }

    /**
     * <AccesoVar-Simple> ::= id <AccesoVar-Simple2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public OperandNode accesoVarSimple() throws LexicalException, SyntacticException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.id_obj) {
            Token token = lookahead;
            match(TokenTypes.id_obj);
            operandNode = accesoVarSimple2(token);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba un identificador de método o variable. " +
                    "Se encontró: " + lookahead.getName());
        }
        return operandNode;
    }

    /**
     * <SentenciaRet> ::= semicolon
     * <SentenciaRet> ::= <ExpOr> semicolon
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode sentenciaRet() throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.semicolon) {
            match(TokenTypes.semicolon);
            expressionNode = null;
        }
        else {
            if (expOrFirst()) {
                expressionNode = expOr();
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
        return expressionNode;
    }

    /**
     * <SentenciaIf> ::= pelse <Sentencia>
     * <SentenciaIf> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public SentenceNode sentenciaIf() throws LexicalException, SyntacticException, ReaderException {
        SentenceNode sentenceNode;
        if (lookahead.getName() == TokenTypes.pelse) {
            match(TokenTypes.pelse);
            sentenceNode = sentencia();
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
                sentenceNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba ';', " +
                        "'if', 'while', 'self', '{', '}', " +
                        "'ret', '(', o un identificador de método o variable. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return sentenceNode;
    }

    /**
     * <AccesoVar-Simple2> ::= <Encadenados-Simples>
     * <AccesoVar-Simple2> ::= brackets1 <ExpOr> brackets2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public OperandNode accesoVarSimple2(Token token) throws LexicalException, SyntacticException, ReaderException {
        OperandNode operandNode;
        if (lookahead.getName() == TokenTypes.brackets1) {
            ArrayAccessNode arrayAccessNode = new ArrayAccessNode();
            match(TokenTypes.brackets1);
            ExpressionNode expressionNode = expOr();
            arrayAccessNode.setExpressionNode(expressionNode);
            operandNode = arrayAccessNode;
            match(TokenTypes.brackets2);
        }
        else {
            if (lookahead.getName() == TokenTypes.dot ||
                lookahead.getName() == TokenTypes.op_equal
            ) {
                ChainedNode chainedNode = encadenadosSimples();
                if (chainedNode == null) {
                    operandNode = new VariableNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
                }
                else {
                    ChainedAccessNode chainedAccessNode = new ChainedAccessNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
                    chainedAccessNode.setChainedNode(chainedNode);
                    operandNode = chainedAccessNode;
                }
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '[', " +
                        "'.' o '='. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
        return operandNode;
    }


    /**
     * <Encadenados-Simples> ::= <Encadenado-Simple> <Encadenados-Simples>
     * <Encadenados-Simples> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedAccessNode encadenadosSimples() throws SyntacticException, LexicalException, ReaderException {
        ChainedAccessNode chainedAccessNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedAccessNode = encadenadoSimple();
            ChainedAccessNode chainedAccessNode2 = encadenadosSimples();
            chainedAccessNode.setChainedNode(chainedAccessNode2);
        }
        else {
            if (lookahead.getName() == TokenTypes.op_equal) {
                //Reducir, pues es lambda
                chainedAccessNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '.' " +
                        "o '='. Se encontró: " +
                        lookahead.getName());
            }
        }
        return chainedAccessNode;
    }

    /**
     * <AccesoSelf-Simple> ::= pself <Encadenados-Simples>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public SelfNode accesoSelfSimple() throws LexicalException, SyntacticException, ReaderException {
        SelfNode selfNode;
        if (lookahead.getName() == TokenTypes.pself) {
            Token token = lookahead;
            match(TokenTypes.pself);
            ChainedAccessNode chainedAccessNode = encadenadosSimples();
            selfNode = new SelfNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
            selfNode.setChainedNode(chainedAccessNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'self'. " +
                    "Se encontró: " + lookahead.getName());
        }
        return selfNode;
    }

    /**
     * <Encadenado-Simple> ::= dot id
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedAccessNode encadenadoSimple() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.dot);
        Token token = lookahead;
        match(TokenTypes.id_obj);
        return new ChainedAccessNode(token, AST.getCurrentClass(), AST.getCurrentMethod());
    }


    /**
     * <Sentencia-Simple> ::= parentheses1 <ExpOr> parentheses2
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public SimpleSentenceNode sentenciaSimple() throws LexicalException, SyntacticException, ReaderException {
        if (lookahead.getName() == TokenTypes.parentheses1) {
            match(TokenTypes.parentheses1);
            ExpressionNode expressionNode = expOr();
            match(TokenTypes.parentheses2);
            return new SimpleSentenceNode(expressionNode);
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
    public ExpressionNode expOr() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            ExpressionNode leftNode = expAnd();
            return expOr2(leftNode);
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
    public ExpressionNode expOr2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_or) {
            Token token = lookahead;
            match(TokenTypes.op_or);
            ExpressionNode rightNode = expAnd();
            ExpressionNode newLeftNode = new BinaryExpressionNode(leftNode, rightNode, token);
            expressionNode = expOr2(newLeftNode);
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
    }

    /**
     * <ExpAnd> ::= <ExpIgual> <ExpAnd2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expAnd() throws SyntacticException, LexicalException, ReaderException {
        ExpressionNode expressionNode;
        if (expOrFirst()) {
            ExpressionNode leftNode = expIgual();
            expressionNode = expAnd2(leftNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
        return expressionNode;
    }

    /**
     * <ExpAnd2> ::= op_and <ExpIgual> <ExpAnd2>
     * <ExpAnd2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expAnd2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        Token token = lookahead;
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_and) {
            match(TokenTypes.op_and);
            ExpressionNode rightNode = expIgual();
            ExpressionNode newLeftNode = new BinaryExpressionNode(leftNode, rightNode, token);
            expressionNode = expAnd2(newLeftNode);
        }
        else {
            if (lookahead.getName() == TokenTypes.op_or ||
                lookahead.getName() == TokenTypes.parentheses2 ||
                lookahead.getName() == TokenTypes.brackets2 ||
                lookahead.getName() == TokenTypes.semicolon ||
                lookahead.getName() == TokenTypes.comma
            ) {
                //retorna, pues es lambda
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '&&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
    }

    /**
     * <ExpIgual> ::= <ExpCompuesta> <ExpIgual2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expIgual() throws SyntacticException, LexicalException, ReaderException {
        ExpressionNode expressionNode;
        if (expOrFirst()) {
            ExpressionNode leftNode = expCompuesta();
            expressionNode = expIgual2(leftNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
        return expressionNode;
    }

    /**
     * <ExpIgual2> ::= <OpIgual> <ExpCompuesta> <ExpIgual2>
     * <ExpIgual2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expIgual2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_rel_equal ||
            lookahead.getName() == TokenTypes.op_rel_notequal
        ) {
            Token token = opIgual();
            ExpressionNode rightNode = expCompuesta();
            ExpressionNode newLeftNode = new BinaryExpressionNode(leftNode, rightNode, token);
            expressionNode = expIgual2(newLeftNode);
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
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '==', '!=', '&&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
    }

    /**
     * <ExpCompuesta> ::= <ExpAd> <ExpCompuesta2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expCompuesta() throws SyntacticException, LexicalException, ReaderException {
        ExpressionNode finalExpressionNode;
        if (expOrFirst()) {
           ExpressionNode leftNode = expAd();
          return expCompuesta2(leftNode);
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
    public ExpressionNode expCompuesta2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_rel_less ||
            lookahead.getName() == TokenTypes.op_rel_greater ||
            lookahead.getName() == TokenTypes.op_rel_lessequal ||
            lookahead.getName() == TokenTypes.op_rel_greaterequal
        ) {
            Token token = opCompuesto();
            ExpressionNode rightNode = expAd();
            expressionNode = new BinaryExpressionNode(leftNode, rightNode, token);
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
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
    }

    /**
     * <ExpAd> ::= <ExpMul> <ExpAd2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expAd() throws SyntacticException, LexicalException, ReaderException {
        if (expOrFirst()) {
            ExpressionNode leftNode = expMul();
            return expAd2(leftNode);
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
    public ExpressionNode expAd2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub
        ) {
            Token token = opAdd();
            ExpressionNode rightNode = expMul();
            ExpressionNode newLeftNode = new BinaryExpressionNode(leftNode, rightNode, token);
            expressionNode = expAd2(newLeftNode);
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
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '+', '-', '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
    }

    /**
     * <ExpMul> ::= <ExpUn> <ExpMul2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expMul() throws SyntacticException, LexicalException, ReaderException {
        ExpressionNode expressionNode;
        if (expOrFirst()) {
            ExpressionNode leftNode = expUn();
            expressionNode = expMul2(leftNode);
        }
        else {
            throw new SyntacticException(lookahead, "Se esperaba 'nil', " +
                    "'true', 'false', '(', 'self', " +
                    "'new', '+', '-', '!', '++', '--', " +
                    "una constante o un identificador. Se " +
                    "encontró: " + lookahead.getName());
        }
        return expressionNode;
    }

    /**
     * <ExpMul2> ::= <OpMul> <ExpUn> <ExpMul2>
     * <ExpMul2> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expMul2(ExpressionNode leftNode) throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode;
        if (lookahead.getName() == TokenTypes.op_mult ||
            lookahead.getName() == TokenTypes.op_div ||
            lookahead.getName() == TokenTypes.op_mod ||
            lookahead.getName() == TokenTypes.pdiv
        ) {
            Token token = opMul();
            ExpressionNode rightNode = expUn();
            ExpressionNode newLeftNode = new BinaryExpressionNode(leftNode, rightNode, token);
            expressionNode = expMul2(newLeftNode);
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
                expressionNode = leftNode;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', '/', '%', 'div', '+', '-', '<', '>', " +
                        "'<=', '>=', '==', '!=', &&', '||', " +
                        "')', ']', ';' o ','. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return expressionNode;
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
    public ExpressionNode expUn() throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode returnExpressionNode;
        if (lookahead.getName() == TokenTypes.op_sum ||
            lookahead.getName() == TokenTypes.op_sub ||
            lookahead.getName() == TokenTypes.op_not ||
            lookahead.getName() == TokenTypes.op_increment ||
            lookahead.getName() == TokenTypes.op_decrement
        ) {
            Token token = opUnario();
            ExpressionNode expressionNode = expUn();
            returnExpressionNode = new UnaryExpressionNode(expressionNode, token);
        }
        else {
            if (lookahead.getName() == TokenTypes.parentheses1) {
                match(TokenTypes.parentheses1);
                returnExpressionNode = expUnAux();
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
                    returnExpressionNode = operando();
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
        return returnExpressionNode;
    }

    /**
     * <ExpUnAux> ::= pint parentheses2 <ExpUn>
     * <ExpUnAux> ::= <ExpresionParentizada2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ExpressionNode expUnAux() throws LexicalException, SyntacticException, ReaderException {
        ExpressionNode expressionNode1;
        if (expOrFirst()) {
            expressionNode1 = expresionParentizada2(lookahead);
        }
        else {
            if (lookahead.getName() == TokenTypes.pint) {
                Token token = lookahead;
                match(TokenTypes.pint);
                match(TokenTypes.parentheses2);
                ExpressionNode expressionNode = expUn();
                expressionNode1 = new UnaryExpressionNode(expressionNode, token);
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba 'int', 'nil', " +
                        "'true', 'false', '(', 'self', " +
                        "'new', '+', '-', '!', '++', '--', " +
                        "una constante o un identificador. Se " +
                        "encontró: " + lookahead.getName());
            }
        }
        return expressionNode1;
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
    public MethodCallNode llamadaMetodo(String class1, boolean isStatic) throws LexicalException, SyntacticException, ReaderException {
        MethodCallNode methodCallNode = new MethodCallNode(class1, AST.getCurrentClass(),
                AST.getCurrentMethod(), lookahead, isStatic);
        if (lookahead.getName() == TokenTypes.id_obj) {
            match(TokenTypes.id_obj);
        } else {
            throw new SyntacticException(lookahead, "Se esperaba identificador de método o variable. " +
                    "Se encontró: " + lookahead.getName());
        }
        List<ExpressionNode> expressionList = argumentosActuales();
        methodCallNode.setParameterList(expressionList);
        ChainedNode chainedNode = llamadaMetodo2();
        methodCallNode.setChainNode(chainedNode);
        return methodCallNode;
    }

    /**
     * ⟨Llamada-Metodo2⟩ ::= <Encadenado>
     * ⟨Llamada-Metodo2⟩ ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public ChainedNode llamadaMetodo2() throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                //lambda
                chainedNode = null;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
    }

    /**
     * ⟨Llamada-Metodo-Estatico⟩ ::= id_class dot ⟨Llamada-Metodo⟩
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public MethodCallNode llamadaMetodoEstatico() throws LexicalException, SyntacticException, ReaderException {
        String class1 = lookahead.getLexeme();
        matchIdClassSimilars();
        match(TokenTypes.dot);
        return llamadaMetodo(class1, true);
    }

    /**
     * ⟨Llamada-Conclassor⟩ ::= pnew  <Llamada-Conclassor2>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    public NewNode llamadaConclassor() throws LexicalException, SyntacticException, ReaderException {
        match(TokenTypes.pnew);
        return llamadaConclassor2();
    }

    /**
     * <Llamada-Conclassor2> ::= id_class ⟨Argumentos-Actuales⟩ <Llamada-Conclassor3>
     * <Llamada-Conclassor2> ::= ⟨Tipo-Primitivo⟩ brackets1 <ExpOr> brackets2 <Llamada-Conclassor3>
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    private NewNode llamadaConclassor2() throws LexicalException, SyntacticException, ReaderException {
        NewNode newNode;
        if (idClassSimilars()) {
            NewNode newNode1 = new NewNode(lookahead, "class");
            matchIdClassSimilars();
            List<ExpressionNode> parametersList = argumentosActuales();
            newNode1.setParameterList(parametersList);
            ChainedNode chainedNode = llamadaConclassor3();
            newNode1.setChainedNode(chainedNode);
            newNode = newNode1;
        }
        else {
            if (lookahead.getName() == TokenTypes.pstr || lookahead.getName() == TokenTypes.pbool ||
                    lookahead.getName() == TokenTypes.pint || lookahead.getName() == TokenTypes.pdouble) {
                NewNode newNode2 = new NewNode(lookahead, "array");
                tipoPrimitivo();
                match(TokenTypes.brackets1);
                ExpressionNode expressionNode = expOr();
                newNode2.setExpressionNode(expressionNode);
                match(TokenTypes.brackets2);
                ChainedNode chainedNode = llamadaConclassor3();
                newNode2.setChainedNode(chainedNode);
                newNode = newNode2;
            }
            else {
                throw new SyntacticException(lookahead, "Se esperaba identificador de clase, " +
                        "'str', 'bool', 'int' o 'double'. " +
                        "Se encontró: " + lookahead.getName());
            }
        }
        return newNode;
    }

    /**
     * <Llamada-Conclassor3> ::= <Encadenado>
     * <Llamada-Conclassor3> ::= λ
     * @throws ReaderException Excepción del reader
     * @throws LexicalException Excepción ocasionada por un error léxico
     * @throws SyntacticException Excepción ocasionada por un error sintáctico
     * @author Paulina Suden y Tomás Rando
     */
    private ChainedNode llamadaConclassor3() throws LexicalException, SyntacticException, ReaderException {
        ChainedNode chainedNode;
        if (lookahead.getName() == TokenTypes.dot) {
            chainedNode = encadenado();
        }
        else {
            if (primarioFollows()) {
                chainedNode = null;
            } else {
                throw new SyntacticException(lookahead, "Se esperaba '*', " +
                        "'/', '%', 'div', '+', '-', '<', " +
                        "'>', '<=', '>=', '==', '!=', '&&', '||'," +
                        " ')', ']', ';', ',', '.'. Se encontró: " + lookahead.getName());
            }
        }
        return chainedNode;
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
