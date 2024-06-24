package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila = new Pila<>();
            if (!esVacia()) {
                Vertice vertice = raiz;
                while (vertice != null) {
                    pila.mete(vertice);
                    vertice = vertice.izquierdo;
                }
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice v = pila.saca();
            if (v.derecho != null){
                pila.mete(v.derecho);
                Vertice actual = v.derecho;
                actual = actual.izquierdo;
                while (actual != null){
                    pila.mete(actual);
                    actual = actual.izquierdo;
                }
            }
            return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        Vertice nuevo = nuevoVertice(elemento);
        if(esVacia())
            raiz = nuevo;
        else
            compara(this.raiz, nuevo);

        elementos++;
        ultimoAgregado = nuevo;

    }

    private void compara(Vertice actual, Vertice insertable){
        if(insertable.elemento.compareTo(actual.elemento) <=0){
            if(!actual.hayIzquierdo()){
                actual.izquierdo = insertable;
                insertable.padre = actual;
            } else
                compara(actual.izquierdo, insertable);

        }else{
            if(!actual.hayDerecho()){
                actual.derecho = insertable;
                insertable.padre = actual;
            } else
                compara(actual.derecho, insertable);
        }
    }
    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(!contiene(elemento))
            return;

        Vertice eliminar = vertice(busca(elemento));
        if(eliminar.hayDerecho()&& eliminar.hayIzquierdo()){
            Vertice intercambiar = intercambiaEliminable(eliminar);
            eliminaVertice(intercambiar);
        }else{
            eliminaVertice(eliminar);
        }
        elementos--;
    }

    protected Vertice maximoEnSubarbol(Vertice vertice){
        if(vertice.derecho == null)
            return vertice;
        return maximoEnSubarbol(vertice.derecho);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice maximo = maximoEnSubarbol(vertice.izquierdo);
        T jaja = vertice.elemento;
        vertice.elemento = maximo.elemento;
        maximo.elemento = jaja;
        return maximo;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice hijo = (vertice.hayIzquierdo()? vertice.izquierdo : vertice.derecho);
        if(!vertice.hayPadre()){
            raiz = hijo;
        }else{
            if(vertice.padre.hayIzquierdo() && vertice.padre.izquierdo == vertice)
               vertice.padre.izquierdo = hijo;
            else
                vertice.padre.derecho = hijo;
        }

        if(hijo != null){
            hijo.padre = vertice.padre;
        }

    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz, elemento);
    }

    private VerticeArbolBinario<T> busca(Vertice target, T elemento){
        if(target == null){
            return null;
        }
        if(target.get().compareTo(elemento) == 0)
            return target;
        if(elemento.compareTo(target.get())< 0)
            return busca(target.izquierdo, elemento);
        else
            return busca(target.derecho , elemento);

    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if(vertice == null || !vertice.hayIzquierdo())
            return;

        Vertice casteado = vertice(vertice);
        Vertice nuevoPadre = vertice(vertice.izquierdo());
        casteado.izquierdo = nuevoPadre.derecho;

        if(nuevoPadre.hayDerecho()){
            nuevoPadre.derecho.padre = casteado;
        }
        nuevoPadre.derecho = casteado;
        nuevoPadre.padre = casteado.padre;
        casteado.padre = nuevoPadre;

        if(nuevoPadre.hayPadre()){

            if(nuevoPadre.padre.hayIzquierdo() && nuevoPadre.padre.izquierdo == casteado)
                nuevoPadre.padre.izquierdo = nuevoPadre;
            else if (nuevoPadre.padre.hayDerecho() && nuevoPadre.padre.derecho == casteado)
                nuevoPadre.padre.derecho = nuevoPadre;
        }else
            this.raiz = nuevoPadre;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(vertice == null || !vertice.hayDerecho())
            return;

        Vertice nuevoPadre = vertice(vertice.derecho());
        Vertice casteado = vertice(vertice);
        casteado.derecho = nuevoPadre.izquierdo;

        if(nuevoPadre.hayIzquierdo())
            nuevoPadre.izquierdo.padre = casteado;

        nuevoPadre.izquierdo = casteado;
        nuevoPadre.padre = casteado.padre;
        casteado.padre = nuevoPadre;

        if(nuevoPadre.hayPadre()){
            if(nuevoPadre.padre.hayIzquierdo() && nuevoPadre.padre.izquierdo == casteado)
                nuevoPadre.padre.izquierdo = nuevoPadre;
            else if (nuevoPadre.padre.hayDerecho() && nuevoPadre.padre.derecho == casteado)
                nuevoPadre.padre.derecho = nuevoPadre;
        }else{
            this.raiz = nuevoPadre;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(raiz, accion);
    }

    private void dfsPreOrder(VerticeArbolBinario<T> vertice, AccionVerticeArbolBinario<T> accion){
        if (vertice == null)
            return;
        accion.actua(vertice);
        if (vertice.hayIzquierdo())
            dfsPreOrder(vertice.izquierdo(), accion);
        if (vertice.hayDerecho())
            dfsPreOrder(vertice.derecho(), accion);

    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(raiz, accion);
    }

    private void dfsInOrder(VerticeArbolBinario<T> vertice, AccionVerticeArbolBinario<T> accion){
        if (vertice == null)
            return;
        if (vertice.hayIzquierdo())
            dfsInOrder(vertice.izquierdo(), accion);
        accion.actua(vertice);
        if (vertice.hayDerecho())
            dfsInOrder(vertice.derecho(), accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(this.raiz(), accion);
    }

    private void dfsPostOrder(VerticeArbolBinario<T> vertice, AccionVerticeArbolBinario<T> accion){
        if (vertice == null)
            return;
        if (vertice.hayIzquierdo())
            dfsPostOrder(vertice.izquierdo(), accion);
        if (vertice.hayDerecho())
            dfsPostOrder(vertice.derecho(), accion);
        accion.actua(vertice);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
