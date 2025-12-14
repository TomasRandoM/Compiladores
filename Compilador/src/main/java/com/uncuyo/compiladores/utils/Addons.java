package com.uncuyo.compiladores.utils;


import com.uncuyo.compiladores.lexicalAnalyzer.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * Utilidades para reutilizar facilmente
 * @author Tomás Rando
 */
public class Addons {

    private static final Set<Character> specialLetters = new HashSet<>();

    static {
        specialLetters.add('ñ');
        specialLetters.add('Ñ');
        specialLetters.add('ü');
        specialLetters.add('Ü');
        specialLetters.add('á');
        specialLetters.add('é');
        specialLetters.add('í');
        specialLetters.add('ó');
        specialLetters.add('ú');
        specialLetters.add('Á');
        specialLetters.add('É');
        specialLetters.add('Í');
        specialLetters.add('Ó');
        specialLetters.add('Ú');

    }

    /**
     * Verifica que un caracter sea una letra (sin tilde)
     *
     * @param c Character
     * @return boolean. True si es letra, false si no
     * @author Tomás Rando
     */
    public static boolean isLetter(Character c) {
        return (isUpperCase(c) || isLowerCase(c));
    }


    /**
     * Verifica que un caracter sea un operador aritmetico
     *
     * @param c Character
     * @return boolean. True si es un operador, false si no
     * @author Tomás Rando
     */
    public static boolean isArithmeticOperator(Character c) {
        boolean is = false;
        if (c == '*' || c == '+' || c == '-' || c == '/' || c == '%') {
            is = true;
        }
        return is;
    }

    /**
     * Verifica que un caracter sea una letra mayuscula
     *
     * @param c Character
     * @return boolean. True si es letra mayuscula, false si no
     * @author Tomás Rando
     */
    public static boolean isUpperCase(Character c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Verifica que un caracter sea una letra minuscula
     *
     * @param c Character
     * @return boolean. True si es letra minuscula, false si no
     * @author Tomás Rando
     */
    public static boolean isLowerCase(Character c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Verifica que un caracter sea un operador booleano
     *
     * @param c Character
     * @return boolean. True si es un operador booleano, false si no
     * @author Paulina Suden
     */
    public static boolean isBooleanOperator(Character c) {
        return c == '&' || c == '|' || c == '!';
    }

    /**
     * Verifica que un caracter sea un simbolo especial
     *
     * @param c Character
     * @return boolean. True si es un simbolo especial, false si no
     * @author Paulina Suden
     */
    public static boolean isSpecialSymbol(Character c) {
        return c == '[' || c == ']' || c == '(' || c == ')' || c == '{'
                || c == '}' || c == '.' || c == ',' || c == ';' || c == ':';
    }

    /**
     * Verifica si un caracter es un símbolo utilizado en el español (tíldes, diéresis, ñ).
     * @param c Character
     * @author Tomás Rando
     * @return True si es un símbolo especial del abecedario o no
     */
    public static boolean isSpecialLetter(Character c) {
        return specialLetters.contains(c);
    }

    /**
     * Verifica si un caracter está definido en nuestra gramática. Estos símbolos son
     * los caracteres imprimibles (www.ascii-code.com/) y, adicionalmente
     * las vocales con tílde, la ñ y la u con diéresis.
     * @param c Character
     * @return True si pertenece, False si no.
     */
    public static boolean isInGrammar(Character c) {
        boolean present;
        int symbolCode;

        symbolCode = (int) c;
        present = (symbolCode > 31 && symbolCode < 127);
        if (!present) {
            present = isSpecialLetter(c);
        }
        return present;
    }

    /**
     * Compara dos listas de tokens que se le pasan como parámetro. Utilizando principalmente en los tests JUnit
     * @param li1 Lista de tokens 1
     * @param li2 Lista de tokens 2
     * @return boolean. true si son iguales, false si no.
     * @author Tomás Rando
     */
    public static boolean compareTokenLists(List<Token> li1, List<Token> li2) {
        boolean condition = li1.size() == li2.size();

        if (condition) {
            Token token1;
            Token token2;
            boolean stop = false;
            int i = 0;
            while (!stop) {
                token1 = li1.get(i);
                token2 = li2.get(i);
                if (token1.getName() != token2.getName() ||
                        !(token1.getLexeme().equals(token2.getLexeme())) ||
                        token1.getRow() != token2.getRow() ||
                        token1.getColumn() != token2.getColumn() ||
                        !(Objects.equals(token1.getValor(), token2.getValor()))) {
                    condition = false;
                }
                i++;
                if (i == li1.size() || !condition) {
                    stop = true;
                }
            }
        }
        return condition;
    }

    pu
}