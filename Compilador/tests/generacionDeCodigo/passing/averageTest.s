// Clase que encapsula operaciones matemáticas sobre un arreglo
class Calculadora {

    pub Array Int datos;
    Int suma;
    Int i;
}

impl Calculadora {

    // Constructor
    .(Array Int d) {
        (IO.out_array_int(d));
        datos = d;
        (99);
        (IO.out_int(datos.length()));
        suma = 0;
        i = 0;
    }

    // Suma todos los elementos del arreglo
    fn Int calcular_suma() {
        suma = 0;
        i = 0;

        while (i < datos.length()) {
            suma = suma + datos[i];
            (++i);
        }

        ret suma;
    }

    // Calcula el promedio (entero)
    fn Int calcular_promedio() {
        Int total;
        total = self.calcular_suma();

        if (datos.length() == 0) {
            ret 0;
        } else {
            ret total div datos.length();
        }
    }

    // Imprime el arreglo
    fn imprimir_datos() {
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

// Programa principal
start {

    Array Int numeros;
    Calculadora calc;
    Int promedio;

    // Creo el arreglo
    numeros = new Int[5];
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;
    numeros[3] = 40;
    numeros[4] = 50;

    // Creo la calculadora
    calc = new Calculadora(numeros);

    // Imprimo los datos
    (calc.imprimir_datos());

    // Calculo el promedio
    promedio = calc.calcular_promedio();

    // Imprimo el resultado final
    (IO.out_str("Promedio = "));
    (IO.out_int(promedio));
    (IO.out_str("\n"));
}
