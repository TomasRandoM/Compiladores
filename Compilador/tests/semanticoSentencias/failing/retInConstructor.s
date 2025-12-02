// Incorrecto. El constructor no puede tener ret
class A {
    Int x;
    Int y;
}

impl A {
    .() {
        ret 5;
    }
    fn a() {
        y = 5;
    }
}

start {
    Int x;
    x = 4;
}
