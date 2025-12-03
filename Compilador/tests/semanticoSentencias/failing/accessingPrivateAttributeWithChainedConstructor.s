// Incorrecto. El atributo x de A es privado
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
    A a;
    x = 4;
    x = new A().x;
}
