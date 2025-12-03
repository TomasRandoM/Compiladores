// Incorrecto. La parámetros del constructor en la linea 19 son más de los solicitados
class A {
    Int x;
    Int y;
}

impl A {
    .(Int a) {
    }
    fn a() {
        y = 5;
    }
}

start {
    Int x;
    A a;
    x = 4;
    a = new A(5, 3);
}
