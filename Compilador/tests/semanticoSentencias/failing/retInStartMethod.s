// Incorrecto. El bloque start no puede tener ret
class A {
    Int x;
    Int y;
}

impl A {
    .() {

    }
    fn a() {
        y = 5;
    }
}

start {
    Int x;
    x = 4;
    ret x;
}
