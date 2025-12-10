package com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree;

import com.uncuyo.compiladores.exceptions.SemanticASTException;
import com.uncuyo.compiladores.lexicalAnalyzer.Token;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Parameter;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.SymbolTable;
import com.uncuyo.compiladores.semanticAnalyzer.symbolTable.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa un new (de clase y de array).
 * Extiende {@link OperandNode}
 */
public class NewNode extends OperandNode{
    /**
     * Token que representa el subtipo, si es una clase corresponde al token de una clase, si es un array corresponde
     * al token del tipo primitivo
     */
    private Token type;
    /**
     * String que representa que tipo de NewNode es. Puede ser "class" o "array"
     */
    private String option;
    /**
     * ChainedNode que representa un encadenamiento. Puede ser null
     */
    private ChainedNode chainedNode;
    /**
     * chainedNode representa la expresion dentro de un array
     */
    private ExpressionNode expressionNode;
    /**
     * Lista de parametros en caso de que sea un constructor de clase. Puede ser null
     */
    private List<ExpressionNode> parameterList = new ArrayList<>();

    /**
     * Constructor de NewNode
     * @param type Token
     * @param option String
     */
    public NewNode(Token type, String option) {
        if (option.equals("class")) {
            this.option = "class";
        }
        else {
            if (option.equals("array")) {
                this.option = "array";
            }
        }
        this.type = type;
    }

    /**
     * Setter de subtype
     * @param type
     */
    public void setType(Token type) {
        this.type = type;
    }

    /**
     * Chequea las semantica
     * @return
     */
    public Type check() throws SemanticASTException {
        Type newType;
        if (option.equals("class")) {
            //Resolución de nombres
            if (SymbolTable.getClass(type.getLexeme()) == null) {
                throw new SemanticASTException(type, "La clase " +
                        type.getLexeme() + " referenciada " +
                        "no se encuentra declarada");
            }
            //Constructor si posee porque lo chequeamos en la tabla de símbolos
            checkParameters(type, parameterList, type.getLexeme());
            if (chainedNode != null) {
                //Resolución de nombres (para los encadenados). Nos trae el último tipo de la serie de encadenamientos
                newType = chainedNode.checkNames(new Type(type, "class"));
            }
            else {
                newType = new Type(type, "class");
            }
        }
        else {
            if (chainedNode != null) {
                throw new SemanticASTException(chainedNode.getToken(), "Un array no puede tener un encadenamiento");
            }
            else {
                //Si pudiese haber arrays de tipo diferente aca debería haber resolución de nombres
                newType = new Type(type, "Array");
                newType.setArrType(new Type(type, type.getLexeme()));
            }
        }
        this.nodeType = newType;
        return newType;
    }

    public void checkParameters(Token token, List<ExpressionNode> expressionList, String class1) throws SemanticASTException {
        Map<String, Parameter> parameters = SymbolTable.getClass(class1).getConstructor().getParameters();

        if (parameters.size() != expressionList.size()) {
            throw new SemanticASTException(token, "El número de parámetros " +
                    "del constructor no coincide con los brindados");
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
                                "parámetros del constructor de la clase " +
                                class1 + ". Se obtuvo: " +
                                providedType.getArrType().getName() +
                                ". Se esperaba " +
                                parameterType.getArrType().getName());
                    }
                }
                index++;
            }
            else {
                if ((!providedType.getName().equals("void")) && SymbolTable.getClass(providedType.getName()).isInheritedClass(parameterType.getName())) {
                    index++;
                }
                else {
                    throw new SemanticASTException(providedType.getToken(), "Tipo incorrecto en " +
                            "parámetros del constructor de la " +
                            "clase " + class1 + ". Se obtuvo: " +
                            providedType.getName() + ".Se esperaba " +
                            parameterType.getName());
                }
            }
        }
    }

    //Getters y setters
    public Token getType() {
        return type;
    }

    public Token getToken() {
        return type;
    }

    @Override
    public void codeGen(StringBuilder string) {
        int memory = 8;
        string.append("#Llamada a constructor \n");
        string.append("#Guardamos el framepointer actual en la pila \n");
        string.append("sw $fp, 0($sp) \n");
        string.append("addiu $sp $sp -4 \n");
        string.append("#Reservamos lugar para el self en la pila \n");
        string.append("addiu $sp $sp -4 \n");

        if (!option.equals("array")) {
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
                    memory += 8;
                    string.append("s.d $f0, 0($sp) \n");
                    string.append("addiu $sp $sp -8 \n");
                }
                else {
                    memory += 4;
                    string.append("sw $a0, 0($sp) \n");
                    string.append("addiu $sp $sp -4 \n");
                }
            }
            //etiqueta del constructor es constructorClase
            string.append("jal constructor").append(type.getLexeme()).append("\n");
            string.append("addi $sp $sp ").append(memory).append("\n");
            string.append("#Restauramos el framepointer \n");
            string.append("lw $fp, 0($sp) \n");

            if (chainedNode != null) {
                chainedNode.codeGen(string);
            }
        }
        else {
            string.append("#Metemos a la pila el parametro que representa el espacio que ocupan los elementos \n");
            string.append("#del array. 8 si es Double, 4 si es otra cosa \n");
            if (type.getLexeme().equals("Double")) {
                string.append("li $a0, 8 \n");
                string.append("sw $a0, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
            }
            else {
                string.append("li $a0, 4 \n");
                string.append("sw $a0, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
            }
            expressionNode.codeGen(string);
            checkChained(string, expressionNode);
            if (expressionNode instanceof ArrayAccessNode) {
                string.append("lw $a0, 0($a0)");
            }
            string.append("#Guardamos en la pila el tamaño del array para pasarlo como parametro \n");
            string.append("sw $a0, 0($sp) \n");
            string.append("addiu $sp $sp -4 \n");
            if (type.getLexeme().equals("Double")) {
                string.append("#Guardo el 0.0 en la pila para usarlo de inicializador \n");
                string.append("l.d $f0, zeroDouble \n");
                string.append("sw $f0, 0($sp) \n");
                string.append("addiu $sp $sp -8 \n");
                string.append("jal constructorArrayDouble \n");
                string.append("#La direccion de memoria del array queda en a0 \n");
                string.append("addiu $sp $sp 24 \n");
            }
            else {
                if (type.getLexeme().equals("Str")) {
                    string.append("li $v0, 9\n");
                    string.append("li $a0, 8 \n");
                    string.append("syscall \n");
                    string.append("la $a0, stringInitialization \n");
                    string.append("sw $a0, 4($v0) \n");
                    string.append("la $a0, vtableStr \n");
                    string.append("sw $a0, 0($v0) \n");
                }
                else {
                    string.append("li $a0, 0 \n");
                }
                string.append("sw $a0, 0($sp) \n");
                string.append("addiu $sp $sp -4 \n");
                string.append("jal constructorArray \n");
                string.append("#La direccion de memoria del array queda en a0 \n");
                string.append("addiu $sp $sp 20 \n");
            }
            string.append("#Restauramos el framepointer \n");
            string.append("lw $fp, 0($sp) \n");
        }
    }

    public String getOption() {
        return option;
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
                type.equals("Char") ||
                type.equals("Double") ||
                type.equals("nil")) {
            return false;
        } else {
            return true;
        }
    }

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

    public void setOption(String option) {
        this.option = option;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public ExpressionNode getExpressionNode() {
        return chainedNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public List<ExpressionNode> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ExpressionNode> parameterList) {
        this.parameterList = parameterList;
    }
}
