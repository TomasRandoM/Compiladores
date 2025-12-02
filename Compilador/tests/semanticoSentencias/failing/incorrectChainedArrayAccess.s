//Incorrecto: Se intenta acceder a una variable luego del array
class A {
    pub Array Int arr;
}

impl A {
    .() {}
    fn Int get(Int i) {
        ret arr[i];
    }
}

start {
    A a;
    Int x;
    Array Int arr;
    Int h;

    a = new A();
    a.arr = new Int[3];
    x = a.arr[1].h;     // acceso inválido
}