// Incorrecto: solo se pueden usar operadores relacionales (<, >, >=, <=) con Int o Double.

start {
    Bool b;
    Array Int arr;

    arr = new Int[5];
    b = arr < 5; // error
}
