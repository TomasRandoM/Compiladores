# Compiladores
Repositorio del compilador del lenguaje de programación TinyS, desarrollado en la materia de **Compiladores** durante el año **2025**.

## Autores
- Paulina Suden
- Tomás Rando
  
## Descripción  
Se desarrolló un compilador para el lenguaje de programación TinyS. Las especificaciones del mismo se encuentran en el archivo [manual_tinyS](https://github.com/TomasRandoM/Compiladores/blob/main/manual_tinyS.pdf). En él se implementaron todas las etapas (léxico, sintáctico, semántico y generación de código), permitiendo compilar un archivo TinyS (.s) y obtener su código ensamblador (MIPS) en un archivo .asm.

## Tecnología utilizada
Para el desarrollo se utilizó únicamente Java 21, sin ninguna librería, a excepción de JUnit 5, utilizada para pruebas. Además, se utilizó Maven para la gestión de dependencias. 

## Utilización 
Para utilizar el compilador se debe compilar el mismo utilizando Maven. Como resultado, obtendremos el archivo `tinyS.jar`.

Posteriormente, teniendo nuestro archivo fuente `.s`, podremos compilarlo utilizando el siguiente comando:

```bash
java -jar tinyS.jar <ruta_al_codigo_fuente.s>
```

Por ejemplo, este es un comando válido:

```bash
java -jar tinyS.jar ejemplo.s
```

El compilador generará un archivo `.asm` en la misma ruta que el archivo `.s`, conteniendo el código ensamblador generado. Dicho código puede ejecutarse en un simulador MIPS, como por ejemplo MARS. 