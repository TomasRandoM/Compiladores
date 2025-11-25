//Incorrecto: metodo estático redefinido

class D {}
class A : B {}
class B : C {}
class C : D {}

impl A {
    .() {}
    st fn Int f(Int x, Str y){ ret 1; }
}

impl B {
    .() {}
}

impl C {
    .() {}
    st fn Int f(Int x, Str y){ ret 23; }
}

impl D {
    .() {}
}

start { }
