// Se ejecuta el factorial de 6. Se prueba recursividad. Imprime 720
class Fact {}
impl Fact {
    .() {}
    fn Int fact(Int n) {
        if (n == 0) {
            ret 1;
        }
        (IO.out_int(n));
        (IO.out_str("\n"));
        ret (n * fact(n - 1));
    }
}
start {
    Fact f;
    f = new Fact();
    (IO.out_int(f.fact(6)));   // 720
}
