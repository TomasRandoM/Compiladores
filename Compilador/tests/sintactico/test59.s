//Correcto: return con expresión y encadenado
class C {
    Int x;
}

impl C {
    .() { x = 10; }

    fn Int getX() {
        ret x;
    }
}

start {
    C c;
    Int y;
    c = new C();
    y = (c.getX() + (Int) 5.5) * 2;
}
