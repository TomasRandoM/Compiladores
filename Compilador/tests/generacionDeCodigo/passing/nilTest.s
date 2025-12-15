//Testeamos el nil y comparaciones entre referencias. Deberia imprimir "soy nil", "Soy igual a a" y "No soy igual a a"
class A {
    pub Int b;
}

impl A {
    .() {}
}

start {
    A a;
    A b;
    a = new A();
    a.b = 0;
    a = nil;
    if (nil == a) {
        (IO.out_str("soy Nil"));
    }
    else {
        (IO.out_str("No soy nil"));
    }
    b = a;
    (IO.out_str("\n"));
    if (b == a) {
        (IO.out_str("Soy igual a a"));
    }
    else {
        (IO.out_str("No soy igual a a"));
    }
    b = new A();
    (IO.out_str("\n"));
    if (b == a) {
        (IO.out_str("Soy igual a a"));
    }
    else {
        (IO.out_str("No soy igual a a"));
    }
}
