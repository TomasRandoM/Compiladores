//Se intenta acceder a atributo privado (mediante variable)
class A {
    Int a;
}

impl A {
    .() {
    }
}

class B : A {

}

impl B {
    .() {
        a = 3;
    }
}
start {
    B b;
    b = new B();
}