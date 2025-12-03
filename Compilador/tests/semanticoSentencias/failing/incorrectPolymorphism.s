// Incorrecto. Se trata de asignar un objeto de tipo C a una variable de tipo declarada A. Además C no hereda de A
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
    a = new C();
}
