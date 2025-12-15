//Imprime 4.966
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
    .() {}
}

start {
     B b;
     Double d;
     b = new B();
     b.b = 5.5334;
     b.a = 2;
     b.c = 10.5;
     d = b.c - b.b;
     (IO.out_double(d));
 }