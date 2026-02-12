//Imprime 66

class A {
    pub Int a;
}

impl A {
    .() {
        a = 1;
    }
    fn Int a() {
        ret a;
    }
}

start {
    Array Int a;
    A a1;
    a1 = new A();
    a = new Int[5];
    a[0] = 5;
    a[1] = 6;
    a[3] = 2;
    a[4] = 3;
    (IO.out_int(a[a1.a]));
    (IO.out_int(a[a1.a()]));

}