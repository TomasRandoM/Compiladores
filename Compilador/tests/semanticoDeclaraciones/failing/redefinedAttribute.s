//Incorrecto. La clase A posee dos declaraciones de atributo con el mismo nombre
class A {
    Int x;
    Int c;
    Double x;
    }

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        Double y;
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
