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
     * Correcto. B redefine getX(Int x) de A
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedMethodCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/redefinedMethod.s").program();
        });
    }

    /**
     * Correcto. La clase A sí posee constructor
     * @author Tomas Rando
     */
    @Test
    public void testConstructorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/constructor.s").program();
        });
    }

    /**
     * Correcto. La clase A posee class e impl
     * @author Tomas Rando
     */
    @Test
    public void testClassAndImplCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/classAndImpl.s").program();
        });
    }

    /**
     * Correcto. Una clase no posee dos métodos con el mismo nombre
     * @author Tomas Rando
     */
    @Test
    public void testDefinedMethodsCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/definedMethods.s").program();
        });
    }

    /**
     * Correcto. La variable y el parametro tienen diferente nombre
     * @author Tomas Rando
     */
    @Test
    public void testVariableAnParameterCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/variableAndParameter.s").program();
        });
    }

    /**
     * Correcto. getB2(Int x, B y) recibe un parametro de tipo B
     * @author Tomas Rando
     */
    @Test
    public void testParameterDefinedTypeCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/parameterDefinedType.s").program();
        });
    }

    /**
     * Correcto. Los atributos y variables tienen diferente nombre
     * @author Tomas Rando
     */
    @Test
    public void testAttributeAndVariableNotRedefinedCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/attributeAndVariableNotRedefined.s").program();
        });
    }

    /**
     * Correcto. B hereda x de A y no lo intenta redefinir
     * @author Tomas Rando
     */
    @Test
    public void testInheritedAttributeNotRedefinedCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/passing/inheritedAttributeNotRedefined.s").program();
        });
    }

    /**
     * Incorrecto. B redefine getX() de A pero con diferente tipo de parámetros
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedMethodWithInvalidSignatureIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodWithInvalidSignature.s").program();
        });
    }

    /**
     * Incorrecto. B redefine getX() de A pero con diferente cantidad de parámetros
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedMethodWithInvalidSignature2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodWithInvalidSignature2.s").program();
        });
    }

    /**
     * Incorrecto. B redefine getX() de A pero con diferente cantidad de parámetros
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedMethodWithInvalidSignature3Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedMethodWithInvalidSignature3.s").program();
        });
    }

    /**
     * Incorrecto. La clase A no tiene constructor
     * @author Tomas Rando
     */
    @Test
    public void testNoConstructorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/noConstructor.s").program();
        });
    }

    /**
     * Incorrecto. La clase A no posee class
     * @author Tomas Rando
     */
    @Test
    public void testClassNotDefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/classNotDefined.s").program();
        });
    }

    /**
     * Incorrecto. La clase A no posee impl
     * @author Tomas Rando
     */
    @Test
    public void testImplNotDefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/implNotDefined.s").program();
        });
    }

    /**
     * Incorrecto. Existen dos definiciones del metodo getC()
     * @author Tomas Rando
     */
    @Test
    public void testSameNameMethodsIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/sameNameMethods.s").program();
        });
    }

    /**
     * Incorrecto. Existen dos definiciones del metodo getC()
     * @author Tomas Rando
     */
    @Test
    public void testSameNameMethods2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/sameNameMethods2.s").program();
        });
    }

    /**
     * Incorrecto. getB2(Int x) de A posee una variable con el mismo nombre que el parametro que recibe
     * @author Tomas Rando
     */
    @Test
    public void testVariableSameNameAsParameterIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/variableSameNameAsParameter.s").program();
        });
    }

    /**
     * Incorrecto. getB2(Int x, B y, C c) recibe un parámetro de un tipo que no existe
     * @author Tomas Rando
     */
    @Test
    public void testParameterUndefinedTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/parameterUndefinedType.s").program();
        });
    }

    /**
     * Incorrecto. El metodo getX() de A posee dos declaraciones de variable con el mismo nombre
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedVariableIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedVariable.s").program();
        });
    }

    /**
     * Incorrecto. La clase A posee dos declaraciones de atributo con el mismo nombre
     * @author Tomas Rando
     */
    @Test
    public void testRedefinedAttributeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/redefinedAttribute.s").program();
        });
    }

    /**
     * Incorrecto. B hereda x de A y lo intenta redefinir
     * @author Tomas Rando
     */
    @Test
    public void testInheritedAttributeRedefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/inheritedAttributeRedefined.s").program();
        });
    }

    /**
     * Incorrecto. El tipo de retorno de getX() (C), no esta definido
     * @author Tomas Rando
     */
    @Test
    public void testReturnTypeNotDefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/returnTypeNotDefined.s").program();
        });
    }

    /**
     * Incorrecto. El tipo de la variable c (C), no esta definido
     * @author Tomas Rando
     */
    @Test
    public void testVariableTypeNotDefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/variableTypeNotDefined.s").program();
        });
    }

    /**
     * Incorrecto. El tipo del atributo c (C), no esta definido
     * @author Tomas Rando
     */
    @Test
    public void testAttributeTypeNotDefinedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoDeclaraciones/failing/attributeTypeNotDefined.s").program();
        });
    }
}
