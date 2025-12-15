//Imprime [1,2,3,4,5]
class A {
}

impl A {
    .() {}
    fn void bubble(Array Int arr, Int n) {
        Int i;
        Int j;
        Int tmp;

        i = 0;
        while (i < n) {
            j = 0;
            while (j < n - 1) {
                if (arr[j] > arr[j + 1]) {
                    tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
                j = j + 1;
            }
            i = i + 1;
        }
    }
}

start {
    A a;
    Array Int v;

    a = new A();
    v = new Int[5];
    v[0] = 5;
    v[1] = 1;
    v[2] = 4;
    v[3] = 2;
    v[4] = 3;

    (a.bubble(v, 5));
    (IO.out_array_int(v));
}
