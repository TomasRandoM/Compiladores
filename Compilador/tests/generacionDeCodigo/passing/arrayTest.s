//Imprime el array vacio, luego el array con un 0 en su primera posicion y las otras con los elementos asignados. Finalmente imprime un 5 (longitud del array)
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
    a[0] = ((Int) (b[1] % b[0])); //0
    (IO.out_array_int(a));
    (IO.out_int(a.length()));

}