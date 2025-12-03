// Incorrecto: No se puede sumar un Array con un Int.

class A {
    pub Array Int arr2;
}
impl A { .() {} }

start {
    Int x;
    Array Int arr;

    arr = new Int[5];
    x = arr + 1; // error
}
