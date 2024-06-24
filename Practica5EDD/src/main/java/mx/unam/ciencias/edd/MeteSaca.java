package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase interna protegida para nodos.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        if(esVacia()){
            throw new NoSuchElementException("Esta estructura esta vacia");
        }
        T retorno = cabeza.elemento;
        if(cabeza.siguiente == null){
            cabeza = null;
            rabo = null;
            return retorno;
        }
        cabeza = cabeza.siguiente;
        return retorno;
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        if (esVacia()) {
            throw new NoSuchElementException("Esta estructura esta vacia");
        }
        return cabeza.elemento;
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <code>true</code> si la estructura no tiene elementos,
     *         <code>false</code> en otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Compara la estructura con un objeto.
     * @param object el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)object;
        if(m.esVacia() && this.esVacia())
            return true;
        if((m.esVacia() && !this.esVacia()) || (this.esVacia() && !m.esVacia())){
            return false;
        }
        Nodo nodoInt = this.cabeza;
        Nodo nodoExt = m.cabeza;
        while(nodoInt != null || nodoExt != null){
            // Cuando un elemento no es igual a su equivalente en la otra estructura, cortocircuitamos a que las structs no son
            // iguales
            if(!nodoInt.elemento.equals(nodoExt.elemento)){
                return false;
            }
            // Cuando una de las dos estructuras se puede seguir recorriendo y la otra no,
            // entonces son de distinta longitud asi que
            // cortocircuitamos a que no son iguales.
            if ((nodoInt.siguiente == null && nodoExt.siguiente != null)
                    || (nodoExt.siguiente == null && nodoInt.siguiente != null)) {
                return false;
            }
            //Iteramos al siguiente
            nodoInt = nodoInt.siguiente;
            nodoExt = nodoExt.siguiente;
        }
        // Si el ciclo terminó, no ocurrió ninguno de los criterios que hacen distintas dos estructuras (longitud o elementos)
        // entonces ambas son iguales
        return true;
    }
}
