Estructuras de Datos
====================

Práctica 11: Usando diccionarios
--------------------------------

### Fecha de entrega: martes 4 de junio, 2023

Deben completar los métodos de la clase
[`Conjunto`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11/-/blob/main/src/main/java/mx/unam/ciencias/edd/Conjunto.java).

Además, deben convertir las clases
[`Grafica`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11/-/blob/main/src/main/java/mx/unam/ciencias/edd/Grafica.java),
[`MonticuloMinimo`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11/-/blob/main/src/main/java/mx/unam/ciencias/edd/MonticuloMinimo.java)
y
[`MonticuloArreglo`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11/-/blob/main/src/main/java/mx/unam/ciencias/edd/MonticuloArreglo.java)
a que utilicen diccionarios en lugar de propiedades (colores en el caso de los vértices de las gráficas; índices en el caso de los montículos).

Una vez que hayan terminado sus clases, la práctica debe compilar al hacer:

```
$ mvn compile
```

También debe pasar todas sus pruebas unitarias al hacer:

```
$ mvn test
```

Y por último, debe correr correctamente el programa escrito en la clase
[`Practica11`](https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11/-/blob/main/src/main/java/mx/unam/ciencias/edd/Practica11.java)
al ejecutar:

```
$ mvn install
...
$ java -jar target/practica11.jar N
```

Donde `N` es un número entero.

Los únicos archivos que deben modificar son:

* `ArbolAVL.java`,
* `ArbolBinario.java`,
* `ArbolBinarioCompleto.java`,
* `ArbolBinarioOrdenado.java`,
* `ArbolRojinegro.java`,
* `Arreglos.java`,
* `Cola.java`,
* `Conjunto.java`,
* `Diccionario.java`,
* `Dispersores.java`,
* `Grafica.java`,
* `Lista.java`,
* `MeteSaca.java`,
* `MonticuloArreglo.java`,
* `MonticuloMinimo.java`, y
* `Pila.java`.

*No deben modificar de ninguna manera ninguno de los otros archivos de la
práctica*.

### Repositorio

Pueden clonar la práctica con el siguiente comando:

```
$ git clone https://aztlan.fciencias.unam.mx/gitlab/2024-2-edd/practica11.git
```

### Documentación

La documentación generada por JavaDoc la pueden consultar aquí:

[Documentación generada por JavaDoc para la práctica
11](https://aztlan.fciencias.unam.mx/~canek/2024-2-edd/practica11/apidocs/index.html)

### Capítulos del libro

Los capítulos [del
libro](https://tienda.fciencias.unam.mx/es/home/437-estructuras-de-datos-con-java-moderno-9786073009157.html)
relacionados a esta práctica son:

22. Conjuntos
23. Mejorando gráficas

El libro no cubre cómo mejorar los montículos.
