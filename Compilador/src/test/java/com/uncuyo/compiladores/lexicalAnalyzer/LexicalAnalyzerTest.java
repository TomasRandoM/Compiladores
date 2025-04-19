package com.uncuyo.compiladores.lexicalAnalyzer;

import com.uncuyo.compiladores.Etapa1;
import com.uncuyo.compiladores.exceptions.LexicalException;
import com.uncuyo.compiladores.exceptions.ReaderException;
import com.uncuyo.compiladores.utils.Addons;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * Test para verificar el caso de un número entero negativo
     * @author Tomás Rando
     */
    @Test
    public void testNegativeInt() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.op_sub, "-", null, 1, 0));
        testList.add(new Token(TokenTypes.const_int, "123", 123, 1, 1));
        testList.add(new Token(TokenTypes.end_of_file, "", null, 1, 4));
        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input7.s"), testList));
    }

    /**
     * Test para verificar el caso de operadores con comentarios y strings.
     * @author Tomás Rando
     */
    @Test
    public void testOperatorsAndString() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.op_sum, "+", null, 1, 0));
        testList.add(new Token(TokenTypes.op_div, "/", null, 1, 2));
        testList.add(new Token(TokenTypes.const_string, "hola", null, 2, 0));
        testList.add(new Token(TokenTypes.end_of_file, "", null, 2, 6));
        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input.s"), testList));
    }

    /**
     * Test para verificar el caso de identificadores junto a comentarios y saltos de línea
     * @author Tomás Rando
     */
    @Test
    public void testIdentifierComments() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.id_class, "Clase", null, 2, 0));
        testList.add(new Token(TokenTypes.pimpl, "impl", null, 2, 6));
        testList.add(new Token(TokenTypes.pstart, "start", null, 2, 11));
        testList.add(new Token(TokenTypes.id_obj, "metodo1", null, 3, 0));
        testList.add(new Token(TokenTypes.id_obj, "meto_do2", null, 4, 0));
        testList.add(new Token(TokenTypes.id_obj, "variable", null, 5, 0));
        testList.add(new Token(TokenTypes.id_obj, "variable3", null, 6, 0));
        testList.add(new Token(TokenTypes.id_obj, "variable", null, 7, 12));
        testList.add(new Token(TokenTypes.end_of_file, "", null, 7, 20));

        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input3.s"), testList));
    }

    /**
     * Test que engloba varios tokens, entre ellos constantes enteras y doubles
     * @author Tomás Rando
     */
    @Test
    public void testMixedIntDoubles() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.pstart, "start", null, 1, 0));
        testList.add(new Token(TokenTypes.braces1, "{", null, 1, 6));
        testList.add(new Token(TokenTypes.braces2, "}", null, 1, 8));
        testList.add(new Token(TokenTypes.id_obj, "impl48", null, 1, 10));
        testList.add(new Token(TokenTypes.const_string, "hola", null, 1, 17));
        testList.add(new Token(TokenTypes.const_string, "adios", null, 1, 24));
        testList.add(new Token(TokenTypes.const_int, "9787", 9787, 1, 32));
        testList.add(new Token(TokenTypes.const_double, "7788.787", (Double) 7788.787, 1, 37));
        testList.add(new Token(TokenTypes.parentheses1, "(", null, 1, 46));
        testList.add(new Token(TokenTypes.parentheses2, ")", null, 1, 48));
        testList.add(new Token(TokenTypes.op_mult, "*", null, 1, 50));
        testList.add(new Token(TokenTypes.op_sum, "+", null, 1, 52));
        testList.add(new Token(TokenTypes.const_string, "hjhfdsfjds)", null, 1, 54));
        testList.add(new Token(TokenTypes.id_class, "Clase", null, 1, 83));
        testList.add(new Token(TokenTypes.end_of_file, "", null, 1, 88));
        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input1.s"), testList));
    }

    /**
     * Test que verifica el caso de un entero pegado a un identificador. En esta
     * ocasión NO debería dar error
     * @author Tomás Rando
     */
    @Test
    public void testIntIdentifier() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.const_int, "4", 4, 1, 0));
        testList.add(new Token(TokenTypes.id_class, "Hola", null, 1, 1));
        testList.add(new Token(TokenTypes.end_of_file, "", null, 1, 5));
        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input11.s"), testList));
    }

    /**
     * Verifica la excepción cuando encuentra un símbolo incorrecto en una string
     * @author Tomás Rando
     */
    @Test
    public void testBadSymbolString() throws LexicalException, ReaderException {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input13.s");
        });
    }

    /**
     * Verifica la excepción cuando encuentra un símbolo incorrecto en un comentario singleline
     * @author Tomás Rando
     */
    @Test
    public void testBadSymbolSinglelineComment() throws LexicalException, ReaderException {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input12.s");
        });
    }

    /**
     * Verifica la excepción cuando encuentra un símbolo incorrecto en un comentario multiline
     * @author Tomás Rando
     */
    @Test
    public void testBadSymbolMultilineComment() throws LexicalException, ReaderException {
        assertThrows(LexicalException.class, () -> {
            Etapa1.getAllTokens("tests/lexico/input14.s");
        });
    }

    /**
     * Test que verifica un comentario singleline
     * @author Tomás Rando
     */
    @Test
    public void testSingleComment() throws LexicalException, ReaderException {
        List<Token> testList = new ArrayList<>();
        testList.add(new Token(TokenTypes.end_of_file, "", null, 1, 0));
        assertTrue(Addons.compareTokenLists(Etapa1.getAllTokens("tests/lexico/input9.s"), testList));
    }

}
