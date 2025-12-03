// Incorrecto. Hay polimorfismo mal hecho en método encadenado porque C no hereda de A
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
    C b;
    B d;
    a = new A(d);
    (a.a(b));
}
