package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.Etapa1;
import com.uncuyo.compiladores.exceptions.LexicalException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Clase que implementa los tests para el analizador léxico
 * @author Tomás Rando
 */
public class LexicalAnalyzerTest {

    /**
     * Test para verificar el error de una string no cerrada
     * @author Tomás Rando
     */
    @Test
    public void testUnclosedString() {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input2.s");
        });
    }

    /**
     * Test para verificar el error de un identificador mal formado
     * @author Tomás Rando
     */
    @Test
    public void testInvalidIdentifier() {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input4.s");
        });
    }

    /**
     * Test para verificar el error de un comentario no cerrado
     * @author Tomás Rando
     */
    @Test
    public void testUnclosedComment() {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input5.s");
        });
    }

    /**
     * Test para verificar el error de entero fuera de límites
     * @author Tomás Rando
     */
    @Test
    public void testUnboundedInt() {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input6.s");
        });
    }

    /**
     * Test para verificar el error de string fuera de límites
     * @author Tomás Rando
     */
    @Test
    public void testStringExceedsLimits() {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input8.s");
        });
    }
}
