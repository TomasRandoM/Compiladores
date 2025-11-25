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

    /**
     * Incorrecto. Herencia circular simple o intermedia
     * @author Paulina Suden
     */
    @Test
    public void testCircularInheritanceIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/circularInheritance.s").program();
        });
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/circularInheritance2.s").program();
        });
    }

    /**
     * Incorrecto. Atributos con el mismo nombre
     * @author Paulina Suden
     */
    @Test
    public void testAttributesWithSameNameIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/attributesWithTheSameName.s").program();
        });
    }

    /**
     * Incorrecto. Variables locales con tipo inexistente
     * @author Paulina Suden
     */
    @Test
    public void testLocalVarTypeNotExistsIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/localVarTypeNotExists.s").program();
        });
    }

    /**
     * Incorrecto. Tipo de atributo inexistente
     * @author Paulina Suden
     */
    @Test
    public void testAttributeTypeNotExistsIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/attributeTypeNotExists.s").program();
        });
    }

    /**
     * Incorrecto. Tipo de parámetro inexistente
     * @author Paulina Suden
     */
    @Test
    public void testParameterTypeNotExistsIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/parameterTypeNotExists.s").program();
        });
    }

    /**
     * Incorrecto. Tipo de retorno inexistente
     * @author Paulina Suden
     */
    @Test
    public void testNonExistingReturnTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/nonExistingReturnType.s").program();
        });
    }

    /**
     * Incorrecto. Variables con el mismo nombre
     * @author Paulina Suden
     */
    @Test
    public void testVariablesWithSameNameIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/variablesWithTheSameName.s").program();
        });
    }

    /**
     * Incorrecto. Redefinición de métodos mal hecha
     * @author Paulina Suden
     */
    @Test
    public void testRedefinedMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodArguments.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodArgumentsTypes.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodType.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodWithInvalidSignature.s").program();
        });
    }

    /**
     * Incorrecto. Redefinición de métodos estáticos
     * @author Paulina Suden
     */
    @Test
    public void testRedefinedStaticMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedStaticMethod.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedStaticMethod2.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedStaticMethod3.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedStaticMethod3.s").program();
        });
    }

    /**
     * Correcto. Constructor simple
     * @author Paulina Suden
     */
    @Test
    public void testConstructorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/constructor.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/constructor2.s").program();
        });
    }

    /**
     * Correcto. Constructor con múltiples parámetros
     * @author Paulina Suden
     */
    @Test
    public void testConstructor2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/constructor2.s").program();
        });
    }

    /**
     * Correcto. Redefinición de métodos válida
     * @author Paulina Suden
     */
    @Test
    public void testRedefinedMethodCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/redefinedMethod.s").program();
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/redefinedMethod2.s").program();
        });
    }

    /**
     * Correcto. Herencia de IO
     * @author Paulina Suden
     */
    @Test
    public void testIOInheritanceCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/IOInheritance.s").program();
        });
    }

    /**
     * Correcto. Programa completo
     * @author Paulina Suden
     */
    @Test
    public void testCorrectProgram() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/correctProgram.s").program();
        });
    }
}
