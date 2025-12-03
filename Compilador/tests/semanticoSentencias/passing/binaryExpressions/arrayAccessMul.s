// CORRECTO: a.arr2[0] es Int, y se puede multiplicar Int * Int.

class A {
    pub Array Int arr2;
}
impl A { .() {} }

start {
    Int x;
    A a;

    a = new A();
    x = a.arr2[0] * 3;
}
