package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La lista de vecinos del vértice. */
        private Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(contiene(elemento) || elemento == null)
            throw new IllegalArgumentException();
        vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice vertA = (Vertice)vertice(a);
        Vertice vertB = (Vertice)vertice(b);

        if(vertA.vecinos.contiene(vertB) || vertA == vertB){
            throw new IllegalArgumentException();
        }
        vertA.vecinos.agrega(vertB);
        vertB.vecinos.agrega(vertA);
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice vertA = (Vertice)vertice(a);
        Vertice vertB = (Vertice)vertice(b);

        if(!vertA.vecinos.contiene(vertB)){
            throw new IllegalArgumentException();
        }

        vertA.vecinos.elimina(vertB);
        vertB.vecinos.elimina(vertA);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        try {
            VerticeGrafica<T> v = vertice(elemento);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminado = (Vertice)vertice(elemento);
        Lista<Vertice> vecinos = eliminado.vecinos;
        for(Vertice vecinosa : vecinos){
            vecinosa.vecinos.elimina(eliminado);
            aristas--;
        }
        vertices.elimina(eliminado);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        return (((Vertice)vertice(a)).vecinos.contiene((Vertice)vertice(b)));
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        VerticeGrafica<T> result = null;
        for(Vertice v : vertices){
            if(v.elemento.equals(elemento))
                result = v;
        }
        if(result==null)
            throw new NoSuchElementException();
        return result;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        Vertice local = (Vertice)vertice(vertice.get());
        if(local != vertice)
            throw new IllegalArgumentException();
        local.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        boolean conexa = true;
        paraCadaVertice((v) -> {((Vertice)v).color = Color.NEGRO;});
        Pila<Vertice> pila = new Pila<>();
        pila.mete(vertices.getPrimero());
        while(!pila.esVacia()){
            Vertice target = pila.saca();
            target.color = Color.ROJO;
            for(Vertice vecino : target.vecinos)
                if(vecino.color == Color.NEGRO)
                    pila.mete(vecino);
        }

        for(Vertice v : vertices)
            if(v.color == Color.NEGRO)
                conexa = false;

        paraCadaVertice((v) -> {((Vertice)v).color = Color.NINGUNO;});
        return conexa;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vert : vertices){
            accion.actua(vert);
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Cola<Vertice> struct = new Cola<>();
        firstSearch(elemento, accion, struct);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, omenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Pila<Vertice> struct = new Pila<>();
        firstSearch(elemento, accion, struct);
    }

    private void firstSearch(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> struct){
        paraCadaVertice((v) -> {((Vertice)v).color = Color.ROJO;});
        Vertice vElemento = (Vertice) (vertice(elemento));
        vElemento.color = Color.NEGRO;
        struct.mete(vElemento);
        while(!struct.esVacia()){
            Vertice actual = struct.saca();
            accion.actua(actual);

            for(Vertice vecino : actual.vecinos)
                if((vecino.color == Color.ROJO)){
                    vecino.color = Color.NEGRO;
                    struct.mete(vecino);
                }

        }
        paraCadaVertice((v) -> {((Vertice)v).color = Color.NINGUNO;});
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return (vertices.esVacia());
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String toString = "{";

        //Lista de vertices
        for(Vertice v : vertices)
            toString += v.elemento + ", ";

        toString += "}, {";

        //Lista de aristas
         Lista<T> verticesPasados = new Lista<>();
        for (Vertice vertice : vertices) {
            for (Vertice vecino : vertice.vecinos)
                if (!verticesPasados.contiene(vecino.elemento))
                    toString += "(" + vertice.elemento.toString() + ", " + vecino.elemento.toString() + "), ";

                    verticesPasados.agrega(vertice.elemento);
        }

        toString += "}";
        return toString;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;

        //Comparacion de cardenalidades de los conjuntos
        if(aristas != grafica.aristas || vertices.getElementos() != grafica.vertices.getElementos())
            return false;

        //Comenzamos a recorrer la lista de vertices.
        for(Vertice v : vertices){

            Vertice v2;
            //Si no encontramos su equivalente, acabamos
            try{
                v2 = (Vertice)grafica.vertice(v.elemento);
            }catch(NoSuchElementException e){
                return false;
            }

            //Si encontramos su equivalente, verificamos sus vecinos
            for(Vertice vecino : v.vecinos){
                boolean contiene = false;

                //Buscamos cada vecino en su equivalente.
                for(Vertice veci2 : v2.vecinos)
                    if(vecino.elemento.equals(veci2.elemento)){
                        contiene = true;
                        break;
                    }

                if(!contiene)
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
