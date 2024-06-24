package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        protected T elemento;
        /** El padre del vértice. */
        protected Vertice padre;
        /** El izquierdo del vértice. */
        protected Vertice izquierdo;
        /** El derecho del vértice. */
        protected Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        protected Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            return (this.padre != null);
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return this.izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return this.derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if(!hayPadre())
                throw new NoSuchElementException("No hay padre");
            return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if(!hayIzquierdo())
                throw new NoSuchElementException("No hay izquierdo");
            return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if(!hayDerecho())
                throw new NoSuchElementException("No hay derecho");
            return derecho;
        }

        private int max(int uno, int dos){
            if(uno > dos)
                return uno;
            return dos;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return 1+ max(hayIzquierdo()?  izquierdo.altura(): -1,
                          hayDerecho()? derecho.altura() : -1);
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return (hayPadre()? 1 + padre.profundidad() : 0);
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return this.elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            if(this.elemento != vertice.get()) //Los elementos no son iguales
                return false;
            if(this.hayDerecho() != vertice.hayDerecho()) //Uno tiene derecho y el otro no
                return false;
            if(this.hayIzquierdo() != vertice.hayIzquierdo()) //Uno tiene izquierod y el otro no
                return false;
            if(this.hayIzquierdo() && vertice.hayIzquierdo())
                if(!this.izquierdo.equals(vertice.izquierdo())) //El izquierdo no es igual al otro izquierdo
                    return false;
            if(this.hayDerecho() && vertice.hayDerecho())
                if(!this.derecho.equals(vertice.derecho())) //El derecho no es igual al otro derecho
                    return false;
            return true; //Todo fine
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        @Override public String toString() {
            return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        Iterator<T> iterador = coleccion.iterator();
        while (iterador.hasNext())
            this.agrega(iterador.next());
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if(esVacia())
            return -1;
        return raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return (busca(elemento) != null);
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz, elemento);
    }

    private VerticeArbolBinario<T> busca(Vertice nodo, T elemento) {
        if(nodo == null)
            return null;
        if(nodo.get().equals(elemento))
            return nodo;
        VerticeArbolBinario<T> izquierdo= busca(nodo.izquierdo,elemento);
        VerticeArbolBinario<T> derecho= busca(nodo.derecho,elemento);

        if(izquierdo != null)
            return izquierdo;
        return derecho;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if(raiz == null)
            throw new NoSuchElementException();
        return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (raiz == null);
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        if(this.esVacia() && arbol.esVacia())
            return true;
        if(this.esVacia() != arbol.esVacia())
            return false;
        return this.raiz.equals(arbol.raiz());
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if(esVacia())
            return "";
        int[]array = new int[altura()+1];
        for(int i = 0; i<array.length; i++){
            array[i] = 0;
        }
        return toString((Vertice)raiz,0, array);
    }

    public String toString(Vertice vertice, int nivel, int[] array){
        String s = vertice.toString() + "\n";
        array[nivel] = 1;
        if(vertice.hayIzquierdo() && vertice.hayDerecho()){
            s += dibujaEspacios(nivel, array);
            s += "├─›";
            s += this.toString((Vertice)vertice.izquierdo(), nivel+1, array);
            s += dibujaEspacios(nivel, array);
            s += "└─»";
            array[nivel] = 0;
            s += toString((Vertice)vertice.derecho(), nivel+1, array);
        }else if(vertice.hayIzquierdo()){
            s += dibujaEspacios(nivel, array);
            s += "└─›";
            array[nivel] = 0;
            s += toString((Vertice)vertice.izquierdo(), nivel+1, array);
        }
        else if(vertice.hayDerecho()){
            s += dibujaEspacios(nivel, array);
            s += "└─»";
            array[nivel] = 0;
            s += toString((Vertice)vertice.derecho(), nivel + 1, array);
        }
        return s;
    }

    public String dibujaEspacios(int l, int[] array){
        String s = "";
        for(int i = 0; i<l; i++){
            if(array[i] == 1)
                s += "│  ";
            else
                s += "   ";
        }
        return s;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
