//Incorrecto. getB2(Int x, B y, C c) recibe un parámetro de un tipo que no existe
class A {
    Int x;
}

impl A {
    .() {
        x = 1;
    }
    fn Int getC() {
        ret 2;
    }
    fn Int getA() {
        ret 2;
    }
    fn Int getB2(Int x, B y, C c) {
        Double d;
        ret 2;
    }
}

class B {
    Int x;
}

impl B {
    .() {
        x = 1;
    }
    fn Int getC() {
        ret 2;
    }
    fn Int getA() {
        ret 2;
    }
    fn Int getB2() {
        ret 2;
    }
}

start {
}
