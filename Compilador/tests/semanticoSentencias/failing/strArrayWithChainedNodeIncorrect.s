// Incorrecto. Se llama al método lengthf que no existe en la clase Str (del objeto dentro del array)
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
    (IO.out_int(a.x[0].lengthf()));
}
