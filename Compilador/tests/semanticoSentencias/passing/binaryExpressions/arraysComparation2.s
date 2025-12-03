// CORRECTO: Arrays son objetos, y se pueden comparar porque son del mismo tipo.

class A { pub Array Int arr2; }
impl A { .() {} }

start {
    Bool b;
    Array Int arr;
    A a;

    arr = new Int[5];
    a = new A();

    b = arr == a.arr2; // ok
}
