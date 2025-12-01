// Incorrecto. Se intenta acceder a un atributo privado
class A {
    Int x;
}

impl A {
    .() {
    x = 1;
    }
}

class B {
}

impl B {
    .() {}
    fn b() {
        A a;
        a = new A();
        a.x = 10;
    }
}

start {}
