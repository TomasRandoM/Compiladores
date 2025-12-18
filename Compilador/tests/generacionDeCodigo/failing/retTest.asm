.globl main 
.text 
main: 
#Bloque start 
#Se forma el nuevo y primer framepointer 
move $fp, $sp 
#Movemos la pila para coherencia, pues no va a haber return address en el start 
addiu $sp $sp -4 
#Declaración de variables 
#Reservamos memoria para las variables en la pila y lo inicializamos
li $a0, 0 
sw $a0, 0($sp) 
addiu $sp $sp -4 
#Sentencias del bloque 
#ASIGNACION 
#NEW NODE
#Llamada a constructor 
#Guardamos el framepointer actual en la pila 
sw $fp, 0($sp) 
addiu $sp $sp -4 
#Reservamos lugar para el self en la pila 
addiu $sp $sp -4 
#Cargamos los parámetros a la pila 
jal constructorA
addi $sp $sp 8
#Restauramos el framepointer 
lw $fp, 0($sp) 
sw $a0, 0($sp) 
addiu $sp $sp -4 
#VARIABLE NODE
#Carga de variable 
#Cargamos la direccion de la variable en a0 utilizando el 
#offset con el fp 
la $a0, -4($fp) 
addiu $sp $sp 4 
#Cargamos el valor del lado derecho 
lw $t0, 0($sp) 
#Se guarda lo del lado derecho en la direccion de a0 
sw $t0, 0($a0) 
#SIMPLE SENTENCE - CODE GEN DE EXPRESION
#METHOD CALL 
#Guardamos el framepointer actual en la pila 
sw $fp, 0($sp) 
addiu $sp $sp -4 
#Se deja espacio para el self 
#En este caso no existe, pero para coherencia 
addiu $sp $sp -4
#Cargamos los parámetros a la pila 
#CHAINED ACCESS NODE 
#Carga de variable 
#Cargamos la direccion de la variable o parametro en a0 utilizando el 
#offset con el fp 
addiu $a0 $fp -4
lw $a0 0($a0) 
#CHAINED CALL NODE 
sw $fp, 0($sp) 
addiu $sp $sp -4 
#Guardamos el self en la pila. Es el que venia del anterior encadenado 
beq $a0, $zero, variableNotInitialized 
sw $a0, 0($sp) 
addiu $sp $sp -4 
#Cargamos los parámetros a la pila 
#LITERAL
li $a0, 3
sw $a0, 0($sp) 
addiu $sp $sp -4 
#Cargamos el self en a0 
lw $a0, 8($sp) 
#Cargamos la direccion de la vtable de self en a0 
lw $a0, 0($a0) 
#Buscamos la direccion del metodo (usando el offset) 
addiu $a0, $a0, 0
#Cargamos la direccion del metodo en el a0
lw $a0, 0($a0)
#Saltamos al metodo y el retorno lo traemos en a0 
jalr $a0 
addiu $sp $sp 12
lw $fp, 0($sp) 
sw $a0, 0($sp) 
addiu $sp $sp -4 
la $a0, vtableIO
#Buscamos la direccion del metodo (usando el offset) 
addiu $a0, $a0, 4
#Cargamos la direccion del metodo en el a0
lw $a0, 0($a0)
jalr $a0 
addi $sp $sp 12
lw $fp, 0($sp) 
#FIN SIMPLE SENTENCE
addiu $sp $sp 8
#Fin del programa 
li $v0, 10 
syscall 
.data 
vtableA: 
.word absA
.text 
#Constructor 
constructorA: 
#Se forma el nuevo framepointer 
move $fp, $sp 
#Se guarda el return address en la pila 
sw $ra, 0($sp) 
addiu $sp $sp -4 
#Se deja espacio para los atributos 
li $a0, 0 
#A la memoria de los atributos se le suman 4 bytes para la vtable 
addiu $a0 $a0 4 
li $v0, 9 
syscall 
#Se carga la direccion de la vtable en a0 y se inserta en la primera posicion de la memoria 
la $a0, vtableA 
sw $a0, 0($v0) 
#Guardamos en el registro de activacion, en la direccion designada para self, la memoria 
sw $v0, 4($fp) 
#Llamada a inicializar los atributos 
#Declaración de atributos 
#Inicializamos los atributos 
#Declaración de variables 
#Reservamos memoria para las variables en la pila y lo inicializamos
#Sentencias del bloque 
#Guardamos el self en a0 para retornarlo 
lw $a0, 4($fp) 
addiu $sp $sp 4
lw $ra, 0($sp) 
jr $ra 
.text 
absA: 
#Se forma el nuevo framepointer 
move $fp, $sp 
#Se guarda el return address en la pila 
sw $ra, 0($sp) 
addiu $sp $sp -4 
#Declaración de variables 
#Reservamos memoria para las variables en la pila y lo inicializamos
li $a0, 0 
sw $a0, 0($sp) 
addiu $sp $sp -4 
#Sentencias del bloque 
#IF THEN ELSE:
#If 
if_absA88: 
#CODE GEN DE LA EXPRESION
#EXPRESION BINARIA
#CODE GEN DEL LEFT
#VARIABLE NODE
#Carga de variable 
#Cargamos la direccion de la variable en a0 utilizando el 
#offset con el fp 
la $a0, 4($fp) 
#EXP BINARIA CONTINUACION
#Se obtiene el valor del array desde la direccion 
lw $a0, 0($a0) 
sw $a0, 0($sp) 
addiu $sp $sp -4 
#CODE GEN DEL RIGHT
#LITERAL
li $a0, 0
#EXP BINARIA CONTINUACION
#Ni left ni right son double
#El lado izquierdo queda en el t0 y el lado derecho en el a0 
lw $t0, 4($sp) 
addiu $sp $sp 4
#Ningun tipo es double
slt $a0, $t0, $a0
#CONTINUA IF THEN ELSE
#Verifica si la condicion es falsa. Si es falsa salta a la etiqueta else 
beq $a0, $zero, elseif_absA88
#SENTENCIA DEL IF
#Sentencias del bloque de un metodo 
#RETURN
#CODE GEN DE LA EXPRESION
#UNARY EXPRESSION
#CODE GEN DE LA EXPRESION
#VARIABLE NODE
#Carga de variable 
#Cargamos la direccion de la variable en a0 utilizando el 
#offset con el fp 
la $a0, 4($fp) 
#Se obtiene el valor del array desde la direccion 
move $a3, $a0 
lw $a0, 0($a0) 
sub $a0, $zero, $a0 
#CONTINUA RETURN 
j endabsA
#Al terminar salta a la etiqueta end del if 
j endif_absA88 
#Etiqueta del else. Si no hay else, esta vacia 
elseif_absA88: 
#SENTENCIA DEL ELSE
#Sentencias del bloque de un metodo 
#ASIGNACION 
#LITERAL
li $a0, 8
sw $a0, 0($sp) 
addiu $sp $sp -4 
#VARIABLE NODE
#Carga de variable 
#Cargamos la direccion de la variable en a0 utilizando el 
#offset con el fp 
la $a0, -4($fp) 
addiu $sp $sp 4 
#Cargamos el valor del lado derecho 
lw $t0, 0($sp) 
#Se guarda lo del lado derecho en la direccion de a0 
sw $t0, 0($a0) 
endif_absA88: 
endabsA:
addiu $sp $sp 8
lw $ra, 0($sp) 
jr $ra 
.data
    addOne: .double 1.0
    zeroDouble: .double 0.0
    stringInitialization: .asciiz ""
    brackets1: .asciiz "["
    brackets2: .asciiz "]"
    comma: .asciiz ", "

    vtableStr:
        .word lengthStr
        .word concatStr

    vtableArray:
        .word lengthArray

    vtableIO:
        .word out_strIO
        .word out_intIO
        .word out_boolIO
        .word out_doubleIO
        .word out_array_intIO
        .word out_array_strIO
        .word out_array_boolIO
        .word out_array_doubleIO
        .word in_strIO
        .word in_intIO
        .word in_boolIO
        .word in_doubleIO

