//Incorrecto: se redefine un metodo static
 class A {
 }

 impl A {
     .() {}
     st fn Int f(Int x, Str y){ ret 1; }}

 class B : A {}

 impl B {
     .() {}
     st fn Int f(Int x, Str y) { ret 5; }}

start {
    A a;
    B b;
    a = new A();
    b = new B();
}
