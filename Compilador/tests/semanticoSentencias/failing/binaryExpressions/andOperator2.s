// Incorrecto: El operador && requiere dos Bool. el Array es Int.

start {
    Bool b;
    Array Int arr;

    arr = new Int[5];
    b = arr[7] && b; // error
}
