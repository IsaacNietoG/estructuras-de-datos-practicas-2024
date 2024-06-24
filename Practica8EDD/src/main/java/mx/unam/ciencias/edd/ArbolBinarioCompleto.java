package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola = new Cola<Vertice>();
            if(!esVacia())
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice actual = cola.saca();
            if(actual.hayIzquierdo())
                cola.mete((Vertice)actual.izquierdo());
            if(actual.hayDerecho())
                cola.mete((Vertice)actual.derecho());
            return actual.get();
        }

        private Vertice nextNodo(){
            Vertice actual = cola.saca();
            if(actual.hayIzquierdo())
                cola.mete((Vertice)actual.izquierdo());
            if(actual.hayDerecho())
                cola.mete((Vertice)actual.derecho());
            return actual;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        //El throws base
        if(elemento == null)
            throw new IllegalArgumentException();
        //Caso trivial
        if(esVacia()){
            this.elementos = 1;
            this.raiz = nuevoVertice(elemento);
            return;

        }
        //Ahora si, la chamba
        Vertice nuevo = nuevoVertice(elemento);
        int coordenada = elementos+1;
        Vertice vertice = raiz;

        int[] coordenadaBinaria = new int[32];
        int i;
        for(i = 0; coordenada > 0 ; i++){
            coordenadaBinaria[i] = coordenada & 1;
            coordenada >>= 1;
        }

        for(int j = i-2; j> 0; j--){
            if(coordenadaBinaria[j] == 0){
                vertice = (Vertice)vertice.izquierdo();
            }
            else{
                vertice = (Vertice)vertice.derecho();
            }
        }
        nuevo.padre = vertice;
        if(!vertice.hayIzquierdo())
            vertice.izquierdo = nuevo;
        else
            vertice.derecho = nuevo;

        elementos++;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(elementos == 1 && raiz.elemento.equals(elemento)){
            limpia();
            return;
        }
        if(elementos == 1 && !raiz.get().equals(elemento))
            return;
        Vertice target = vertice(busca(elemento));
        Iterador iterador = (Iterador)this.iterator();
        Vertice ultimo=raiz;
        while(iterador.hasNext()){
            ultimo = iterador.nextNodo();
        }
        target.elemento = ultimo.elemento;
        Vertice ultimoPadre = ultimo.padre;
        if(ultimoPadre.hayDerecho()){
            if(ultimoPadre.derecho.equals(ultimo))
                ultimoPadre.derecho = null;
        }
        else
            ultimoPadre.izquierdo = null;
        elementos--;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(esVacia())
            return -1;
        return (int)(Math.log(elementos)/Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        Cola<Vertice> queue = new Cola<>();
        queue.mete(raiz);
        while(!queue.esVacia()){
            Vertice target = queue.saca();
            accion.actua(target);
            if(target.hayIzquierdo())
                queue.mete(vertice(target.izquierdo()));
            if(target.hayDerecho())
                queue.mete(vertice(target.derecho()));
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
