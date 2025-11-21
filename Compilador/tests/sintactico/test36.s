//Incorrecto. new no tiene la lista de argumentos entre paréntesis
class A { }
impl A {
    .() { }
}
start {
    A a;
    a = new A;
}
