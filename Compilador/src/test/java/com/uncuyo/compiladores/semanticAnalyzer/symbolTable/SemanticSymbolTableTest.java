package com.uncuyo.compiladores.semanticAnalyzer.symbolTable;

import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.exceptions.SyntacticException;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemanticSymbolTableTest {

    /**
     * Incorrecto. Hay dos definiciones de la clase A
     * @author Tomas Rando
     */
    @Test
    public void testClassRedefinitionIncorrect() {
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/classRedefinition.s").program();
        });
    }

    /**
     * Incorrecto. Hay dos impl de la clase A
     * @author Tomas Rando
     */
    @Test
    public void testImplRedefinitionIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/implRedefinition.s").program();
        });
    }

    /**
     * Incorrecto. Clase B hereda de A y A no está definida
     * @author Tomas Rando
     */
    @Test
    public void testUndefinedAncestorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/undefinedAncestor.s").program();
        });
    }

    /**
     * Incorrecto. Tipo A del metodo getX no está definido
     * @author Tomas Rando
     */
    @Test
    public void testUndefinedMethodTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/undefinedMethodType.s").program();
        });
    }

    /**
     * Incorrecto. Tipo A de la variable b no está definido
     * @author Tomas Rando
     */
    @Test
    public void testUndefinedVariableTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/undefinedVariableType.s").program();
        });
    }

    /**
     * Correcto. Se verifica la herencia
     * @author Tomas Rando
     */
    @Test
    public void testInheritanceCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/inheritance.s").program();
        });
    }
}
