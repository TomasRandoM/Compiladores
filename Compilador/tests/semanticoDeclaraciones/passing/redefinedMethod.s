//correcto: se redefine el metodo f().
 class A {
 }

 impl A {
     .() {}
     fn Int f(Int a, Str b)
        { ret 1; }
 }

 class B : A {}

 impl B {
     .() {}
     fn Int f(Int j, Str m) {
         Int y;
         y = 3;
         ret 1; }
     }

start {
    A a;
    B b;
    a = new A();
    b = new B();
}
