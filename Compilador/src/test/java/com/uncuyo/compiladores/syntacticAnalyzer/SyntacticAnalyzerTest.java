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

    /**
     * Incorrecto. 123 no es id_class.
     * @author Paulina Suden
     */
    @Test
    public void test12Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test12.s").program();
        });
    }

    /**
     * Incorrecto. Falta tipo luego de :.
     * @author Paulina Suden
     */
    @Test
    public void test13Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test13.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test14Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test14.s").program();
        });
    }

    /**
     * Incorrecto. Clase sin llave de cierre.
     * @author Paulina Suden
     */
    @Test
    public void test15Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test15.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test16Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test16.s").program();
        });
    }

    /**
     * Incorrecto. Fibonacci es un id_class.
     * @author Paulina Suden
     */
    @Test
    public void test17Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test17.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test18Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test18.s").program();
        });
    }

    /**
     * Incorrecto. Falta [].
     * @author Paulina Suden
     */
    @Test
    public void test19Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test19.s").program();
        });
    }

    /**
     * Incorrecto. ++ está del lado derecho.
     * @author Paulina Suden
     */
    @Test
    public void test20Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test20.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test21Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test21.s").program();
        });
    }

    /**
     * Incorrecto. if sin parentesis.
     * @author Paulina Suden
     */
    @Test
    public void test22Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test22.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test23Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test23.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test24Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test24.s").program();
        });
    }

    /**
     * Incorrecto. Parentesis mal cerrado.
     * @author Paulina Suden
     */
    @Test
    public void test25Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test25.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test26Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test26.s").program();
        });
    }

    /**
     * Correcto.
     * @author Paulina Suden
     */
    @Test
    public void test27Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test27.s").program();
        });
    }

    /**
     * Incorrecto. Falta start.
     * @author Paulina Suden
     */
    @Test
    public void test28Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test28.s").program();
        });
    }

}
