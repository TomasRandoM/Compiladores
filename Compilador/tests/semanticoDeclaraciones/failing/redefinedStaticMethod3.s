//Incorrecto: se redefine un metodo a static
 class A {
 }

 impl A {
     .() {}
     fn Int f(Int x, Str y){ ret 1; }}

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
