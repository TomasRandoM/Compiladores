//Correcto: varias clases e impl antes de start
class A {
    Int x;
    pub Str name;
}

class B : A {
    Bool flag;
}

impl A {
    .() {
        x = 0;
        name = "default";
    }

    fn Int getX() {
        ret x;
    }
}

impl B {
    .() {
        flag = true;
    }

    fn Bool isOn() {
        ret flag;
    }
}

start {
    A a;
    B b;
    a = new A();
    b = new B();
    (a.getX());
    (b.isOn());
}
