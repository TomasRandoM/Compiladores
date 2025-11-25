//Incorrecto: herencia circular intermedia

class A : B {

}
class B : C {

}
class C : B {

}

impl A {
    .() {}
}

impl B {
    .() {}
}

impl C {
    .() {}
}

start { }
