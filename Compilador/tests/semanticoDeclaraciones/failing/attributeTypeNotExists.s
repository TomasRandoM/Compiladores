//Incorrecto: el tipo Pepito del atributo no existe
class A {
    Pepito x;
}

impl A {
    .() {}
}

start {
    A a;
    a = new A();
}
