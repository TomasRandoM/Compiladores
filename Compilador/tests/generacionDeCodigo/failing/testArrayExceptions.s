//Incorrecto. Se testea la excepcion del array al accederse a un indice fuera del rango
class A {
    pub Array Int hola;
}

impl A {
    .() {
    }
    fn getArrayIndex(Int a) {
        hola[a] = 4;
    }
}

start {
    A a;
    a = new A();
    a.hola = new Int[5];
    (a.getArrayIndex(7));
}