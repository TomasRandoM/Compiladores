start {
    Array Int a;
    Array Int b;
    b = new Int[2];
    a = new Int[5];
    (IO.out_array_int(a));
    b[0] = 5;
    b[1] = 10;
    a[0] = 1;
    a[1] = 1;
    a[3] = 2;
    a[4] = 3;
    a[0] = (a[1] + b[0]); //6
    (IO.out_array_int(a));
    (IO.out_int(a.length()));

}