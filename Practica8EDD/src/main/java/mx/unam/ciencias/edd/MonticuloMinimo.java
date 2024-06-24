package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < elementos);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(indice >= elementos)
                throw new NoSuchElementException();

            return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        elementos = 0;
        arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
        int i=0;
        for(T elem : iterable){
            agrega(i++, elem);
        }

        elementos = i;

        for(int j = n/2 ; j >= 0; j--){
            monticulearAbajo(j);
        }
    }

    private void agrega(int indice, T elemento){
        arbol[indice] = elemento;
        elemento.setIndice(indice);
    }

    private void monticulearAbajo(int indice){
        int indiceIzq = 2*indice + 1;
        int indiceDer = 2* indice + 2;
        int indiceMenor = indice;

        if(indiceIzq < elementos && arbol[indiceIzq].compareTo(arbol[indiceMenor]) < 0)
            indiceMenor = indiceIzq;
        if(indiceDer < elementos && arbol[indiceDer].compareTo(arbol[indiceMenor]) < 0)
            indiceMenor = indiceDer;

        if(indiceMenor != indice){
            intercambiar(indice, indiceMenor);
            monticulearAbajo(indiceMenor);
        }


    }

    private void monticulearArriba(int indice){
        int padre = (indice -1) /2;

        if(indice > 0 && arbol[indice].compareTo(arbol[padre]) < 0){
            intercambiar(indice, padre);
            monticulearArriba(padre);
        }
    }

    private void intercambiar(int indice1, int indice2){
        //Obtenemos los elementos
        T elem2 = arbol[indice2];
        T elem1 = arbol[indice1];

        //Intercambio de indices
        elem1.setIndice(indice2);
        elem2.setIndice(indice1);

        //Intercambio en el arreglo
        arbol[indice1] = elem2;
        arbol[indice2] = elem1;

    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(arbol.length == elementos){
            T[] expansion = nuevoArreglo(elementos * 2);
            int i = 0;
            for(T previoT : arbol){
                expansion[i++] = previoT;
            }
            arbol = expansion;
        }

        agrega(elementos, elemento);
        elementos++;
        monticulearArriba(elementos-1);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos == 0)
            throw new IllegalStateException();

        T retorno = arbol[0];
        intercambiar(0, elementos-1);

        arbol[elementos-1].setIndice(-1);

        elementos--;
        monticulearAbajo(0);
        return retorno;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int indiceEliminar = elemento.getIndice();
        if(indiceEliminar < 0 || indiceEliminar >= elementos)
            return;

        intercambiar(indiceEliminar, elementos-1);
        arbol[elementos-1].setIndice(-1);
        elementos--;

        if(indiceEliminar < elementos)
            reordena(arbol[indiceEliminar]);

    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int indice = elemento.getIndice();
        if(indice < 0)
            return false;
        //Vamos a ver si puedo mejorar mis expresiones booleanas erizas jaja
        boolean mayora0 = indice >= 0;
        boolean menorAlTamanio = indice < elementos;
        boolean siEsElemento = arbol[indice].equals(elemento);

        return (mayora0 && menorAlTamanio && siEsElemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos ==0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for(int i = 0; i<elementos; i++){
            arbol[i] = null;
        }
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        int indice = elemento.getIndice();
        monticulearAbajo(indice);
        monticulearArriba(indice);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i < 0 || i >= elementos)
            throw new NoSuchElementException();

        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String toString = "";

        for (T elemento : arbol)
            toString += elemento.toString() + ", ";

        return toString;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;

        if (monticulo.elementos != elementos)
            return false;

        for (int i = 0; i < elementos; i++)
            if (!arbol[i].equals(monticulo.arbol[i]))
                return false;

        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> entrada = new Lista<>();
        Lista<T> salida = new Lista<>();

        for (T elemento : coleccion)
            entrada.agrega(new Adaptador<T>(elemento));

        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<>(entrada);

        while (!monticulo.esVacia()) {
            Adaptador<T> eliminado = monticulo.elimina();
            salida.agrega(eliminado.elemento);
        }

        return salida;
    }
}
