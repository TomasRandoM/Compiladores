//B no puede acceder directamente a a (de A), pero sí puede hacerlo a través de los métodos heredados desde A. Se imprime 05
class A {
    Int a;
}

impl A {
    .() {
    }

    fn void setA() {
        a = 5;
    }

    fn Int getA() {
        ret a;
    }
}

class B : A {

}

impl B {
    .() {
    }
}

start {
    B b;
    b = new B();
    (IO.out_int(b.getA()));
    (b.setA());
    (IO.out_int(b.getA()));
}