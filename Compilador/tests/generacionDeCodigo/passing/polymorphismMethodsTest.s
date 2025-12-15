//Imprime el area dependiendo de la clase declarada. Se pueba el polimorifsmo y redefinicion de metodos. Imprime 12.56 y 9.0
class Figura {
    pub Double base;
}

impl Figura {
    .(Double b) {
        base = b;
    }

    fn Double area() {
        ret 0.0;
    }
}

class Circulo : Figura {
}

impl Circulo {
    .(Double r) {
        base = r;
    }

    fn Double area() {
        ret 3.14 * base * base;
    }
}

class Cuadrado : Figura {
}

impl Cuadrado {
    .(Double l) {
        base = l;
    }

    fn Double area() {
        ret base * base;
    }
}

start {
    Figura f;

    f = new Circulo(2.0);
    (IO.out_double(f.area()));   //12.56
    (IO.out_str("\n"));
    f = new Cuadrado(3.0);
    (IO.out_double(f.area()));   //9.0
}
