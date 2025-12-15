//Imprime 5 y 7.0

class A {
}

impl A {
    .() {}
    fn Int complexInt(Int a, Int b, Int c) {
            // ((a + b) * (c - 2)) / (b + 1)
            ret (Int) (((a + b) * (c - 2)) / (b + 1));
        }

    fn Double complexDouble(Double x, Double y, Int z) {
        // (x * (y + 2.5)) - (z / (x - 1.0))
        ret ((x * (y + 2.5)) - (z / (x - 1.0)));
    }
}

start {
    A a;
    Int r1;
    Double r2;

    a = new A();

    r1 = a.complexInt(3, 4, 6);
    (IO.out_int(r1));        // (7*4)/5 = 5
    (IO.out_str("\n"));

    r2 = a.complexDouble(2.0, 3.0, 4);
    (IO.out_double(r2));     // 11 - 4 = 7.0
    (IO.out_str("\n"));
}
