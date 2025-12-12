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

    /**
     * Realiza la generacion de codigo para los bloques del codigo.
     * Basicamente guarda el framepointer nuevo, el return address y llama a los codeGen de sentencias.
     * En el caso del start tambien finaliza el programa
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        int memory = 0;
        if (methodBlock) {
            checkClassVTable(string);
            if (methodName != null) {
                if (!methodName.equals("start")) {
                    string.append(".text \n");
                    string.append(methodName).append(className).append(": \n");
                    string.append("#Se forma el nuevo framepointer \n");
                    string.append("move $fp, $sp \n");
                    string.append("#Se guarda el return address en la pila \n");
                    string.append("sw $ra, 0($sp) \n");
                    string.append("addiu $sp $sp -4 \n");
                    Map<String, Variable> variables = SymbolTable.getClass(className).getMethods().get(methodName).getVariables();
                    //Declaraciones
                    memory = calcultateMemoryV(variables, string);
                    string.append("#Sentencias del bloque \n");
                    for (SentenceNode sentenceNode : sentences) {
                        sentenceNode.codeGen(string);
                    }
                    string.append("addiu $sp $sp ").append(memory + 4).append("\n");
                    string.append("lw $ra, 0($sp) \n");
                    string.append("jr $ra \n");
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
                    //Declaraciones
                    memory = calcultateMemoryV(SymbolTable.getStartMethodStored().getVariables(), string);
                    string.append("#Sentencias del bloque \n");
                    for (SentenceNode sentenceNode : sentences) {
                        sentenceNode.codeGen(string);
                    }
                    string.append("addiu $sp $sp ").append(memory + 4).append("\n");
                    //Fin del programa
                    string.append("#Fin del programa \n");
                    string.append("li $v0, 10 \n");
                    string.append("syscall \n");
                }
            }
            else {
                string.append(".text \n");
                string.append("constructor").append(className).append(": \n");
                string.append("#Se forma el nuevo framepointer \n");
                string.append("move $fp, $sp \n");
                string.append("#Se guarda el return address en la pila \n");
                string.append("sw $ra, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
                string.append("#Constructor \n");

                Map<String, Variable> variables = SymbolTable.getClass(className).getConstructor().getVariables();
                //Declaraciones
                memory = calcultateMemoryV(variables, string);
                string.append("#Sentencias del bloque \n");
                for (SentenceNode sentenceNode : sentences) {
                    sentenceNode.codeGen(string);
                }
                string.append("addiu $sp $sp ").append(memory + 4).append("\n");
                string.append("lw $ra, 0($sp) \n");
                string.append("jr $ra \n");
            }

        }
        else {
            //Bloque de sentencias DENTRO de un bloque de método
            string.append("#Sentencias del bloque de un metodo \n");
            for (SentenceNode sentenceNode : sentences) {
                sentenceNode.codeGen(string);
            }
        }
    }

    /**
     * Carga las Vtable 1 vez por clase
     * @param string StringBuilder
     */
    public void checkClassVTable(StringBuilder string) {
        if (className != null) {
            if (!AST.getVtablesMade().contains(className)) {
                string.append(".data \n");
                string.append("vtable").append(className).append(": \n");
                for (Method m : SymbolTable.getClass(className).getMethods().values()){
                    string.append(".word ").append(m.getName()).append(className).append("\n");
                }
                AST.addClassToVtablesMadeList(className);
            }
        }
    }

    /**
     * Declara las variables de un metodo, le reserva memoria en la pila y devuelve la memoria utilizada
     * @param variables Map de las variables
     * @param string StringBuilder
     * @return Int con la memoria utilizada
     */
    public int calcultateMemoryV(Map<String, Variable> variables, StringBuilder string) {
        int memory = 0;
        string.append("#Declaración de variables \n");
        string.append("#Reservamos memoria para las variables en la pila y lo inicializamos\n");
        for (Variable variable : variables.values()) {
            if (variable.getType().getName().equals("Double")) {
                string.append("l.d $f0, zeroDouble\n");
                string.append("s.d $f0, 0($sp)\n");
                string.append("addiu $sp $sp -8\n");
                memory += 8;
            }
            else {
                if (variable.getType().getName().equals("Str")) {
                    string.append("la $a0, stringInitialization \n");
                    string.append("sw $a0, 0($sp) \n");
                    string.append("addiu $sp $sp -4 \n");
                }
                else {
                    string.append("li $a0, 0 \n");
                    string.append("sw $a0, 0($sp) \n");
                    string.append("addiu $sp $sp -4 \n");
                }
                memory += 4;
            }
        }
        return memory;
    }


}
