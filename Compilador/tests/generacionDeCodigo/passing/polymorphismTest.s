//Imprime 3.8
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
    a = 4;
    }

    fn hola(Double c) {
    (IO.out_double(c));
    }
}

start {
     A b;
     Double d;
     b = new B(9.4, 3.8);
     (IO.out_double(b.b));
 }