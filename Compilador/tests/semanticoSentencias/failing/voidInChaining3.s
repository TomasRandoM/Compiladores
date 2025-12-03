// Incorrecto. Void en un encadenamiento
class A {
    Int x;
    Int y;
    Array Int c;
}

impl A {
    .() {
    }
    fn a() {
        y = 5;
        y = self.b().c();
    }
    fn b() {
    }
}

start {
    Int x;
    A a;
    x = 4;
}
