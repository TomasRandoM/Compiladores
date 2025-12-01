package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Method;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Parameter;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

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

    /**
     * Metodo para chequear los tipos. Verifica si es estatico, si esta declarado correctamente, si tiene
     * encadenado llama a hacer el chequeo y verifica los parametros
     * @return Type Devuelve el tipo de retorno del metodo o el tipo que viene desde el encadenado
     */
    @Override
    public Type check() throws SemanticASTException {
        Type type;
        Method method = SymbolTable.getClass(className).getMethods().get(token.getLexeme());
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
            chainedNode.checkNames(method.getType());
            type = chainedNode.check();
        }
        else {
            if (!isStatic && SymbolTable.getClass(methodOwnerClassName)
                    .getMethods().get(callerMethod).isStaticMethod()) {
                throw new SemanticASTException(token, "Se realiza una llamada a un método de " +
                        "instancia desde un contexto estático");
            }
            type = method.getType();
        }
        type.setToken(token);
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
                    "del constructor no coincide con los brindados");
        }

        int index = 0;
        for (Map.Entry<String, Parameter> entry : parameters.entrySet()) {
            Type parameterType = entry.getValue().getType();
            Type providedType = expressionList.get(index).check();
            if (parameterType.getName().equals(providedType.getName())) {
                index++;
            }
            else {
                throw new SemanticASTException(entry.getValue().getToken(), "Tipo incorrecto en " +
                        "parámetros del método " + method.getName() + ". Se obtuvo: " +
                        providedType.getName() + ".Se esperaba " +
                        parameterType.getName());
            }
        }
    }
}
