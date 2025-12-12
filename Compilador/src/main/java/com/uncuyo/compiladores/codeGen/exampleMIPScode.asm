

.data
    addOne: .double 1.0
    zeroDouble: .double 0.0
    stringInitialization: .asciiz ""
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
        bc1t enddouble
        li $a0, 1
        j enddouble

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


class A {
    Int x;
    Double y;
    Str z;
}

impl A {
    .() {

    }

    fn Int b(Int y) {
        y = 5;
        ret y;
    }
}

start {
    A a;
    Int r;
    a = new A();
    (r = a.b(5));
}

.data:
    vtA:
        .word bA

.text

    main:
        #Creamos el primer stack frame
        move $fp, $sp
        #Movemos la pila para coherencia, pues no va a haber return address en el start
        addiu $sp $sp -4

        #A a
        $Reservamos memoria en la pila para la variable local y la inicializamos en 0 (nil) porque es una clase
        li $a0, 0
        sw $a0, -4($sp)
        addiu $sp $sp -4
        #Int r
        li $a0, 0
        sw $a0, -4($sp)
        addiu $sp $sp -4

        #guardamos el fp
        sw $fp, 0($sp)
        #Bajamos 8 espacios en la pila para dejarle 4 bytes al self que asignara el constructor
        addi $sp $sp -8
        #lado derecho de la asignacion (se guarda el resultado en $v0)
        jal constructorA
        #borramos pila y recuperamos el framepointer
        addi $sp $sp 8
        lw $fp 0($sp)

        #esto se ejecuta por ser un metodo
        move $a0, $v0
        #esto se ejecuta siempre que hay una asignacion
        move $t0, $a0
        #lado izquierdo de la asignacion
        la $a0, -4($fp)
        #asignacion
        sw $t0, 0($a0)

        #segunda asignacion
        #lado derecho asignacion
        #accedemos a a
        la $a0, -4($fp)
        #movemos la direccion de a a t0 para luego pasarla como parametro (como self)
        move $t0, $a0
        #Accedemos a la Vtable
        la $a0, 0($a0)
        #Accedemos al metodo b de la vtable, en este caso, es el primero
        la $a0, 0($a0)
        #Guardamos el a0 en la pila debido a que un método puede ser un parametro
        sw $a0, 0($sp)
        addiu $sp $sp -4

        #Guardamos el fp actual
        sw $fp, 0($sp)
        addiu $sp $sp -4
        sw $t0, 0($sp)
        addiu $sp $sp -4
        #cargamos los parametros
        li $a0, 5
        sw $a0, 0($sp)
        addiu $sp $sp -4

        #cargamos la direccion del metodo b, EN ESTE CASO es 16, pero depende de la cantidad de parametros y su tipo
        lw $a0, 16($sp)
        jalr $a0

        #borramos los parametros y el fp de la pila
        addiu $sp $sp 12
        #restauramos el fp
        lw $fp, 0($sp)
        #Borramos la direccion del metodo b que habia sido guardado en la pila debido a que un parametro podia llegar a ser un metodo
        addiu $sp $sp 4

        #retorno del metodo
        move $a0, $v0
        #esto se ejecuta siempre que hay una asignacion
        move $t0, $a0
        #lado izquierdo de la asignacion
        la $a0, -8($fp)
        #asignacion
        sw $t0, 0($a0)

        li $v0, 10
        syscall

    constructorA:
        #Guardamos el framepointer nuevo
        move $fp, $sp
        #Guardamoes el return address
        sw $ra, 0($sp)
        #Movemos la pila 1
        addiu $sp $sp -4

        li $a0, 20
        # guardamos la memoria que pusimos en $a0
        li $v0, 9
        syscall

        #movemos la memoria solicitada a t0
        move $t0, $v0
        #Guardamos la dirección a la memoria asignada en el registro de activacion para representar el self
        sw $t0, 4($fp)
        #guardamos la vt en 0(t0)
        la $a0, vtA
        sw $a0, 0($t0)
        # inicializamos el int en 0 y lo guardamos en el offset 0
        sw $zero, 4($t0)
        # inicializamos el double en 0.0 y lo guardamos en el offset 8
        l.d $f0, zeroDouble
        s.d $f0, 8($t0)
        #Inicializamos el string con "" y lo guardamos en el offset 16
        la $a0, stringInitialization
        sw $a0, 16($t0)

        #salimos del constructor y dejamos el resultado en el v0
        move $v0, $t0
        lw $ra, 4($sp)
        addiu $sp $sp 4
        jr $ra

    #metodo b de la clase A
    bA:
        #Guardamos el framepointer nuevo
        move $fp, $sp
        #Guardamoes el return address
        sw $ra, 0($sp)
        #Movemos la pila 1
        addiu $sp $sp -4

        #lado derecho asignacion
        li $a0, 5
        move $t0, $a0
        #lado izquierdo asignacion
        #codigo de variable
        la $a0, 8($fp)
        #codigo de asignacion
        sw $t0, 0($a0)

        #codigo de variable (expresion del return)
        la $a0, 8($fp)
        #retorno
        lw $v0, 0($a0)
        #restablezco ra
        lw $ra, 4($sp)
        #vuelvo al llamador
        addiu $sp $sp 4
        jr $ra



#retornos de funciones en v0 o f0 (si es double), cargas en a0 o f0 (si es un double)