// Correcto. Polimorfismo en parámetros
class A {
    Int x;
    Int y;
}

impl A {
    .(A a) {
    }
    fn A a(A a) {
        A c;
        y = 5;
        ret c;
    }

    fn A b() {
        B b;
        (a(b));
        ret b;
    }
}

class B : A {
}

impl B {
    .() {}
}

class C {
}

impl C {
    .() {}
}


start {
    Int x;
    A a;
    B b;
    a = new A(b);
    (a.a(b));
}
