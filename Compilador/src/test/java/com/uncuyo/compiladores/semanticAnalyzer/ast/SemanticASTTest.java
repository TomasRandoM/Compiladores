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

    /**
     * Correcto.
     */
    @Test
    public void testArrayAccessMulCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/arrayAccessMul.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testArraysComparationCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/arraysComparation.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testArraysComparation2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/arraysComparation2.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testBoolComparationCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/boolComparation.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testDivOperatorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/divOperator.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testPDivOperatorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/pdivOperator.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testRelationalOpCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/binaryExpressions/relationalOp.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testAndOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/andOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testAndOperator2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/andOperator2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testEqComparationIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/eqComparation.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testEqComparation2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/eqComparation2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectArrayAccessIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/incorrectArrayAccess.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectDivTypeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/incorrectDivType.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectPdivOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/incorrectPdivOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectSumWithStrIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/incorrectSumWithStr.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectTypeInSumIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/incorrectTypeInSum.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testModOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/modOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testModOperator2Incorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/modOperator2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testRelationalOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/relationalOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testSumArrayAndIntIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/binaryExpressions/SumArrayAndInt.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testArrayCastIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/arrayCast.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testArrayNotOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/arrayNotOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testArraySubIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/arraySub.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testBoolCastIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/boolCast.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testBoolIncrementIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/boolIncrement.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testBoolSubIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/boolSub.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testNotOperatorIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/notOperator.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStringDecrementIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/stringDecrement.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStringSumIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/unaryExpressions/stringSum.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testArrayIncrementCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/arrayIncrement.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testCastOperatorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/castOperator.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testIncrementCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/increment.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testIncrementAndDecrementCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/incrementAndDecrement.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testUnaryNotOperatorCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/notOperator.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testSubAndSumCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/subAndSum.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testSubAndSum2Correct() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/unaryExpressions/subAndSum2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIncorrectPolymorphism() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/incorrectPolymorphism.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testPolymorphismInReturn() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/polymorphismInReturn.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testPolymorphismParameters1() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/polymorphismParameters1.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testPolymorphismParameters2() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/polymorphismParameters2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testPolymorphismParameters3() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/polymorphismParameters3.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testArrayCallMethodTestCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/arrayCallMethodTest.s").program();
        });
    }


    /**
     * Correcto.
     */
    @Test
    public void testCorrectPolymorphismCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/correctPolymorphism.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testPolymorphismCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/polymorphism.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testPolymorphismInParametersCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/polymorphismInParameters.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testPolymorphismMethodCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/polymorphismMethod.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testRetInMethodCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/retInMethod.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testPolymorphismMethodIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/polymorphismMethod.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStrArrayWithChainedNodeCorrect() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/strArrayWithChainedNode.s").program();
        });
    }

    /**
     * Correcto.
     */
    @Test
    public void testStrArrayWithChainedNodeCorrect2() {
        SymbolTable.resetSymbolTable();
        assertDoesNotThrow(() -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/passing/strArrayWithChainedNode2.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testIntArrayWithChainedNodeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/intArrayWithChainedNode.s").program();
        });
    }
    /**
     * Incorrecto.
     */
    @Test
    public void testStrArrayWithChainedNodeIncorrect() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/strArrayWithChainedNodeIncorrect.s").program();
        });
    }

    /**
     * Incorrecto.
     */
    @Test
    public void testStrArrayWithChainedNodeIncorrect2() {
        SymbolTable.resetSymbolTable();
        assertThrows(SemanticASTException.class, () -> {
            new SyntacticAnalyzer("tests/semanticoSentencias/failing/strArrayWithChainedNodeIncorrect2.s").program();
        });
    }

}
