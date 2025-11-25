//Incorrecto. La clase A posee 2 impl
class A {
    Int x;}

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        ret x;
    }}

impl A {
    .() {
        x = 1;
    }
    fn Int getX() {
        ret 2;
    }}

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
    (a.getX());
    (b.getX());
}
