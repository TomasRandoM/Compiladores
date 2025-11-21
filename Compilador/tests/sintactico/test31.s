//Correcto
class A { }
impl A {
    fn Int f() { ret 3; }
}
start {
    A a;
    a = new A();
    (a.f());
}
