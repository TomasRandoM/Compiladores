// Incorrecto. Se accede al atributo privado b de B desde la clase A
class A {
    Int x;
    Int y;
}

impl A {
    .() {
    }
    fn a() {
        B b;
        b = new B();
        y = b.b;
    }
}

class B {
    Int b;
}

impl B {
    .() {
    }
}

start {
}
