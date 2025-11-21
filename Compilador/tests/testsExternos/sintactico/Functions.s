
class A {
    Int counter;
}

impl A {

    .(Int counter)
    {
        self.counter = counter;
    }

    fn void increment_counter()
    {
        counter = count + 1;
    }

    fn Int increment_and_get_count()
    {
        (self.increment_counter());
        ret counter;
    }

    fn A increment_and_get_new_counter()
    {
        (increment_counter());
        ret new A(self.counter);
    }

    st fn A get_counter()
    {
        ret new A(0);
    }
}
start
{

    Int n;
    A a;

    a = A.get_counter();
    (a.increment_counter());

    n = a.increment_and_get_new_counter()
         .increment_and_get_new_counter()
         .count;

}