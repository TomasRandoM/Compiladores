class A {
    pub Int a;
}

impl A {
    .() {}
}
start {
    A a;
    a = new A();
    a.a = 5;
    (IO.out_int(a.a));
}