//Correcto: cast + new Int[...] + encadenado
start {
    Int a;
    Double b;
    Array Int arr;

    b = 3.7;
    a = (Int) b;
    arr = new Int[(Int) b + 1];

    arr[0] = a;
    (IO.out_int(arr[0]));
}
