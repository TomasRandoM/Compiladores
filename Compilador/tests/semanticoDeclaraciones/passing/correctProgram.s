//Correcto: esta todo bien

class Persona {
    Str nombre;
    Int edad;
}

// Implementación de Persona
impl Persona {
    .() {}

    fn Str getNombre() {
        ret nombre;
    }

    fn Int getEdad() {
        ret edad;
    }

    fn void setDatos(Str n, Int e) {
        nombre = n;
        edad = e;
    }
}


class Estudiante : Persona {
    Int legajo;
}


impl Estudiante {
    .() {}

    fn Int getLegajo() {
        ret legajo;
    }

    // Redefinición
    fn Str getNombre() {
        Str prefijo;
        prefijo = "Est. ";
        ret prefijo;
    }
}


class Materia {
    Str nombre;
    Int codigo;
}


impl Materia {
    .() {}

    fn Str getNombre() {
        ret nombre;
    }

    fn Int getCodigo() {
        ret codigo;
    }
}

start {
    Persona p;
    Estudiante e;
    Materia m;

    p = new Persona();
    e = new Estudiante();
    m = new Materia();
}
