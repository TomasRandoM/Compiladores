//Incorrecto. getB2(Int x) de A posee una variable con el mismo nombre que el parámetro que recibe
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
    fn Int getB2(Int x) {
        Double x;
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
