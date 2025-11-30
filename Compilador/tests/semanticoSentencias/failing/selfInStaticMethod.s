//Incorrecto. El método estático posee self
class A {
    Int x;
}

impl A {
    .() {
        x = 1;
    }
    st fn Int getC() {
        self.x = 3;
        ret 2;
    }
    fn Int getA() {
        ret 2;
    }
    fn Int getB() {
        ret 2;
    }
}

start {
}
