// Correcto. Se llama al método length desde un objeto del array que es Str. Imprime 4
class A {
    pub Array Str x;
    Int y;
}

impl A {
    .() {
    }
    fn a(Int a) {
        x[0] = "hola";
    }
}

class B {
    pub A a;
}

impl B {
    .() {
    a = new A();
    a.x = new Str[5];
    (a.a(5));
    }
}

start {
    Int x;
    B b;
    b = new B();

    (IO.out_int(b.a.x[0].length()));
}
