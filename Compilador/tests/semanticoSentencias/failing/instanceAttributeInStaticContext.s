// Incorrecto. Se accede a un atributo de instancia dentro de un metodo estatico en la linea 8
class A {
    Int x;
}

impl A {
    .() {
    }
    st fn a() {
        x = 10;
    }
}

start {
}