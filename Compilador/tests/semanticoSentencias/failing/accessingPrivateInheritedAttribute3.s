//Se intenta acceder a atributo privado (mediante Chained con array)
class A {
    Array Int a;
}

impl A {
    .() {
    a = new Int[5];
    a[0] = 1;
    }
}

start {
    A a;
    a = new A();
    (IO.out_int(a.a[0]));
}