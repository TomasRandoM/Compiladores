//Incorrecto: se redefine mal el metodo f(). No tiene los mismos tipos de parámetros
 class A {
 }

 impl A {
     .() {}
     fn Int f(Int x, Str y){ ret 1; }}

 class B : A {}

 impl B {
     .() {}
     fn Int f(Int x, Int y) { ret 1; }}

start {
    A a;
    B b;
    a = new A();
    b = new B();
}