.text
    lessDouble:
        c.lt.d $f2, $f0
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    greaterDouble:
        c.lt.d $f0, $f2
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    equalDouble:
        c.eq.d $f2, $f0
        li $a0, 1
        bc1t endDouble
        li $a0, 0
        j endDouble

    notEqualDouble:
        c.eq.d $f2, $f0
        li $a0, 0
        bc1t endDouble
        li $a0, 1
        j endDouble

    greaterEqualDouble:  #!(f2<f0) f2>=f0
        c.lt.d $f2, $f0
        li $a0, 0
        bc1t endDouble
        li $a0, 1
        j endDouble


    lessEqualDouble: #f2 <= f0
        c.lt.d $f0, $f2
        li $a0, 0
        bc1t endDouble
        li $a0, 1
        j endDouble

    endDouble:
        jr $ra


    constructorArray:
        #constructorArray
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4
        #Cargo el size del array en t0
        lw $t0 8($fp)
        bltz $t0, negativeArraySizeException
        #Cargo la memoria que ocupan los elementos en t1
        lw $t1 12($fp)
        #Cargo en t2 el elemento que se usará para inicializar el array
        lw $t2 4($fp)
        #Memoria que ocuparan los elementos del array
        mul $a0, $t1, $t0
        #Sumo 8 bytes para la longitud del array y la vtable del mismo
        addiu $a0 $a0 8
        #Reservo la memoria para el array
        li $v0, 9
        syscall
        la $a0, vtableArray
        sw $a0, 0($v0)
        #Guardo la cantidad de elementos del array en el CIR del array
        sw $t0, 4($v0)
        #Muevo la direccion del CIR del array a a0
        move $a0, $v0
        #Apunto v0 donde va el primer elemento del array
        addiu $v0, $v0, 8
        #Cargo 1 para realizar restas
        li $t3, 1
        beq $t0, $zero, endConstructorArray
        forConstructorArray:
            #Coloco el elemento inicializador en 0($v0)
            sw $t2, 0($v0)
            add $v0, $t1, $v0
            #Resto 1 al size
            sub $t0, $t0, $t3
            #Comparo si el size es 0, si no, salto nuevamente al for
            bne $t0 $zero forConstructorArray
        endConstructorArray:
        #El resultado queda devuelto en a0
        #Restauramos el ra
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    constructorArrayDouble:
        #Metodo constructorArrayDouble
            move $fp, $sp
            sw $ra 0($sp)
            addiu $sp $sp -4
            #Cargo el size del array en t0
            lw $t0 12($fp)
            bltz $t0, negativeArraySizeException
            #Cargo la memoria que ocupan los elementos en t1

            lw $t1 16($fp)
            #Cargo en $t4 y $t5 las dos partes del double que se usará para inicializar el array
            lw $t4, 8($fp)
            lw $t5, 4($fp)

            #Memoria que ocuparan los elementos del array
            mul $a0, $t1, $t0
            #Sumo 8 bytes para la longitud del array y la vtable del mismo
            addiu $a0 $a0 8
            #Reservo la memoria para el array
            li $v0, 9
            syscall
            la $a0, vtableArray
            sw $a0, 0($v0)
            #Guardo la cantidad de elementos del array en el CIR del array
            sw $t0, 4($v0)
            #Muevo la direccion del CIR del array a a0
            move $a0, $v0
            #Apunto v0 donde va el primer elemento del array
            addiu $v0, $v0, 8
            #Cargo 1 para realizar restas
            li $t3, 1
            beq $t0, $zero, endConstructorDouble
            forConstructorDouble:
                #Coloco el elemento inicializador en 0($v0)
                sw $t4, 0($v0)
                sw $t5, 4($v0)
                add $v0, $t1, $v0
                #Resto 1 al size
                sub $t0, $t0, $t3
                #Comparo si el size es 0, si no, salto nuevamente al for
                bne $t0 $zero forConstructorDouble
            endConstructorDouble:
            #El resultado queda devuelto en a0
            #Restauramos el ra
            lw $ra 0($fp)
            addiu $sp $sp 4
            jr $ra


    constructorStr:
        #constructorStr
        #Cargo el framepointer
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4


        #Guardo lugar en la pila
        li $v0, 9
        li $a0, 8
        syscall

        #Cargo la vtable
        la $a0, vtableStr
        sw $a0, 0($v0)

        #Inicializo
        la $a0, stringInitialization

        #Guardo la direccion de la inicializacion
        sw $a0, 4($v0)

        #Muevo a a0
        move $a0, $v0

        #Restauro los valores y vuelvo al ra
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra

    out_strIO:
        #Metodo out_strIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargamos la direccion del CIR de la Str
        lw $a0, 4($fp)
        #Cargamos la direccion del Str en a0
        lw $a0, 4($a0)

        li $v0, 4
        syscall

        #Restauramos el ra y retornamos
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    out_boolIO:
        #Metodo out_boolIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargamos el valor
        lw $a0, 4($fp)

        li $v0, 1
        syscall

        #Restauramos el ra y retornamos
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra


    out_intIO:
        #Metodo out_intIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargamos el entero al a0
        lw $a0 4($fp)
        li $v0, 1
        syscall

        #Restauramos el ra y retornamos
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    out_doubleIO:
        #Metodo out_doubleIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargamos el double al f12
        lw $t0, 8($fp)
        lw $t1, 4($fp)
        mtc1 $t0, $f0
        mtc1 $t1, $f1
        mov.d $f12, $f0
        li $v0, 3
        syscall

        #Restauramos el ra y retornamos
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    out_array_doubleIO:
        #Metodo out_array_doubleIO
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4
        #Cargamos en a0 la direccion del array
        lw $a0, 4($fp)
        #Cargamos en t1 la cantidad de elementos del array
        lw $t1, 4($a0)
        #Cargamos en t0 la constante 1 para restar
        li $t0, 1
        #Movemos el puntero de a0 al primer elemento
        addiu $a0 $a0 8
        #Movemos el puntero a t2 para no sobreescribirlo
        move $t2 $a0
        la $a0, brackets1
        li $v0, 4
        syscall
        # i = 0
        li $t3, 0
        beq $t1, $zero, endOutArrayDouble

        forOutArrayDoubleIO:
                lw $t4, 0($t2)
                lw $t5, 4($t2)
                mtc1 $t4, $f0
                mtc1 $t5, $f1
                mov.d $f12, $f0

                li $v0, 3
                syscall

                # i++
                addiu $t3, $t3, 1
                addiu $t2, $t2, 8

                # si i = size salgo
                beq $t3, $t1, endOutArrayDouble

                # imprimir si no es el ultimo
                la $a0, comma
                li $v0, 4
                syscall

                j forOutArrayDoubleIO

        endOutArrayDouble:
            la $a0, brackets2
            li $v0, 4
            syscall

        #Restauramos el ra y retornamos
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra


    out_array_intIO:
    out_array_boolIO:
        #Metodo out_array_boolIO o out_array_intIO
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4
        #Cargamos en a0 la direccion del array
        lw $a0, 4($fp)
        #Cargamos en t1 la cantidad de elementos del array
        lw $t1, 4($a0)
        #Cargamos en t0 la constante 1 para restar
        li $t0, 1
        #Movemos el puntero de a0 al primer elemento
        addiu $a0 $a0 8
        #Movemos el puntero a t2 para no sobreescribirlo
        move $t2 $a0
        la $a0, brackets1
        li $v0, 4
        syscall
        # i = 0
        li $t3, 0
        beq $t1, $zero, endOutArrayIntOrBool

        forOutArrayIntOrBoolIO:
                lw $a0, 0($t2)
                li $v0, 1
                syscall

                # i++
                addiu $t3, $t3, 1
                addiu $t2, $t2, 4  # avanzar al siguiente elemento

                # si i = size salgo
                beq $t3, $t1, endOutArrayIntOrBool

                # imprimir si no es el ultimo
                la $a0, comma
                li $v0, 4
                syscall

                j forOutArrayIntOrBoolIO

        endOutArrayIntOrBool:
            la $a0, brackets2
            li $v0, 4
            syscall

        #Restauramos el ra y retornamos
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra


    out_array_strIO:
        #Metodo out_array_strIO
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargamos en a0 la direccion del array
        lw $a0, 4($fp)

        #Cargamos en t1 la cantidad de elementos del array
        lw $t1, 4($a0)
        #Cargamos en t3 el indice 0 para empezar a contar
        li $t3, 0
        #Movemos el puntero de a0 al primer elemento
        addiu $a0 $a0 8
        #Movemos el puntero a t2 para no sobreescribirlo
        move $t2 $a0
        la $a0, brackets1
        li $v0, 4
        syscall
        #Si el array esta vacio no se imprime nada
        beq $t1, $zero, endOutArrayStrIO
        forOutArrayStrIO:
            lw $a0, 0($t2)
            lw $a0 4($a0)
            li $v0, 4
            syscall

            # i++
            addiu $t3, $t3, 1
            addiu $t2, $t2, 4

            # si i = size salgo
            beq $t3, $t1, endOutArrayStrIO

            # imprimir si no es el ultimo
            la $a0, comma
            li $v0, 4
            syscall

            j forOutArrayStrIO

        endOutArrayStrIO:
            la $a0, brackets2
            li $v0, 4
            syscall

        #Restauramos el ra y retornamos
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra

    in_boolIO:
        #Metodo in_boolIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Leo la entrada
        li $v0, 5
        syscall

        #Paso la entrada a a0
        move $a0 $v0

        #Verifico si la entrada fue 1 o 0
        beq $a0, $zero, correctBoolInput
        li $t0, 1
        beq $a0, $t0, correctBoolInput
        j incorrectInBoolIOException

        correctBoolInput:
            #La retorno
            lw $ra, 0($fp)
            addiu $sp $sp 4
            jr $ra

    in_intIO:
        #Metodo in_intIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Leo la entrada
        li $v0, 5
        syscall

        #Paso la entrada a a0
        move $a0 $v0

        #La retorno
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra

    in_strIO:
        #Metodo in_strIO
        move $fp, $sp
        sw $ra, 0($sp)
        addiu $sp $sp -4

        # Reservo buffer para el string
        li $v0, 9
        li $a0, 256
        syscall

        # $v0 contiene el buffer
        move $t0, $v0

        # Leo la entrada
        li $v0, 8
        move $a0, $t0
        li $a1, 256
        syscall

        move $t1, $t0

        sacarSaltoLinea:
            lb $t2, 0($t1)
            beq $t2, $zero, endSalto
            beq $t2, 10, replace
            addiu $t1, $t1, 1
            j sacarSaltoLinea

        replace:
            sb $zero, 0($t1)

        endSalto:

        # Reservo espacio para Str
        li $v0, 9
        li $a0, 8
        syscall

        # Guardo la vtable
        la $a0, vtableStr
        sw $a0, 0($v0)

        # Guardo el puntero al buffer
        sw $t0, 4($v0)

        # Retorno el objeto
        move $a0, $v0

        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra


    in_doubleIO:
        #Metodo in_doubleIO
        move $fp, $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Leo la entrada que ya queda en $f0
        li $v0, 7
        syscall

        #La retorno
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra


    lengthArray:
        #Metodo lengthArray
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargo el array
        lw $a0, 4($fp)
        #Cargo la longitud del array en a0
        lw $a0, 4($a0)
        lw $ra, 0($fp)
        addiu $sp $sp 4
        jr $ra


    lengthStr:
        #Metodo lengthStr
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargo la cadena
        lw $a0, 4($fp)
        lw $a0, 4($a0)

        #Longitud
        li $t0, 0

        lengthStrLoop:
            #Load byte carga el primer byte de la cadena
            lb $t1, 0($a0)
            beq $t1 $zero, endLengthStr
            addiu $t0, $t0, 1
            addiu $a0, $a0, 1
            j lengthStrLoop

        endLengthStr:
            #Muevo la longitud a a0 para retornarla
            move $a0, $t0
            lw $ra 0($fp)
            addiu $sp $sp 4
            jr $ra


    concatStr:
        #Metodo concatStr
        move $fp, $sp
        sw $ra, 0($fp)
        addiu $sp $sp -4

        #Cargo la primera Str a t0
        lw $t0, 8($fp)
        #Cargo el framepointer a la pila
        sw $fp, 0($sp)
        addiu $sp $sp -4
        #Cargo la Str a la pila
        sw $t0, 0($sp)
        addiu $sp $sp -4
        jal lengthStr
        #Restauro el framepointer y en a0 se encuentra la longitud de la primer string
        addiu $sp $sp 8
        lw $fp, 0($sp)
        #Guardo el a0 en la pila para guardarlo
        sw $a0, 0($sp)
        addiu $sp $sp -4

        #Llamo al metodo nuevamente para calcular la longitud de la segunda Str
        #Guardo el fp en la pila
        sw $fp 0($sp)
        addiu $sp $sp -4
        #Guardo el self (el Str)
        lw $t0, 4($fp)
        sw $t0, 0($sp)
        addiu $sp $sp -4
        jal lengthStr
        #Restauro el fp y el resultado lo tengo en a0
        addiu $sp $sp 8
        lw $fp, 0($sp)
        addiu $sp $sp 4
        #Restauro la primer longitud
        lw $t1, 0($sp)

        #Calculo la longitud de la nueva string sumando ambas longitudes
        add $a0, $a0, $t1
        #Le sumo uno mas para el terminador
        addiu $a0 $a0 1
        #Reservo memoria en el heap
        li $v0, 9
        syscall

        sw $fp, 0($sp)
        addiu $sp $sp -4
        sw $v0, 0($sp)
        addiu $sp $sp -4
        #Cargo en a0 la direccion de la primera string
        lw $a0, 8($fp)
        lw $a0, 4($a0)
        #Cargo la Str en la pila
        sw $a0, 0($sp)
        addiu $sp $sp -4

        jal concatStrCopy
        addiu $a0 $a0 -1
        #En v0 esta la nueva string copiada
        #En a0 esta la direccion donde debo copiar la segunda string, es decir
        #en direccionDestino + length(Str1)
        addiu $sp $sp 12
        #Restauro fp
        lw $fp, 0($sp)

        #Cargo el fp en la pila
        sw $fp, 0($sp)
        addiu $sp $sp -4
        #Cargo la direccion de destino nueva (el final del anterior copy)
        sw $a0, 0($sp)
        addiu $sp $sp -4
        #Cargo en a0 la direccion de la segunda string
        lw $a0, 4($fp)
        lw $a0, 4($a0)
        #Cargo la Str en la pila
        sw $a0, 0($sp)
        addiu $sp $sp -4
        jal concatStrCopy

        #Restauro el framepointer y la pila
        addiu $sp $sp 12
        lw $fp 0($sp)
        #En v0 sigo teniendo la direccion de la nueva string
        #La guardo en t0
        move $t0, $v0
        #Reservo memoria para el objeto Str
        li $v0, 9
        li $a0, 8
        syscall
        la $a0, vtableStr
        sw $a0, 0($v0)
        sw $t0, 4($v0)

        #retorno en a0
        move $a0, $v0

        addiu $sp $sp 4
        lw $ra, 0($fp)
        jr $ra


    #Copia la string en la direccion pasada como parametro
    concatStrCopy:
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4

        #Cargo en a1 la string que sera copiada
        lw $a1, 4($fp)
        #Cargo en a0 donde sera copiada la string
        lw $a0, 8($fp)
        concatStrCopyLoop:
            lb $t0, 0($a1)
            sb $t0, 0($a0)
            addi $a0, $a0, 1
            addi $a1, $a1, 1
            bne $t0, $zero, concatStrCopyLoop
        addiu $sp $sp 4
        lw $ra, 0($fp)
        jr $ra
