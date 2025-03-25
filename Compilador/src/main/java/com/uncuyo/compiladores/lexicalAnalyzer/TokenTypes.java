package com.uncuyo.compiladores.lexicalAnalyzer;

/**
 * Enum para representar los tipos de tokens existentes
 * @author Paulina Suden y Tomás Rando
 */
public enum TokenTypes {
    //KEY WORDS
    pstart, pclass, pimpl, pelse, pfalse, pif, pret, pwhile, ptrue, pnil, pnew, pfn,
    pst, ppub, pself, pdiv, pbool, pstr, pint, pdouble, pobject, pio, parray, pvoid,

    //OPERADOR ARITMETICO
    op_sum, op_div, op_mult, op_sub, op_mod, op_increment, op_decrement,

    //OPERADOR LÓGICO
    op_log_and, op_log_or, op_log_not,

    //OPERADOR RELACIONAL
    op_rel_less, op_rel_greater, op_rel_equal, op_rel_greaterequal, op_rel_lessequal, op_rel_notequal,

    //OPERADOR ASIGNACIÓN
    op_equal,

    //SIMBOLOS ESPECIALES
    brackets1, brackets2, parentheses1, parentheses2, braces1, braces2, dot, comma, semicolon,

    //CONSTANTES
    const_int, const_double, const_string,

    //IDENTIFICADORES
    id_class, id_obj,

    //EOF (End of file)
    end_of_file;
}

