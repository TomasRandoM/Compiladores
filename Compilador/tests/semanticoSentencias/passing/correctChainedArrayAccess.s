//Es correcto porque se accede a un array publico
class A {
    pub Array Int arr;
}

impl A {
    .() {}
    fn Int getValue(Int i) {
        ret arr[i];
    }
}

start {
    A a;
    a = new A();
    a.arr = new Int[5];
}