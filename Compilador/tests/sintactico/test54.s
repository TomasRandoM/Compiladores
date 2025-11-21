//Correcto: uso de self con encadenado y array
class Buffer {
    pub Array Int data;
}

impl Buffer {
    .() {
        data = new Int[3];
        data[0] = 1;
        data[1] = 2;
        data[2] = 3;
    }

    fn Int getAt(Int i) {
        ret self.data[i];
    }
}

start {
    Buffer buf;
    Int x;
    buf = new Buffer();
    x = buf.getAt(1);
    (IO.out_int(x));
}
