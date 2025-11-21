// Define class A attributes
class A {
  Int a;
  pub Str b;
  pub Array Int arrInt;
}

// Implements class A constructor and methods
impl A {
  .(Int a, Int b) {
     self.a = a; 
     self.arrInt = new Int[b];
  }
  fn Int get_a(){
    ret a;
  }
  st fn imprimo_algo(){
    (IO.out_str("hola mundo"));
  }
}

// Class B inherits from class A
class B : A {
  Int c;
}

// Class B implementation
impl B {
  .() { a = 0; b = 1;}
  fn Int get_b(){
    ret b;
  }
  st fn imprimo_b(){
    (IO.out_int(b));
  }
}

/*
Start method. Creates a B instance and tests chained access
*/
start{
  B b;
  Int value;
  b = new B();
  (print("Hello world!"));
  (IO.out_int(b));
  (b.get_a().attribute.random_method().array[4]);
  value = b.get_a();
}