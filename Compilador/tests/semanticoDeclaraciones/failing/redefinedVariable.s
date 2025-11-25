//Incorrecto. El método getX() de A posee dos declaraciones de variable con el mismo nombre
class A {
    Int x;
    Int c;
    }

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        Double y;
        Int y;
        ret x;
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
