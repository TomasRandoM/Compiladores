package com.uncuyo.compiladores.syntacticAnalyzer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.uncuyo.compiladores.exceptions.SyntacticException;
import org.junit.jupiter.api.Test;

public class SyntacticAnalyzerTest {

    /**
     * Incorrecto. Contiene 2 start
     * @author Tomas Rando
     */
    @Test
    public void test1Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test1.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test2Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test2.s").program();
        });
    }

    /**
     * Incorrecto. El codigo no contiene la palabra start
     * @author Tomas Rando
     */
    @Test
    public void test3Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test3.s").program();
        });
    }

    /**
     * Incorrecto. En class solo van declaraciones.
     * @author Tomas Rando
     */
    @Test
    public void test4Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test4.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test5Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test5.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test6Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test6.s").program();
        });
    }

    /**
     * Incorrecto. Falta un parentesis en la llamada a un metodo de la linea 22
     * @author Tomas Rando
     */
    @Test
    public void test7Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test7.s").program();
        });
    }

    /**
     * Incorrecto. Falta ; en linea 5
     * @author Tomas Rando
     */
    @Test
    public void test8Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test8.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test9Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test9.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test10Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test10.s").program();
        });
    }

    /**
     * Incorrecto. Existe una clase en una serie de encadenamientos.
     * @author Tomas Rando
     */
    @Test
    public void test11Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test11.s").program();
        });
    }
}
