//Incorrecto. Hay dos definiciones de clase A
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
}

class A {
    Int b;
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
