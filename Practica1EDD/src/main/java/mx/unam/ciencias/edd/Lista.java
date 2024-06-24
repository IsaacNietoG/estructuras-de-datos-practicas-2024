package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(!hasNext()){
                throw new NoSuchElementException("No hay siguiente");
            }
            T tmp = siguiente.elemento;
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return tmp;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(!hasPrevious()){
                throw new NoSuchElementException("No hay previo");
            }
            T tmp = anterior.elemento;
            siguiente = anterior;
            anterior = anterior.anterior;
            return tmp;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (cabeza == null && rabo == null);
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("Elemento invalida");
        }
        Nodo nodo = new Nodo(elemento);
        if(esVacia()){
            cabeza = nodo;
            rabo = nodo;
            longitud = 1;
            return;
        }
        rabo.siguiente = nodo;
        nodo.anterior = rabo;
        rabo = nodo;
        longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("Elemento invalido");
        }
        Nodo nodo = new Nodo(elemento);
        if(esVacia()){
            cabeza = nodo;
            rabo = cabeza;
            longitud = 1;
            return;
        }
        cabeza.anterior = nodo;
        nodo.siguiente = cabeza;
        cabeza = nodo;
        longitud++;

    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("Elemento invalido");
        }
        if(i <= 0){
            agregaInicio(elemento);
            return;
        }
        if(i >= longitud){
            agregaFinal(elemento);
            return;
        }
        Iterador iterador = new Iterador();
        for(int n = 0; n<i; n++){
            iterador.next();
        }
        Nodo nodo = new Nodo(elemento);
        Nodo temp = iterador.siguiente;
        nodo.anterior = temp.anterior;
        nodo.siguiente = temp;
        temp.anterior.siguiente = nodo;
        temp.anterior = nodo;
        longitud++;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (esVacia()){
            return;
        }
        if (cabeza.elemento.equals(elemento)) {
            eliminaPrimero();
            return;
        }
        Iterador iterador = new Iterador();
        while(iterador.hasNext()){
            if(iterador.siguiente == rabo && iterador.siguiente.elemento.equals(elemento)){
                eliminaUltimo();
                return;
            }
            if(iterador.siguiente.elemento.equals(elemento)){
                Nodo anterior = iterador.anterior;
                Nodo siguiente = iterador.siguiente.siguiente;
                anterior.siguiente = siguiente;
                siguiente.anterior = anterior;
                longitud--;
                return;
            }
            iterador.next();
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(esVacia()){
            throw new NoSuchElementException("La lista está vacía");
        }
        T cabezaPrevia = cabeza.elemento;
        if(cabeza.siguiente == null){
            limpia();
            return cabezaPrevia;
        }
        Nodo tmp = cabeza.siguiente;
        tmp.anterior = null;
        cabeza = tmp;
        longitud--;
        return cabezaPrevia;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(esVacia()){
            throw new NoSuchElementException("La lista esta vacia");
        }
        T raboPrevio = rabo.elemento;
        if(rabo.anterior == null){
            limpia();
            return raboPrevio;
        }
        rabo = rabo.anterior;
        rabo.siguiente = null;
        longitud--;
        return raboPrevio;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Iterador iterador = new Iterador();
        while(iterador.hasNext()){
            if(iterador.next().equals(elemento)){
                return true;
            }
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> resultado = new Lista<>();
        Iterador copiador = new Iterador();
        copiador.end();
        while(copiador.hasPrevious()){
            resultado.agrega(copiador.previous());
        }
        return resultado;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> resultado = new Lista<>();
        Iterador copiador = new Iterador();
        while(copiador.hasNext()){
            resultado.agrega(copiador.next());
        }
        return resultado;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        this.cabeza = null;
        this.rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(cabeza == null){
            throw new NoSuchElementException("La lista esta vacia");
        }
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista esta vacia");
        }
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i<0 || i>= longitud){
            throw new ExcepcionIndiceInvalido("El indice proporcionado no es válido");
        }
        Iterador iterador = new Iterador();
        T objeto = null;
        for(int n = 0; n<=i; n++){
            objeto = iterador.next();
        }
        return objeto;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Iterador iterador = new Iterador();
        for(int i = 0; i<longitud; i++){
            if(iterador.next().equals(elemento)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if(esVacia()){
            return "[]";
        }
        String string = "";
        string += "[";
        Iterador iterador = new Iterador();
        while(iterador.hasNext() && iterador.siguiente != rabo){
            string += iterador.next();
            string += ", ";
        }
        string += rabo.elemento;
        string += "]";
        return string;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(lista.getLongitud()!= this.getLongitud()){
            return false;
        }
        Iterador iteradorInt = new Iterador();
        Iterator<T> iteradorExt = lista.iterator();
        for(int i = 0; i<this.getLongitud();i++){
            if(!iteradorInt.next().equals(iteradorExt.next())){
                return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}