.data
    # Excepciones de division por cero
    divZero: .asciiz "RUNTIME EXCEPTION: division por cero."
    modZero: .asciiz "RUNTIME EXCEPTION: division por cero en operacion modulo."

    # Excepciones de Array
    negativeArraySize:       .asciiz "RUNTIME EXCEPTION: la longitud del array no puede ser negativa."
    arrayIndexOutOfRange:    .asciiz "RUNTIME EXCEPTION: indice del array fuera de rango."
    negativeArrayIndex:      .asciiz "RUNTIME EXCEPTION: indice del array negativo."

    # Excepcion de booleano incorrecto
    incorrectInBoolIO:       .asciiz "RUNTIME EXCEPTION: se esperaba 0 o 1 como entrada de un Bool."
    # Cuando se intenta usar una clase o array no inicializado
    variableNotInitializedMsg: .asciiz "RUNTIME EXCEPTION: se intenta acceder a una variable no inicializada."

.text
    divZeroException:
        la $a0, divZero
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    modZeroException:
        la $a0, modZero
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    negativeArraySizeException:
        la $a0, negativeArraySize
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    arrayIndexOutOfRangeException:
        la $a0, arrayIndexOutOfRange
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    negativeArrayIndexException:
        la $a0, negativeArrayIndex
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    incorrectInBoolIOException:
        la $a0, incorrectInBoolIO
        li $v0, 4
        syscall
        li $v0, 10
        syscall

    variableNotInitialized:
            la $a0, variableNotInitializedMsg
            li $v0, 4
            syscall
            li $v0, 10
            syscall
