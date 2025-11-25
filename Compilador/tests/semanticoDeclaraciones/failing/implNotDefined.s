//Incorrecto. La clase A no posee class

impl A {
    .() {
        x = 1;}
    fn Int getX() {
        ret x;
    }}

start {
    A a;
    a = new A();
    (a.getX());
}
