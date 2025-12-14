# Clases del paquete AbstractSyntaxTree que utilizan doubles en codeGen()

Este documento lista todas las clases del paquete `abstractSyntaxTree` que utilizan instrucciones relacionadas con doubles en sus métodos `codeGen()`, incluyendo instrucciones como `s.d`, `l.d`, registros como `$f0`-`$f12`, y operaciones de punto flotante.

## Lista de Clases

### 1. ArrayAccessNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Utilizado para obtener valores de arrays de tipo Double.

---

### 2. AssignmentNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria
- `s.d $f0, 0($sp)` - Almacenamiento de double en la pila
- `s.d $f0, 0($a0)` - Almacenamiento de double en memoria

**Contexto:** Manejo de asignaciones de valores Double.

---

### 3. BinaryExpressionNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double
- `s.d $f0, 0($sp)` - Almacenamiento de double en pila
- `l.d $f2, 8($sp)` - Carga de double desde pila
- `l.d $f8, zeroDouble` - Carga de constante double (0.0)
- `mtc1 $a0, $f0` - Mover de registro entero a registro de punto flotante
- `mtc1 $a0, $f2` - Mover de registro entero a registro de punto flotante
- `cvt.d.w $f0, $f0` - Conversión de entero a double
- `cvt.d.w $f2, $f2` - Conversión de entero a double
- `add.d $f0, $f0, $f2` - Suma de doubles
- `sub.d $f0, $f2, $f0` - Resta de doubles
- `mul.d $f0, $f2, $f0` - Multiplicación de doubles
- `div.d $f0, $f2, $f0` - División de doubles
- `c.eq.d $f0, $f8` - Comparación de doubles (igualdad)
- `cvt.w.d $f6, $f4` - Conversión de double a entero

**Contexto:** Operaciones aritméticas y relacionales con doubles. Incluye conversiones entre tipos y operaciones del módulo para doubles.

---

### 4. BlockNode.java
**Instrucciones utilizadas:**
- `l.d $f0, zeroDouble` - Carga de constante double (0.0)
- `s.d $f0, 0($sp)` - Almacenamiento de double en pila
- `s.d $f0, offset($v0)` - Almacenamiento de double en offset de objeto

**Contexto:** Inicialización de variables y atributos de tipo Double.

---

### 5. ChainedArrayAccessNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Acceso encadenado a arrays de tipo Double.

---

### 6. ChainedCallNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria
- `s.d $f0, 0($sp)` - Almacenamiento de double en pila

**Contexto:** Llamadas a métodos encadenadas con parámetros de tipo Double.

---

### 7. IfThenElseNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Evaluación de condiciones que involucran valores Double.

---

### 8. LiteralNode.java
**Instrucciones utilizadas:**
- `l.d $f0, nombre_constante` - Carga de literal double

**Contexto:** Manejo de constantes literales de tipo Double en el código.

---

### 9. MethodCallNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria
- `s.d $f0, 0($sp)` - Almacenamiento de double en pila

**Contexto:** Paso de parámetros Double a métodos y manejo de valores de retorno.

---

### 10. NewNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria
- `l.d $f0, zeroDouble` - Carga de constante double (0.0)
- `s.d $f0, 0($sp)` - Almacenamiento de double en pila (usado en múltiples contextos: para constructor de arrays y manejo de expresiones)

**Contexto:** Creación de objetos y arrays, incluyendo inicialización de arrays de Double.

---

### 11. ParenthesizedExpressionNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Manejo de expresiones entre paréntesis con valores Double.

---

### 12. ReturnNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Retorno de valores Double desde métodos.

---

### 13. UnaryExpressionNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria
- `l.d $f2, addOne` - Carga de constante double (1.0)
- `l.d $f2, zeroDouble` - Carga de constante double (0.0)
- `add.d $f0, $f0, $f2` - Suma de doubles (incremento)
- `sub.d $f0, $f0, $f2` - Resta de doubles (decremento)
- `sub.d $f0, $f2, $f0` - Resta de doubles (negación)
- `s.d $f0, 0($a3)` - Almacenamiento de double en memoria
- `cvt.w.d $f2, $f0` - Conversión de double a entero (casting)
- `mfc1 $a0, $f2` - Mover de registro de punto flotante a registro entero

**Contexto:** Operaciones unarias con doubles: incremento, decremento, negación y casting a entero.

---

### 14. WhileNode.java
**Instrucciones utilizadas:**
- `l.d $f0, 0($a0)` - Carga de double desde memoria

**Contexto:** Evaluación de condiciones en bucles while con valores Double.

---

## Resumen

**Total de clases:** 14

**Instrucciones MIPS más comunes:**
- `l.d` - Load double (carga de double desde memoria)
- `s.d` - Store double (almacenamiento de double en memoria)
- `$f0, $f2, $f4, $f6, $f8` - Registros de punto flotante utilizados
- `add.d`, `sub.d`, `mul.d`, `div.d` - Operaciones aritméticas de doubles
- `cvt.d.w` - Conversión de entero a double
- `cvt.w.d` - Conversión de double a entero
- `mtc1`, `mfc1` - Movimiento entre registros enteros y de punto flotante
- `c.eq.d` - Comparación de doubles

**Clases con mayor uso de instrucciones de doubles:**
1. **BinaryExpressionNode.java** - La más completa, incluye todas las operaciones aritméticas y conversiones
2. **UnaryExpressionNode.java** - Operaciones unarias completas incluyendo casting
3. **BlockNode.java** - Inicialización de variables y atributos

Todas estas clases pertenecen al paquete:
`com.uncuyo.compiladores.semanticAnalyzer.abstractSyntaxTree`
