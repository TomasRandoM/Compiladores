//No falla pero explota en ejecución debido a la falta del ret
class A {
}

impl A {
    .(){}
    fn Int abs(Int x) {
        Int a;
        if (x < 0) {
            ret -x;
        } else {
            a = 8;
        }
    }
}
start {
    A a;
    a = new A();
    (IO.out_int(a.abs(3)));
}
