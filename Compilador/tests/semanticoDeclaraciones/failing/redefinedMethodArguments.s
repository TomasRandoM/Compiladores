//Incorrecto: se redefine mal el metodo f(). No tiene la misma cantidad de parámetros
 class A {
 }

 impl A {
     .() {}
     fn Int f(Int x, Str y){ ret 1; }}

 class B : A {}

 impl B {
     .() {}
     fn Int f(Int x, Str y, Bool z) { ret 1; }}

start {
    A a;
    B b;
    a = new A();
    b = new B();
}
