//Incorrecto. El start debe ir después de las definiciones de clases/impl
start {
    Int x;
    x = 1;
}
class A {
    Int y;
}
impl A {
    .() {
        y = 0;
    }
}
