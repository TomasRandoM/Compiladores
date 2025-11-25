//Correcto. B hereda x de A y no lo intenta redefinir
class A {
    Int x;}

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        ret x;
    }}

class B : A {
    Double y;
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
    (a.getX());
    (b.getX());
}
