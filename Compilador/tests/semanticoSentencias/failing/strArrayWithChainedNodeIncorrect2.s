// Incorrecto. Se intenta acceder a un atributo de un Str en la linea 21
class A {
    pub Array Str x;
    Int y;
}

impl A {
    .() {
    }
    fn a(Int a) {
        y = 5;
    }
}

start {
    Int x;
    A a;
    x = 4;
    (a.a(5));
    a.x = new Str[5];
    (IO.out_int(a.x[0].lengthf));
}
