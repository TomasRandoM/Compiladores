// Correcto. El polimorfismo es correcto
class A {
    Int x;
    Int y;
}

impl A {
    .() {
    }
    fn a(Double a) {
        y = 5;
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
    a = new B();
}
