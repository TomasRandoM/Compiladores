// Incorrecto: arr.x es inválido: los arrays no tienen propiedades llamadas x.

class A {
    pub Array Int arr2;
}
impl A { .() {} }

start {
    Int x;
    Array Int arr;

    arr = new Int[5];
    x = arr.x - 1; // error
}
