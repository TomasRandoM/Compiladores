//Correcto
class A {
    Int x;
}

impl A {
    .() {}
}

class B : A {
    Str y;
}

impl B {
    .() {}
}

class C : B {
    Bool z;
}

impl C {
    .() {}
}

start {
    C c;
    c = new C();
}
