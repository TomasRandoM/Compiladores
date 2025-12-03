class A {
     pub Array Int arr2;
     //pub Array Bool arr2;
}
impl A {
    .() {}
}

start {
    Int x;
    Double d;
    Bool b;
    Str s;
    Array Int arr;
    A a;

    arr = new Int[5];
    a = new A();
    //x = 1 + true;
    //x = "hola" + 3;
    //x = a.arr2[0] + 1;
    //x = arr +1;
    //x = arr.x + 1;
    //x = true % false;
    //x = 3%4.8;
    //x = 8 div 6;
    //x = 9/8;

    d = 1 + 2.0;
    d = 2.5 - 2;
    d = 3 * 2.5;
    d = 8/5.7;
    //d = 8.8 div 5; //solo int
    //d = 6.8%7;

    //b = 3 < 5;
    //b = 2.5 >= 1;
    //b = a.arr2[6] > arr[1];
    b = 1 < 2.0;

    //b = "hola" < "chau";
    //b = true >= false;
    //b = arr < 5;
    //b = arr[1] > "hola";

    //b = true && false;
    //b = b || (3 < 5);

    //b = 1 && 2;
    //b = "hola" || true;
    //b = arr[7] && b;
    //b = arr == nil;
    //b = arr[1] == s;

    //b = (x + true) > 3;
    //d = (arr == arr[1]);
    //b = (x < "hola");


   }