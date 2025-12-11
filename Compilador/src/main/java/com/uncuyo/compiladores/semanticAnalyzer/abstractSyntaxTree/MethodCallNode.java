package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Class;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Parameter;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.beans.Expression;
import java.util.List;
import java.util.Map;

/**
 * Metodo que representa una llamada a un metodo
 * Extiende {@link OperandNode}
 */
public class MethodCallNode extends OperandNode {

    /**
     * Nombre de la clase del metodo. Si es estatico contendra el nombre de la clase al ser llamado, es decir,
     * si hubiese sido llamado A.a();, contendria A. En otro caso, contendra el nombre de la clase que
     * posee al metodo declarado
     */
    private String className;

    /**
     * Nombre de la clase que posee al metodo en el cual se hace la llamada. En el caso de un metodo no estatico
     * coincidira con className, sin embargo, sera diferente en el caso de ser estatico
     */
    private String methodOwnerClassName;

    /**
     * Nombre del metodo que contiene la llamada. Sirve para verificar que no se llame a un metodo no estatico
     * desde un contexto estatico
     */
    private String callerMethod;

    /**
     * Boolean que indica si es estatico o no
     */
    private boolean isStatic;

    /**
     * Token que representa el id del metodo
     */
    private Token token;

    /**
     * Lista que representa los parametros del metodo
     */
    private List<ExpressionNode> parameterList;

    /**
     * ChainNode que representa los encadenamientos
     */
    private ChainedNode chainedNode;

    /**
     * Constructor de la clase
     * @param token Token
     * @param isStatic boolean
     * @param className String con el nombre de la clase
     * @param callerMethod String con el nombre del metodo que contiene el llamado
     */
    public MethodCallNode(String className, String methodOwnerClassName, String callerMethod, Token token, boolean isStatic) {
        this.className = className;
        this.isStatic = isStatic;
        this.token = token;
        this.callerMethod = callerMethod;
        this.methodOwnerClassName = methodOwnerClassName;
    }

    public Token getToken() {
        return token;
    }

