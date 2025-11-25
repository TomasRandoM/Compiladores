//Incorrecto. B redefine getX() de A pero con diferente cantidad de parámetros
class A {
    Int x;}

impl A {
    .() {
        x = 0;
    }
    fn Int getX(Int x, Double y) {
        ret x;
    }}

class B : A {
}

impl B {
    .() {}
    fn Int getX(Int x) {
        ret 1;}}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (a.getX());
    (b.getX());
}
