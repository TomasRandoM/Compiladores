//Imprime 12345
start {
    Str a;
    Str a2;
    Str a3;

    a = "hola";
    a2 = "hola";
    a3 = "chau";

    if (a == a2) {
        (IO.out_int(1));
    }
    if (a == "hola") {
        (IO.out_int(2));
    }
    if (a != "hola") {
        (IO.out_int(4));
    }
    if (a != a2) {
        (IO.out_int(4));
    }
    if (a != a3) {
        (IO.out_int(3));
    }
    if (a != "chau") {
        (IO.out_int(4));
    }
    if ("hola" != "chau") {
        (IO.out_int(5));
    }

}