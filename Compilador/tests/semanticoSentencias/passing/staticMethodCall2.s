//Correcto se llama a un metodo estatico desde otro metodo estatico de otra clase
class A {}
class B {
    A a;
    Int c;
}
impl A {
    .() {}
    st fn Int metodoEstatico() {
        ret 8;
    }

    st fn Int metodoEstatico2() {
        ret 5;
    }
}
impl B {
    .() {}
    st fn Int metodoEstaticoB() {
        Int x;
        x = (a.metodoEstatico2());
        ret x;
    }

    fn B metodoEstatico2() {
        B b;
        b = new B();
        ret b;
    }
}

start {
    A a;
    B b;
    Int x;
    a = nil;
    x = (b.metodoEstaticoB());
}
