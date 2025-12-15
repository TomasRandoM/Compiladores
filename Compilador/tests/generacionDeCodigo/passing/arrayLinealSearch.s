//Imprime 3
class A {
}

impl A {
    .() {}
    fn Int find(Array Int arr, Int n, Int x) {
        Int i;
        i = 0;
        while (i < n) {
            if (arr[i] == x) {
                ret i;
            }
            i = i + 1;
        }
        ret -1;
    }
    }

start {
    A a;
    Array Int v;
    a = new A();
    v = new Int[5];
    v[0] = 3;
    v[1] = 7;
    v[2] = 2;
    v[3] = 9;
    v[4] = 1;
    (IO.out_int(a.find(v, 5, 9)));
}
