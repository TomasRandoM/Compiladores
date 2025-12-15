//Imprime el array inicializado en 0, luego el array con un 4.7 en su primera posicion y las otras con los elementos asignados. Finalmente imprime un 5 (longitud del array)
start {
    Array Double a;
    Array Double b;
    b = new Double[2];
    a = new Double[5];
    (IO.out_array_double(a));
    b[0] = 5.3;
    b[1] = 10.0;
    a[0] = 1.0;
    a[1] = 1.5;
    a[3] = 2.6;
    a[4] = 3.74;
    a[0] = (b[1] % b[0]); //0
    (IO.out_array_double(a));
    (IO.out_int(a.length()));

}