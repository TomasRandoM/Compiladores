class A {
    pub B b;
    }
impl A {
    .() {
    b = new B();
    }
}
class B {
    pub Int a;
}

impl B {
    .() {}
}
start {
     Bool b;
     Int s;
     A a;
     a = new A();
     a.b.a = 5;
     if (a.b.a != 5) {
        (IO.out_int(a.b.a));
     }
     else {
        (++a.b.a);
        (IO.out_int(a.b.a));
     }

 }