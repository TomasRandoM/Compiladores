//Correcto se llama un metodo estatico desde una instancia
class A {}
impl A {
    .() {}
    st fn Int metodoEstatico() {
        ret 8;
    }

    fn metodoDeInstancia() {
        Int x;
        x = (self.metodoEstatico());
    }
}
start {
    A a;
    a = new A();
    (a.metodoEstatico());
}
