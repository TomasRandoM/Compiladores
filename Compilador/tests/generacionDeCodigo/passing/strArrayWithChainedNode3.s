// Correcto. Se llama al método concat desde un objeto del array que es Str
class A {
    pub Array Str x;
    Int y;
}

impl A {
    .() {
    }
    fn a(Int a) {
        Str s;
        s = "chau";
        x[0] = "holas";
        (IO.out_str(x[0].concat(s)));
    }
}

start {
    Int x;
    A a;
    a = new A();
    a.x = new Str[5];
    (a.a(5));

}
