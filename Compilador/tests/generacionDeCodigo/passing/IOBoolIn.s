start {
    Bool b;
    Str s;
    s = "BOOOL";
    (IO.out_str(s));

    //Esto de abajo ya funciona
    b = (IO.in_bool());
    (IO.out_bool(b));
}