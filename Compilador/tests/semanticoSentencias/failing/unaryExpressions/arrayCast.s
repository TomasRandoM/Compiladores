//INCORRECTO: base Array es Str.
class A {
    pub Array Str arr;
}
impl A { .(){} }

start {
    A a;
    Int x;
    a = new A();
    x = (Int) a.arr;
}
