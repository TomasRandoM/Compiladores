package com.uncuyo.compiladores.utils;

/**
 * Utilidades para reutilizar facilmente
 */
public class Addons {

    /**
     * Verifica que un caracter sea una letra (sin tilde)
     * @param c Character
     * @return boolean. True si es letra, false si no
     */
    public static boolean isLetter(Character c) {
        return (isUpperCase(c) || isLowerCase(c));
    }

    /**
     * Verifica que un caracter sea un operador aritmetico
     * @param c Character
     * @return boolean. True si es un operador, false si no
     */
    public static boolean isArithmeticOperator(Character c) {
        if (c == '*' || c == '+' || c == '-' || c == '%') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica que un caracter sea un operador relacional
     * @param c Character
     * @return boolean. True si es un operador, false si no
     */
    public static boolean isRelationalOperator(Character c) {
        if (c == '>' || c == '<' || c == '=' || c == '!') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica que un caracter sea una letra mayuscula
     * @param c Character
     * @return boolean. True si es letra mayuscula, false si no
     */
    public static boolean isUpperCase(Character c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Verifica que un caracter sea una letra minuscula
     * @param c Character
     * @return boolean. True si es letra minuscula, false si no
     */
    public static boolean isLowerCase(Character c) {
        return (c >= 'a' && c <= 'z');
    }

}

