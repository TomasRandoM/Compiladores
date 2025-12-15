//Imprime el array vacio, luego el array con los elementos asignados. Finalmente imprime un 5 (longitud del array)
start {
    Array Double a;
    a = new Int[5];
    (IO.out_array_double(a));
    a[0] = 1.0;
    a[1] = 0.0;
    a[3] = 3.45;
    a[4] = 4.14;
    (IO.out_array_double(a));
    (IO.out_int(a.length()));

}