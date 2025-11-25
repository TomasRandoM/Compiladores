//Correcto. Los atributos y variables tienen diferente nombre
class A {
    Int x;
    Int c;
    Int y;
    }

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        Double y;
        Int f;
        Int g;
        ret x;
    }}

class B : A {
}

impl B {
    .() {}
    fn Int getX() {
        ret 1;}
    }

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (a.getX());
    (b.getX());
}
