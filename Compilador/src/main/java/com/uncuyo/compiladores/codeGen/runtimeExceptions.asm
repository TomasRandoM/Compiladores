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
