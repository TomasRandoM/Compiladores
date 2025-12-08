// Correcto. a,b y c tienen el retorno y que es un int. Se testea que el ret esté en diferentes lugares (Bloque principal, if y while)
class A {
    Int x;
    Int y;
}

impl A {
    .() {

    }
    fn Int a() {
        y = nil;
        ret y;
    }

    fn Int b() {
        if (5 == 5) {
            ret y;
        }
    }

    fn Int c() {
        while (5 == 5) {
            ret y;
        }
    }
}

start {
    Int x;
    x = 4;
}
