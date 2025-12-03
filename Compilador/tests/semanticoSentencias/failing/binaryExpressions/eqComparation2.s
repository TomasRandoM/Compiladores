// Incorrecto: (arr == arr[1]) es Bool. No puede asignarse a Double.

class A { pub Array Int arr2; }
impl A { .() {} }

start {
    Double d;
    Array Int arr;

    arr = new Int[5];
    d = (arr == arr[1]); // error
}
