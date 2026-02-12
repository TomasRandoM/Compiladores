//Se intenta acceder a atributo privado (mediante ArrayAccessNode)
class A {
    Array Int a;
}

impl A {
    .() {
    a = new Int[5];
    a[0] = 1;
    }
}

class B : A {

}

impl B {
    .() {
        a[3] = 3;
    }
}

start {
    B b;
    b = new B();
}