    /**
     * Generacion de codigo para la llamada a un metodo (estatico y dinamico)
     * @param string StringBuilder
     */
    @Override
    public void codeGen(StringBuilder string) {
        string.append("#Llamada a método \n");
        string.append("#Guardamos el framepointer actual en la pila \n");
        string.append("sw $fp, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        int memory = 8;
        Class class1 = SymbolTable.getClass(className);
        int methodOffset;
        if (!isStatic) {
            int selfOffset = class1.getMethods().get(token.getLexeme()).getParameterMemory();
            methodOffset = class1.getMethodOffset(token.getLexeme());
            string.append("#Cargamos en a0 el self \n");
            string.append("lw $a0 ").append(selfOffset).append("($fp) \n");
            string.append("beq $a0, $zero, variableNotInitialized \n");
            string.append("sw $a0 0($sp) \n");
            string.append("addiu $sp $sp -4 \n");
            memory = codeGenParameters(string, memory);
            string.append("#Copiamos el self en a0 \n");
            string.append("lw $a0, ").append(memory - 4).append("($sp) \n");
            string.append("#Cargamos la direccion de la vtable de self en a0 \n");
            string.append("lw $a0, 0($a0) \n");
        }
        else {
            String vtableName  = "vtable" + className;
            methodOffset = SymbolTable.getClass(className).getMethodOffset(token.getLexeme());
            string.append("#Se deja espacio para el self \n");
            string.append("#En este caso no existe, pero para coherencia \n");
            string.append("addiu $sp $sp -4");
            memory = codeGenParameters(string, memory);
            string.append("la $a0, ").append(vtableName).append("\n");
        }

        string.append("#Buscamos la direccion del metodo (usando el offset) \n");
        string.append("addiu $a0, $a0, ").append(methodOffset).append("\n");
        string.append("#Cargamos la direccion del metodo en el a0\n");
        string.append("lw $a0, 0($a0)\n");
        string.append("jalr $a0 \n");
        //string.append("jal ").append(token.getLexeme()).append(className).append("\n");
        string.append("addi $sp $sp ").append(memory).append("\n");
        string.append("lw $fp, 0($sp) \n");

        if (chainedNode != null) {
            chainedNode.codeGen(string);
        }

    }

    /**
     * Carga los parametros del metodo actual en la pila
     * @param string StringBuilder
     * @param memory int con el espacio en la pila utilizado hasta el momento
     * @return int con la memoria utilizada en la pila
     */
    public int codeGenParameters(StringBuilder string, int memory) {
        string.append("#Cargamos los parámetros a la pila \n");
        for (ExpressionNode expressionNode : parameterList.reversed()) {
            expressionNode.codeGen(string);
            checkChained(string, expressionNode);
            if (expressionNode instanceof ArrayAccessNode) {
                string.append("#Se obtiene el valor del array desde la direccion \n");
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0, 0($a0) \n");
                }
                else {
                    string.append("lw $a0, 0($a0) \n");
                }
            }

            if (expressionNode.nodeType.getName().equals("Double")) {
                string.append("s.d $f0, 0($sp) \n");
                string.append("addiu $sp $sp -8 \n");
                memory += 8;
            } else {
                string.append("sw $a0, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
                memory += 4;
            }
        }
        return memory;
    }


    /**
     * Este metodo se fija si el ultimo encadenado es un ChainedAccessNode
     * para asi cargar su valor (desde la direccion que retorna).
     * @param string StringBuilder
     * @param expressionNode ExpressionNode
     */
    public void checkChained(StringBuilder string, ExpressionNode expressionNode) {
        ChainedNode chainedNode1 = expressionNode.getLastChainedNode();

        if ((!(chainedNode1 instanceof ChainedArrayAccessNode) && chainedNode1 instanceof ChainedAccessNode)) {
            if (!isClassOrArray(expressionNode.nodeType.getName())) {
                if (expressionNode.nodeType.getName().equals("Double")) {
                    string.append("l.d $f0 0($a0)");
                }
                else {
                    string.append("lw $a0 0($a0)");
                }
            }
        }
    }

    /**
     * Devuelve false si es un tipo primitivo y true en caso contrario
     * @param type
     * @return boolean
     */
    public boolean isClassOrArray(String type) {
        if (type.equals("Int") ||
                type.equals("void") ||
                type.equals("Bool") ||
                type.equals("Str") ||
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Obtenemos el ultimo ChainedNode de un encadenado
     * @return ChainedNode
     */
    public ChainedNode getLastChainedNode() {
        ChainedNode chainedNode1;
        if (chainedNode != null) {
            chainedNode1 = chainedNode.getLastChainedNode();
        }
        else {
            chainedNode1 = null;
        }
        return chainedNode1;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }

    public ChainedNode getChainNode() {
        return chainedNode;
    }

    public void setChainNode(ChainedNode chainNode) {
        this.chainedNode = chainNode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodOwnerClassName() {
        return methodOwnerClassName;
    }

    public void setMethodOwnerClassName(String methodOwnerClassName) {
        this.methodOwnerClassName = methodOwnerClassName;
    }

    public String getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(String callerMethod) {
        this.callerMethod = callerMethod;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Metodo para chequear los tipos. Verifica si es estatico, si esta declarado correctamente, si tiene
     * encadenado llama a hacer el chequeo y verifica los parametros
     * @return Type Devuelve el tipo de retorno del metodo o el tipo que viene desde el encadenado
     */
    @Override
    public Type check() throws SemanticASTException {
        Type type;
        Method method;
        if (callerMethod.equals("start") && !isStatic) {
            throw new SemanticASTException(token, "El método de instancia " +
                    token.getLexeme() + " está siendo llamado " +
                    "desde start sin utilizar instancia.");
        }
        method = SymbolTable.getClass(className).getMethods().get(token.getLexeme());
        Method callerMethod1;

        if (callerMethod == null) {
            callerMethod1 = SymbolTable.getClass(methodOwnerClassName).getConstructor();
        }
        else {
            callerMethod1 = SymbolTable.getClass(className).getMethods().get(callerMethod);
        }

        if (method == null) {
            throw new SemanticASTException(token, "El " +
                    "método " + token.getLexeme() +
                    " invocado no se " +
                    "encuentra declarado en " +
                    "el ámbito actual");
        }
        if (isStatic && !method.isStaticMethod()) {
            throw new SemanticASTException(token, "El método " +
                    token.getLexeme() +
                    " invocado no es estático");
        }
        checkParameters(token, parameterList, method, className);
        if (chainedNode != null) {
            type = chainedNode.checkNames(method.getType());
        }
        else {
            if (!isStatic && callerMethod1.isStaticMethod()) {
                throw new SemanticASTException(token, "Se realiza una llamada a un método de " +
                        "instancia desde un contexto estático");
            }
            type = method.getType();
        }
        type.setToken(token);
        this.nodeType = type;
        return type;
    }

    /**
     *
     * @param token Token de la llamada al metodo
     * @param expressionList Lista de parametros
     * @param method Metodo analizado
     * @param class1 Nombre de la clase dueña del metodo
     * @throws SemanticASTException Excepcion si los parametros son incorrectos en tipo o en cantidad
     * @author Tomas Rando
     */
    public void checkParameters(Token token, List<ExpressionNode> expressionList, Method method, String class1) throws SemanticASTException {
        Map<String, Parameter> parameters = method.getParameters();
        if (parameters.size() != expressionList.size()) {
            throw new SemanticASTException(token, "El número de parámetros " +
                    "del método " + token.getLexeme() +
                    " no coincide con los brindados. Se esperaban " +
                    parameters.size() + " y se obtuvieron " +
                    expressionList.size());
        }

        int index = 0;
        for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
            Type parameterType = entry.getValue().getType();
            ExpressionNode expressionNode = expressionList.get(index);
            Type providedType;
            if (expressionNode instanceof ChainedNode) {
                providedType = ((ChainedNode) expressionNode).checkNames(null);
            }
            else {
                providedType = expressionNode.check();
            }

            if (parameterType.getName().equals(providedType.getName())) {
                if (parameterType.getName().equals("Array")) {
                    if (!parameterType.getArrType().getName().equals(providedType.getArrType().getName())) {
                        throw new SemanticASTException(providedType.getToken(), "Tipo " +
                                "de Array incorrecto en " +
                                "parámetros del método " +
                                method.getName() + ". Se obtuvo: " +
                                providedType.getArrType().getName() +
                                ". Se esperaba " +
                                parameterType.getArrType().getName());
                    }
                }
                index++;
            }
            else {
                //Se verifica el polimorfismo
                if (!(providedType.getName().equals("void")) && !(providedType.getName().equals("nil")) && SymbolTable.getClass(providedType.getName()).isInheritedClass(parameterType.getName())) {
                    index++;
                }
                else {
                    //En caso de que el parametro sea nil, se verifica que el tipo del mismo sea una clase o array
                    if (providedType.getName().equals("nil") && (isClassOrArray(parameterType.getName()))) {
                        index++;
                    }
                    else {
                        throw new SemanticASTException(providedType.getToken(), "Tipo incorrecto en " +
                                "parámetros de llamada a método " + method.getName() + ". Se obtuvo: " +
                                providedType.getName() + ". Se esperaba " +
                                parameterType.getName());
                    }
                }

            }
        }
    }
}
