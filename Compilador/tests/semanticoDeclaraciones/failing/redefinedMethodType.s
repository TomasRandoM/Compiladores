//Incorrecto: se redefine mal el metodo f(). No tiene el mismo tipo de retorno
 class A {
 }

 impl A {
     .() {}
     fn Int f(){ ret 1; }}

 class B : A {}

 impl B {
     .() {}
     fn Str f() { ret "1"; }}

start {
    A a;
    B b;
    a = new A();
    b = new B();
}
