//Incorrecto. El tipo del atributo c (C), no está definido
class A {
    Int x;
    C c;
    }

impl A {
    .() {
        x = 0;
    }
}

class B : A {
}

impl B {
    .() {}
    fn Int getX() {
        ret 1;}}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (b.getX());
}
