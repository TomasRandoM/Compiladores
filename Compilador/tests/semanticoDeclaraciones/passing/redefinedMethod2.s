//Correcto
class C { }

impl C {
    .() {}
    fn Int f(Int a, Str b) { ret a; }
}

class D : C { }

impl D {
    .() {}
    fn Int f(Int a, Str b) { ret a; }
}

start {
    D b;
    b = new D();
}
