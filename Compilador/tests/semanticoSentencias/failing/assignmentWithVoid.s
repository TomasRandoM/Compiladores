// Incorrecto. Se asigna un void
class A {
    Int x;
    Int y;
}

impl A {
    .() {
    }
    fn a() {
        y = 5;
    }
}

start {
    Int x;
    Int b;
    A a;
    x = 4;
    a = new A();
    b = a.a();
}
