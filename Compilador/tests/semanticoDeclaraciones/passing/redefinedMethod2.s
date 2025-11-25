class A { }

impl A {
    .() {}
    fn Int f(Int a, Str b) { ret a; }
}

class B : A { }

impl B {
    .() {}
    fn Int f(Int a, Str b) { ret a; }
}

start {
    B b;
    b = new B();
}
