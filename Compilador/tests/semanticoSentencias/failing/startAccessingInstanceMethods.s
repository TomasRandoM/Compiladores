//Incorrecto. Start accede a un método de instancia
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
    }
    fn A b() {
        A a;
        ret a;
    }
    fn Int c() {
        ret 4;
    }
}


start {
    Int x;
    A a;
    x = 4;
    (a());
}
