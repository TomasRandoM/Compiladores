// Deberia imprimir 30
class Calculadora {
    pub Array Int datos;
    Int suma;
    Int i;
}

impl Calculadora {

    .(Array Int d) {
        (IO.out_array_int(d));
        datos = d;
        (IO.out_int(datos.length()));
        suma = 0;
        i = 0;
    }

    fn Int calcularSuma() {
        suma = 0;
        i = 0;

        while (i < datos.length()) {
            suma = suma + datos[i];
            (++i);
        }

        ret suma;
    }

    fn Int calcularPromedio() {
        Int total;
        total = self.calcularSuma();

        if (datos.length() == 0) {
            ret 0;
        } else {
            ret total div datos.length();
        }
    }

    fn imprimirDatos() {
        i = 0;
        (IO.out_array_int(datos));
        (IO.out_int(datos[0]));
        while (i < datos.length()) {
            (IO.out_str("dato["));
            (IO.out_int(i));
            (IO.out_str("] = "));
            (IO.out_int(datos[i]));
            (IO.out_str("\n"));
            (++i);
        }
    }
}

start {

    Array Int numeros;
    Calculadora calc;
    Int promedio;

    numeros = new Int[5];
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;
    numeros[3] = 40;
    numeros[4] = 50;

    calc = new Calculadora(numeros);

    (calc.imprimirDatos());

    promedio = calc.calcularPromedio();

    (IO.out_str("Promedio = "));
    (IO.out_int(promedio));
    (IO.out_str("\n"));
}
