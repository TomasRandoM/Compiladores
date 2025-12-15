//Fibonacci pero polimorfico, imprime 55 dos veces
class FibBase {
}
impl FibBase {
    .() {}
    fn Int fib(Int n) {
            ret 0;
    }
}

class FibIter : FibBase {
}

impl FibIter {
    .() {
    }
    fn Int fib(Int n) {
            Int a;
            Int b;
            Int i;
            Int tmp;

            a = 0;
            b = 1;
            i = 0;

            while (i < n) {
                tmp = a + b;
                a = b;
                b = tmp;
                i = i + 1;
            }

            ret a;
        }
}

class FibRec : FibBase {
}

impl FibRec {
    .() {}
    fn Int fib(Int n) {
        if (n < 2) {
            ret n;
        }
        ret fib(n - 1) + fib(n - 2);
    }
}

start {
    FibBase f;

    f = new FibIter();
    (IO.out_int(f.fib(10)));   // 55
    (IO.out_str("\n"));
    f = new FibRec();
    (IO.out_int(f.fib(10)));   // 55
}
