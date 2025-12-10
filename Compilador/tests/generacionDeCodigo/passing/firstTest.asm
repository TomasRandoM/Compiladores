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
l.d $f0, zeroDouble
s.d $f0, 0($sp)
addiu $sp $sp -8
#Sentencias del bloque 
#Asignación 
#Literales
li $a0, 5
sw $a0, 0($sp) 
addiu $sp $sp -4 
#Literales
li $a0, 0
#Ni left ni right son double
#El lado izquierdo queda en el t0 y el lado derecho en el a0 
lw $t0, 4($sp) 
addiu $sp $sp 4
#Ningun tipo es double
#Se convierten los tipos a double para la operacion de division 
mtc1 $t0, $f2
mtc1 $a0, $f0
cvt.d.w $f0, $f0
cvt.d.w $f2, $f2
div.d $f0, $f2, $f0
s.d $f0, 0($sp) 
addiu $sp $sp -8 
#Carga de variable 
#Cargamos la direccion de la variable en a0 utilizando el 
#offset con el fp 
la $a0, -4($fp) 
addiu $sp $sp 8 
l.d $f0, 0($sp) 
#Se guarda el double del lado derecho en la direccion de a0 
s.d $f0, 0($a0) 
#Fin del programa 
li $v0, 10 
syscall 
