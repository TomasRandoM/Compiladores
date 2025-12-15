//Imprime 9.4, 9.4 y 3.8
class A {
    pub Int a;
    pub Double b;
}
class B : A {
    pub Double c;
}

impl A {
    .() {}
    }
impl B {
    .(Double c, Double b) {
    self.c = c;
    self.b = b;
    }

    fn hola(Double c) {
    (IO.out_double(c));
    }
}

start {
     B b;
     Double d;
     b = new B(9.4, 3.8);
     b.a = 2;
     d = b.c - b.b;
     (b.hola(9.4));
     (IO.out_str("\n"));
     (IO.out_double(b.c));
     (IO.out_str("\n"));
     (IO.out_double(b.b));
 }