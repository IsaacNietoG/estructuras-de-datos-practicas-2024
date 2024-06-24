Estructuras de Datos
====================
All the exercises of the course.
------------------
### Nieto Gallegos Isaac Julian
### Professor : Canek Pelaez Valdez

In this repository you can find all the exercises I did during the Estructuras De Datos Course, in my second semester of the Computer Science Degree I am doing in the Faculty of Sciences, UNAM.

### Data Structures Seen

During this course, I implemented in the Java language all the basic data structures seen here:

- AVL Trees
- Complete Binary Trees
- Ordered Binary Trees
- Red-Black Trees
- Arrays (not implemented, but studied)
- Queues
- Stacks
- Dictionaries (with some hash functions)
- Sets
- Graphs
- Linked Lists
- Binary Heaps (two implementations)

### About the course

The course was given by Dr. Canek Pelaez Valdez, during the 2024-2 semester, in the Faculty of Sciences of the National Autonomous University of Mexico.

The course relied heavily on the corresponding [book](https://tienda.fciencias.unam.mx/es/home/437-estructuras-de-datos-con-java-moderno-9786073009157.html) written by the professor; throughout the course, this was the main reference material.

### About the structure of this repository

Each exercise has its own pom.xml file, so you can build the exercise with Maven and Java. In the same fashion, each exercise has its own README.md, written in Spanish, by the professor, with a brief explanation about the exercise and which chapters of the book you could use to do the exercise.

### How to build the exercises
The instructions come in every README.md of each exercise, although they are in spanish, generally this is the procedure.

```
$ mvn compile
...
$ mvn install
...
$ java -jar target/practicaX.jar
```
Where X is the exercise number you want to compile
