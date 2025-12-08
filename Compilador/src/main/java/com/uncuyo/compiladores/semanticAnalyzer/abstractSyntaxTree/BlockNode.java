package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Nodo que representa un bloque
 * Extiende {@link SentenceNode}
 */
public class BlockNode extends SentenceNode {
    /**
     * Conjunto de sentencias del bloque
     */
    List<SentenceNode> sentences = new ArrayList<>();
    /**
     * Nombre del
     * metodo al que pertenece
     */
    private String methodName;
    /**
     * Nombre de la clase a la que pertenece
     */
    private String className;

    /**
     * Si es true se trata de un bloque de metodo, caso contrario, se trata de un
     * bloque de sentencias
     */
    private boolean methodBlock;

    public BlockNode(String className, String methodName, boolean methodBlock) {
        this.className = className;
        this.methodName = methodName;
        this.methodBlock = methodBlock;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isMethodBlock() {
        return methodBlock;
    }

    public void setMethodBlock(boolean methodBlock) {
        this.methodBlock = methodBlock;
    }

    public void addSentence(SentenceNode sentenceNode) {
        sentences.add(sentenceNode);
    }

    public List<SentenceNode> getSentences() {
        return sentences;
    }

    public void setSentences(List<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    public String getMethod() {
        return methodName;
    }

    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Metodo para chequear semanticamente
     */
    public void check() throws SemanticASTException {
        for (SentenceNode sentence : sentences) {
            sentence.check();
        }
        //Se verifica que el metodo posee un ret (si debe)
        if (methodName != null &&
                !methodName.equals("start") &&
                !SymbolTable.getClass(className).getMethods()
                    .get(methodName).getType().getName().equals("void") &&
                !AST.isReturnPresent() &&
                methodBlock
        ) {
            Method method = SymbolTable.getClass(className).getMethods().get(methodName);
            throw new SemanticASTException(method.getToken(), "El " +
                    "tipo de retorno del método " +
                    methodName + " no es " +
                    "void y el método no posee un ret");
        }
        //Reseteo el isReturnPresent del AST para el siguiente metodo
        if (methodBlock) {
            AST.setIsReturnPresent(false);
        }
    }

    @Override
    public void codeGen(StringBuilder string) {
        int memory = 0;
        if (methodBlock) {
            if (methodName != null) {
                if (!methodName.equals("start")) {
                    string.append("#Se forma el nuevo framepointer \n");
                    string.append("move $fp, $sp \n");
                    string.append("#Se guarda el return address en la pila \n");
                    string.append("sw $ra, 0($sp) \n");
                    string.append("addiu $sp $sp -4 \n");
                    Map<String, Variable> variables =SymbolTable.getClass(className).getMethods().get(methodName).getVariables();
                }
                else {
                    //Inicio del main
                    string.append(".text \n");
                    string.append("main: \n");
                    string.append("#Bloque start \n");
                    string.append("#Se forma el nuevo y primer framepointer \n");
                    string.append("move $fp, $sp \n");
                    string.append("#Movemos la pila para coherencia, pues no va a haber return address en el start \n");
                    string.append("addiu $sp $sp -4 \n");

                    //SENTENCIAS (PENDIENTE)

                    //Fin del programa
                    string.append("li $v0, 10 \n");
                    string.append("syscall \n");
                }
            }
            else {
                string.append("#Se forma el nuevo framepointer \n");
                string.append("move $fp, $sp \n");
                string.append("#Se guarda el return address en la pila \n");
                string.append("sw $ra, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
                string.append("#Constructor \n");
                string.append("#Se reserva memoria en la pila para las variables \n");
                string.append("addiu $sp $sp ").append(-memory).append(" \n");
            }

        }

    }

    public int calcultateMemoryV(Map<String, Variable> variables, StringBuilder string) {
        int memory = 0;
        string.append("#Se guarda el framepointer \n");
        string.append("sw $fp 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        for (Variable variable : variables.values()) {
            if (variable.getType().getName().equals("Double")) {
                string.append("");
                memory += 8;
            }
            else {
                memory += 4;
            }
        }
        string.append("lw $fp 4($sp) \n");
        string.append("addiu $sp $sp 4 \n");

        return memory;
    }
}
