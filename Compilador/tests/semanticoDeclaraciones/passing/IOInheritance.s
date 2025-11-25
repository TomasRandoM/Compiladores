class X : IO {
    Int v;
}

impl X {
    .() {}
}

start {
    X x;
    x = new X();
}
