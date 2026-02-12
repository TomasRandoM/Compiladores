//Imprime 2 y 3
class A {

}

impl A {
    .() {
    }

    fn void a() {
        (IO.out_int(1));
    }

    fn void b() {
        (IO.out_int(3));
    }
}


impl B {
    .() {
        }

    fn void a() {
        (IO.out_int(2));
    }
}

class B : A {
}

start {
    B b;
    b = new B();
    (b.a());
    (IO.out_str("\n"));
    (b.b());
}
