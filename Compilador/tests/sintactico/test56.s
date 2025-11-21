//Incorrecto: new clase sin ()
class A { }

impl A {
    .() { }
}

start {
    A a;
    Array Int arr;
    a = new A;
    arr = new Int[3];
}
