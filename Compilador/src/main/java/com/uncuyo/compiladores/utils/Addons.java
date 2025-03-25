package com.uncuyo.compiladores.utils;


import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.lexicalAnalyzer.ModifiedFileReader;


/**
 * Utilidades para reutilizar facilmente
 * @author Tomás Rando
 */
public class Addons {

    /**
     * Verifica que un caracter sea una letra (sin tilde)
     * @param c Character
     * @return boolean. True si es letra, false si no
     * @author Tomás Rando
     */
    public static boolean isLetter(Character c) {
        return (isUpperCase(c) || isLowerCase(c));
    }

    /**
     * Verifica que un caracter sea un operador aritmetico
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
     * @param c Character
     * @return boolean. True si es letra mayuscula, false si no
     * @author Tomás Rando
     */
    public static boolean isUpperCase(Character c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Verifica que un caracter sea una letra minuscula
     * @param c Character
     * @return boolean. True si es letra minuscula, false si no
     * @author Tomás Rando
     */
    public static boolean isLowerCase(Character c) {
        return (c >= 'a' && c <= 'z');
    }

}

