// CORRECTO: ambos accesos de array son Int y se pueden comparar.

class A { pub Array Int arr2; }
impl A { .() {} }

start {
    Bool b;
    Array Int arr;
    A a;

    arr = new Int[5];
    a = new A();

    b = a.arr2[6] > arr[1]; // ok
}
