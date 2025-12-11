// Correcto. A y se le asigna un nil
class A {
    Int x;
    B y;
}
class B {
    Int c;
}
impl B {
    .() {
    }
}
impl A {
    .() {

    }
    fn B a() {
        y = nil;
        ret y;
    }
}

start {
    Int x;
    x = 4;
}
