//Correcto
class A {
    pub Array Int arr;
}
impl A {
    .() {
        arr = new Int[3];
        arr[0] = 10;
        arr[1] = 20;
        arr[2] = 30;
    }
}
start {
    A a;
    a = new A();
    (IO.out_array_int(a.arr));
}