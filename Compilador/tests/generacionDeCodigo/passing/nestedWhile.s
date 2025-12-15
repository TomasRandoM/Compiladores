//Imprime la multiplicacion de i (1 a 3) por j (1 a 4)
start {
    Int i;
    Int j;

    i = 1;
    while (i <= 3) {
        j = 1;
        while (j <= 4) {
            (IO.out_int(i * j));
            (IO.out_str("\n"));
            j = j + 1;
        }
        i = i + 1;
    }
}
