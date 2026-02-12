//Se intenta acceder a atributo privado (mediante Chained)
class A {
    Int a;
}

impl A {
    .() {
    }
}

start {
    A a;
    a = new A();
    (IO.out_int(a.a));
}