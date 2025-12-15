//Imprime par 4, impar 3, par 2, impar 1, resultado=10
class A {
}

impl A {
    .() {}
    fn Int process(Int n) {
            Int acc;
            acc = 0;

            while (n > 0) {
                acc = acc + n;

                if ((n % 2) == 0) {
                    (IO.out_str("par "));
                } else {
                    (IO.out_str("impar "));
                }

                (IO.out_int(n));
                (IO.out_str("\n"));

                n = n - 1;
            }

            ret acc;
    }
}

start {
    A a;
    Int x;
    Int r;

    a = new A();
    x = -3;

    if (x > 0) {
        (IO.out_str("positivo\n"));
    } else {
        r = a.process(4);
        (IO.out_str("resultado="));
        (IO.out_int(r));
        (IO.out_str("\n"));
    }
}
