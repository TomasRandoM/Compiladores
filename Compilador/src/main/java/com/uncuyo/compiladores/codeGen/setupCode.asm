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
            #Cargo en f0 el elemento que se usará para inicializar el array
            l.d $f0 8($fp)
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
                s.d $f0, 0($v0)
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

        #Cargamos el double al f0
        l.d $f0 8($fp)
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
                l.d $f12, 0($t2)
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
