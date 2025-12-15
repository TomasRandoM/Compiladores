//Se ingresa un Str como entrada. La salida es un 0, lo leido por pantalla y lo leido por pantalla concatenado con hola

class A {
    pub Str s;
    pub B b;
}
impl A {
    .() {
    b = new B();
    }
}
class B {
    pub Int a;
}
impl B {
    .() {}
}
start {
    A a;
    Str b;
    Str c;
    a = new A();
    (IO.out_int(a.b.a));
    (IO.out_str("\n"));
    a.s = IO.in_str();
    (IO.out_str(a.s));
    (IO.out_str("\n"));
    b = "hola";
    (IO.out_str(a.s.concat(b)));
}