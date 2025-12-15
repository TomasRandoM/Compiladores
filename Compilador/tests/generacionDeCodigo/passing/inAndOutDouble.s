//Se ingresa un double como entrada y se imprime por pantalla 3 + entrada

class A {
    pub Double x;
}
impl A {
    .() {}
}
start {
    A a;
    a = new A();
    a.x = IO.in_double();
    a.x = 3 + a.x;
    (IO.out_double(a.x));
}