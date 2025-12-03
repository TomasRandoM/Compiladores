// Incorrect: arr[1] es Int y s es Str y la comparación == esta prohibida para tipos distintos.

class A { pub Array Int arr2; }
impl A { .() {} }

start {
    Bool b;
    Str s;
    Array Int arr;

    arr = new Int[5];
    b = arr[1] == s; // error
}
