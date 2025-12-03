package com.uncuyo.compiladores.semanticAnalyzer.ast;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.exceptions.SemanticException;
import com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree.AST;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.syntacticAnalyzer.SyntacticAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemanticASTTest {

    @BeforeEach
    public void setUp() {
        AST.resetAST();
    }
    /**
     * Incorrecto.
     */
    @Test
    public void testVariableTypeDoesNotExistIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/variableTypeDoesNotExist.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testVoidInChainingIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/voidInChaining.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testVoidInChaining2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/voidInChaining2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testVoidInChaining3Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/voidInChaining3.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testAccessingPrivateAttributeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/accessingPrivateAttribute.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testAccessingPrivateAttributeWithChainedConstructorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/accessingPrivateAttributeWithChainedConstructor.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testArrayIndexNotIntIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/arrayIndexNotInt.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testArrayIndexNotInt2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/arrayIndexNotInt2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testAssignmentWithVoidIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/assignmentWithVoid.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testBinaryExpressionIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpression.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testConstructorClassInNewDoesNotExistIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/constructorClassInNewDoesNotExist.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testConstructorParametersWrongQuantityIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/constructorParametersWrongQuantity.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testConstructorParametersWrongTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/constructorParametersWrongType.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectChainedArrayAccessIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/incorrectChainedArrayAccess.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectPrivateAccessIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/incorrectPrivateAccess.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectPrivateArrayAccessIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/incorrectPrivateArrayAccess.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectTypeInAssignmentIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/incorrectTypeInAssignment.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testInstanceAttributeInStaticContextIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/instanceAttributeInStaticContext.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testMethodWithWrongTypesIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/methodWithWrongTypes.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testMethodWithWrongTypesInsideClassIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/methodWithWrongTypesInsideClass.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testNonDeclaredVariableIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/nonDeclaredVariable.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testNonStaticMethodFromStaticMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/nonStaticMethodFromStaticMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testNotBooleanExpressionInIfIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/notBooleanExpressionInIf.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testNotBooleanExpressionInWhileIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/notBooleanExpressionInWhile.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRetInConstructorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/retInConstructor.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRetInStartMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/retInStartMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRetInVoidMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/retInVoidMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRetNotPresentInMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/retNotPresentInMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRetWithDifferentTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/retWithDifferentType.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testSelfAssignedIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/selfAssigned.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testSelfInStaticMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/selfInStaticMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStartAccessingInstanceAttributesIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/startAccessingInstanceAttributes.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStartAccessingInstanceMethodsIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/startAccessingInstanceMethods.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStartAccessingInstancePrivateAttributeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/startAccessingInstancePrivateAttribute.s").program();
        });
    }


    /**
     * Correcto.
     */
    @Test
    public void testStartAccessingInstanceAttributesCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/startAccessingInstanceAttributes.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStartAccessingInstanceMethodsCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/startAccessingInstanceMethods.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStaticMethodCallCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/staticMethodCall.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStaticMethodCall2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/staticMethodCall2.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStaticMethodFromAnInstanceCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/staticMethodFromAnInstance.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testVariableTypeExistsCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/variableTypeExists.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testAssignmentUsingSelfCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/assignmentUsingSelf.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testAttributeAssignedWithSelfCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/attributeAssignedWithSelf.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testBooleanExpressionInIfCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/booleanExpressionInIf.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testBooleanExpressionInWhileCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/booleanExpressionInWhile.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testChainedConstructorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/chainedConstructor.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testChainedSelfCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/chainedSelf.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testChainingWithoutVoidCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/chainingWithoutVoid.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testChainingWithoutVoid2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/chainingWithoutVoid2.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testChainingWithoutVoid3Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/chainingWithoutVoid3.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testCompleteTestCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/completeTest.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testCorrectAssignationCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/correctAssignation.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testCorrectChainedArrayAccessCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/correctChainedArrayAccess.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testCorrectConstructorWithParametersCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/correctConstructorWithParameters.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testDeclaredAttributeCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/declaredAttribute.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testDeclaredParameterCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/declaredParameter.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testDeclaredVariableCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/declaredVariable.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testMethodCalledWithSelfCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/methodCalledWithSelf.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testMethodWithRightTypeCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/methodWithRightType.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testMethodWithRightType2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/methodWithRightType2.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testNilInAssignmentCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/nilInAssignment.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testNilInBooleanOperationCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/nilInBooleanOperation.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testRetWithNilCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/retWithNil.s").program();
        });
    }

}
