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

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test29Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test29.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test30Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test30.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test31Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test31.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test32Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test32.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test33Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test33.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void test34Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test34.s").program();
        });
    }

    /**
     * Incorrecto. En el while hay dos () antes de la {
     * @author Tomas Rando
     */
    @Test
    public void test35Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test35.s").program();
        });
    }

    /**
     * Incorrecto. new no tiene la lista de argumentos entre paréntesis
     * @author Tomas Rando
     */
    @Test
    public void test36Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test36.s").program();
        });
    }

    /**
     * Incorrecto. Falta la expresión del operador de incrementar en la linea 4
     * @author Tomas Rando
     */
    @Test
    public void test37Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test37.s").program();
        });
    }

    /**
     * Incorrecto. El constructor requiere ()
     * @author Tomas Rando
     */
    @Test
    public void test38Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test38.s").program();
        });
    }

    /**
     * Incorrecto. Faltan () en el if
     * @author Tomas Rando
     */
    @Test
    public void test39Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test39.s").program();
        });
    }

    /**
     * Incorrecto. Falta ] en la línea 5.
     * @author Tomas Rando
     */
    @Test
    public void test40Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test40.s").program();
        });
    }

    /**
     * Correcto. Se testea el array
     * @author Tomas Rando
     */
    @Test
    public void test41Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test41.s").program();
        });
    }

    /**
     * Correcto. Se testea el encadenamiento largo con el acceso a un atributo al final
     * @author Tomas Rando
     */
    @Test
    public void test42Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test42.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test43Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test43.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test44Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test44.s").program();
        });
    }

    /**
     * Incorrecto. Falta cerrar el start
     * @author Tomas Rando
     */
    @Test
    public void test45Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test45.s").program();
        });
    }

    /**
     * Correcto. Se testea la herencia
     * @author Tomas Rando
     */
    @Test
    public void test46Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test46.s").program();
        });
    }

    /**
     * Incorrecto. Falta la clase de la cual hereda
     * @author Tomas Rando
     */
    @Test
    public void test47Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test47.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test48Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test48.s").program();
        });
    }

    /**
     * Incorrecto. El start debe ir después de las definiciones de clases/impl
     * @author Tomas Rando
     */
    @Test
    public void test49Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test49.s").program();
        });
    }

    /**
     * Correcto
     * @author Tomas Rando
     */
    @Test
    public void test50Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test50.s").program();
        });
    }

    /**
     * Incorrecto. Clase declarada despues de start.
     * @author Tomas Rando
     */
    @Test
    public void testClassAfterStartIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ClassAfterStart.s").program();
        });
    }

    /**
     * Incorrecto. Clase dentro de otra clase.
     * @author Tomas Rando
     */
    @Test
    public void testClassInsideClassIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ClassInsideClass.s").program();
        });
    }

    /**
     * Incorrecto. Constructor dentro de start.
     * @author Tomas Rando
     */
    @Test
    public void testConstructorInsideStartIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ConstructorInsideStart.s").program();
        });
    }

    /**
     * Incorrecto. Constructor fuera de clase.
     * @author Tomas Rando
     */
    @Test
    public void testConstructorOutsideOfClassIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ConstructorOutsideOfClass.s").program();
        });
    }

    /**
     * Incorrecto. Archivo vacio.
     * @author Tomas Rando
     */
    @Test
    public void testEmptyFileIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/EmptyFile.s").program();
        });
    }

    /**
     * Incorrecto. Variable global fuera de clase.
     * @author Tomas Rando
     */
    @Test
    public void testGlobalVariableIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/GlobalVariable.s").program();
        });
    }

    /**
     * Incorrecto. Metodo dentro de metodo.
     * @author Tomas Rando
     */
    @Test
    public void testMethodInsideMethodIncorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/MethodInsideMethod.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testArithmeticExpressionsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ArithmeticExpresions.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testBasicDeclarationsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/BasicDeclarations.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testBlockTestCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/BlockTest.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testClassDeclarationsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/ClassDeclarations.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testCompoundSentencesCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/CompoundSentences.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testFibonacciExampleCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/FibonacciExample.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testFunctionsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/Functions.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testLogicOperatorsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/LogicOperators.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testRelationalOperatorsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/RelationalOperators.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testSimpleStartBlockCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/SimpleStartBlock.s").program();
        });
    }

    /**
     * Correcto.
     * @author Tomas Rando
     */
    @Test
    public void testStartBlockDeclarationsCorrect() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/testsExternos/sintactico/StartBlockDeclarations.s").program();
        });
    }

    /**
     * Correcto. Varias clases e impl antes de start.
     * @author Paulina
     */
    @Test
    public void test51Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test51.s").program();
        });
    }

    /**
     * Correcto. Herencia con atributo array usado en start.
     * @author Paulina
     */
    @Test
    public void test52Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test52.s").program();
        });
    }

    /**
     * Correcto. Cast y creación de array con expresión.
     * @author Paulina
     */
    @Test
    public void test53Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test53.s").program();
        });
    }

    /**
     * Correcto. Uso de self con encadenado y arreglo.
     * @author Paulina
     */
    @Test
    public void test54Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test54.s").program();
        });
    }

    /**
     * Correcto. Impl con métodos estáticos y start vacío.
     * @author Paulina
     */
    @Test
    public void test55Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test55.s").program();
        });
    }

    /**
     * Incorrecto. El constructor de clase requiere () luego de new.
     * @author Paulina
     */
    @Test
    public void test56Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test56.s").program();
        });
    }

    /**
     * Incorrecto. start no admite parámetros.
     * @author Paulina
     */
    @Test
    public void test57Incorrect() {
        assertThrows(SyntacticException.class, () -> {
            new SyntacticAnalyzer("tests/sintactico/test57.s").program();
        });
    }

    /**
     * Correcto. If/else anidado sin llaves.
     * @author Paulina
     */
    @Test
    public void test58Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test58.s").program();
        });
    }

    /**
     * Correcto. Return con expresión compleja y encadenado.
     * @author Paulina
     */
    @Test
    public void test59Correct() {
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/sintactico/test59.s").program();
        });
    }


}
