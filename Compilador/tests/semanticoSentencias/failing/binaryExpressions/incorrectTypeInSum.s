// Incorrecto No se puede sumar Int + Bool
class A {
    pub Array Int arr2;
}
impl A { .() {} }

start {
    Int x;
    x = 1 + true;  // error
}
