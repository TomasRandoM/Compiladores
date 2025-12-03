//Es correcto porque se accede a un array publico
class A {
    pub Array Int arr;
    Int b;
}

impl A {
    .() {}
    fn Int getValue(Int i) {
        ret arr[i];
    }
}

start {
    A a;
    Int b;
    a = new A();
    a.arr = new Int[5];
    b = (a.arr[5]);
}