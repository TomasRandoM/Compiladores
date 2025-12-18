RUNTIME EXCEPTION: se intenta acceder a una variable no inicializada.
class A {
    pub Int a;
}

impl A {
    .() {}
}
start {
    A a;
    a.a = 5;
    (IO.out_int(a.a));
}