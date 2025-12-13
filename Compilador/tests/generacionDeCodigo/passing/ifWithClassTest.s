class A {
    pub Int a;
    }
impl A {
    .() {}
    }
start {
     Bool b;
     Int s;
     A a;
     a = new A();
     a.a = 5;
     if (a.a == 5) {
        (IO.out_int(a.a));
     }
     else {
        (++a.a);
        (IO.out_int(a.a));
     }

 }