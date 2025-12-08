// Incorrecto. El método llamado es de la clase B pero el objeto fue declarado como de clase A
class A {
    Int x;
    Int y;
}

impl A {
    .() {
    }
    fn A a(Double a) {
        A c;
        y = 5;
        ret c;
    }
}

class B : A {
}

impl B {
    .() {}
    fn Int b(Double b) {
        ret 5;
    }
}

class C {
}

impl C {
    .() {}
}


start {
    Int x;
    A a;
    a = new B();
    (a.b(5.5));
}
