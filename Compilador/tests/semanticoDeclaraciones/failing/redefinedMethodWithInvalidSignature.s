//Incorrecto. A redefine getX() de B pero con diferente tipo de parámetros
class A {
    Int x;}

impl A {
    .() {
        x = 0;
    }
    fn Int getX(Int x) {
        ret x;
    }}

class B : A {
}

impl B {
    .() {}
    fn Int getX(Double x) {
        ret 1;}}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (a.getX());
    (b.getX());
}
