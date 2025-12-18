// Correcto. Se llama al método length desde un objeto del array que es Str
class A {
    pub Array Str x;
    Int y;
}

impl A {
    .() {
    }
    fn a(Int a) {
        y = 5;
    }
}

class B {
    pub A a;
}

impl B {
    .() {
    a = new A();
    a.x = new Str[5];
    }
}

start {
    Int x;
    B b;
    b = new B();

    (IO.out_int(b.a.x[0].length()));
}
