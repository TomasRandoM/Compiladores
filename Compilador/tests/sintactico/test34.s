//Correcto
class A { }
impl A {
    fn A f() { ret new A(); }
}
start {
    A a;
    a = new A().f().f().f();
}
