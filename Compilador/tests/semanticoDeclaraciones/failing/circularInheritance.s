//Incorrecto: A hereda de A
class A {
    Int x;}

impl A {
    .() {
        x = 0;
    }
    fn Int getX() {
        ret x;
    }}

class A : A {
}

start {
    A a;
    a = new A();
    (a.getX());
}