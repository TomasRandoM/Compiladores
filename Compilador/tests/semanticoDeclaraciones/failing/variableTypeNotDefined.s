//Incorrecto. El tipo de la variable c (C), no está definido
class A {
    Int x;}

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
    C c;
    a = new A();
    b = new B();
    (b.getX());
}
