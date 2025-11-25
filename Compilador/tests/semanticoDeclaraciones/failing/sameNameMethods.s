//Incorrecto. Existen dos definiciones del método getC()
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
    fn Int getC() {
        ret 3;
    }
}

start {
}
