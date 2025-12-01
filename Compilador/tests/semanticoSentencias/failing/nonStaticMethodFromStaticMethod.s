// Incorrecto. Método estático llama a un método no estático
class A {
    Int x;
}

impl A {
    .() {}
    st fn getXS() {
        (getX());
    }

    fn Int getX() {
        ret x;
    }
}

start {
}
