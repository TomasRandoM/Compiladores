//Correcto: herencia + atributo array usado en start
class Vector {
    pub Array Int data;
}

impl Vector {
    .() {
        data = new Int[5];
        data[0] = 1;
        data[1] = 2;
    }

    fn Int first() {
        ret data[0];
    }
}

class ExtendedVector : Vector {
    pub Int extra;
}

impl ExtendedVector {
    .() {
        extra = 10;
    }

    fn Int sumFirstAndExtra() {
        ret data[0] + extra;
    }
}

start {
    ExtendedVector v;
    v = new ExtendedVector();
    (IO.out_int(v.sumFirstAndExtra()));
}
