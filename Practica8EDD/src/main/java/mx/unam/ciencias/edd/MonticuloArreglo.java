package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
        arreglo = nuevoArreglo(n);
        int i=0;
        for(T elem : iterable){
            agrega(i++, elem);
        }

        elementos = i;
    }

    private void agrega(int indice, T elemento){
        arreglo[indice] = elemento;
        elemento.setIndice(indice);
    }

    private void intercambiar(int indice1, int indice2){
        //Obtenemos los elementos
        T elem2 = arreglo[indice2];
        T elem1 = arreglo[indice1];

        //Intercambio de indices
        elem1.setIndice(indice2);
        elem2.setIndice(indice1);

        //Intercambio en el arreglo
        arreglo[indice1] = elem2;
        arreglo[indice2] = elem1;

    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos==0)
            throw new IllegalStateException();


        T minimo = null;

        for (T elemento : arreglo)
            if (minimo == null && elemento != null)
                minimo = elemento;
            else if (elemento != null && elemento.compareTo(minimo) <= 0)
                minimo = elemento;

        arreglo[minimo.getIndice()] = null;
        minimo.setIndice(-1);
        elementos--;

        return minimo;



    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i < 0 || i>= elementos)
            throw new NoSuchElementException();

        return (arreglo[i]);

    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return (elementos == 0);
    }

    /**
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return elementos;
    }
}
