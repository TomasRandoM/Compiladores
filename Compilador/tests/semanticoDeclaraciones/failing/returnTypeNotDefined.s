//Incorrecto. El tipo de retorno de getX() (C), no está definido
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
    fn C getX() {
        ret 1;}}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (b.getX());
}
