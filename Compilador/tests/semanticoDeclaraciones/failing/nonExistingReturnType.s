//Incorrecto: el tipo de retorno Pepito no existe

class A {}

impl A {
    .() {}

    fn Pepito f() {
        ret 3;
    }
}

start {
    A a;
    a = new A();
}
