package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            if(this.color == Color.NEGRO)
                return "N{" + elemento + "}";
            else
                return "R{" + elemento + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        if(vertice == null){
            return Color.NEGRO;
        }
        VerticeRojinegro casteo = (VerticeRojinegro)vertice;
        return casteo.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vertice = (VerticeRojinegro)ultimoAgregado;
        vertice.color = Color.ROJO;
        rebalancea(vertice);
    }

    private void rebalancea(VerticeRojinegro vertice){
        //Encontramos a toda su familia
        VerticeRojinegro padre = (VerticeRojinegro)vertice.padre;
        VerticeRojinegro hermano;
        VerticeRojinegro tio;
        VerticeRojinegro abuelo;

        //Buscamos a su familia
        if(padre == null){//Si no hay padre entonces tampoco habrá hermano, tío o abuelo
            hermano = null;
            tio = null;
            abuelo = null;
        }else{
            hermano = padre.izquierdo == vertice ? (VerticeRojinegro)padre.derecho : (VerticeRojinegro)padre.izquierdo;
            abuelo = (VerticeRojinegro)padre.padre;
        }
        if(abuelo != null){ //Analogamente, solo si hay abuelo podemos ver si hay tio
            tio = abuelo.izquierdo == padre ? (VerticeRojinegro)abuelo.derecho : (VerticeRojinegro)abuelo.izquierdo;
        }else{
            tio = null;
        }
        //Ahora si a chambear
        //Caso 1
        if(padre == null) {
            vertice.color = Color.NEGRO;
            return;
        }

        //Caso 2
        if(getColor(padre) == Color.NEGRO){
            return;
        }

        //Caso 3
        if(getColor(tio) == Color.ROJO){
            padre.color = Color.NEGRO;
            tio.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalancea(abuelo);
            return;
        }

        //Caso 4 cuando el vértice es hijo izquierdo pero el padre es derecho
        if(padre.izquierdo == vertice && abuelo.derecho == padre){
            super.giraDerecha(padre);
            vertice = padre;
            padre = (VerticeRojinegro)vertice.padre;
        }else if (padre.derecho == vertice && abuelo.izquierdo == padre){ //Caso 4 y 5cuando el vertice es hijo derecho pero el padre es izquierdo
            super.giraIzquierda(padre);
            vertice = padre;
            padre = (VerticeRojinegro)vertice.padre;
        }

        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if(padre.derecho == vertice){
            super.giraIzquierda(abuelo);
        }else{
            super.giraDerecha(abuelo);
        }

    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro eliminar = (VerticeRojinegro)busca(elemento);
        VerticeRojinegro fantasma = null;
        VerticeRojinegro hijo;

        if(eliminar == null) //Guard clause
            return;

        if(eliminar.hayDerecho() && eliminar.hayIzquierdo()){
            eliminar = (VerticeRojinegro)intercambiaEliminable(eliminar); //Intercambiamos a un wey con maximo un hijo
        }

        //Creamos el fantasma si es necesario
        if(!eliminar.hayDerecho() && !eliminar.hayIzquierdo()){
            fantasma = (VerticeRojinegro)nuevoVertice(null);
            fantasma.color = Color.NEGRO;
            fantasma.padre = eliminar;
            eliminar.izquierdo = fantasma;
            hijo = fantasma;
        }else{
            hijo = eliminar.hayIzquierdo()? (VerticeRojinegro)eliminar.izquierdo : (VerticeRojinegro)eliminar.derecho();
        }

        eliminaVertice(eliminar);

        //Verificamos si hay que rebalancear o no
        if(getColor(hijo) == Color.ROJO || getColor(eliminar) == Color.ROJO){
            hijo.color = Color.NEGRO;
        }else{
            rebalanceaElimina(hijo);
        }

        if(fantasma != null)
            eliminaVertice(fantasma);

        elementos--;
    }

    private void rebalanceaElimina(VerticeRojinegro vertice){
        //Encontramos a toda su familia
        VerticeRojinegro padre = (VerticeRojinegro)vertice.padre;
        VerticeRojinegro hermano;
        VerticeRojinegro abuelo;
        VerticeRojinegro sobrinoCruzado;
        VerticeRojinegro sobrinoNoCruzado;

        //Buscamos a su familia
        if(padre == null){//Si no hay padre entonces tampoco habrá hermano o abuelo
            hermano = null;
            abuelo = null;
        }else{
            hermano = padre.izquierdo == vertice ? (VerticeRojinegro)padre.derecho : (VerticeRojinegro)padre.izquierdo;
            abuelo = (VerticeRojinegro)padre.padre;
        }
        if(hermano != null){ //Y si hay hermano podemos buscar los sobrinos
            sobrinoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.derecho : (VerticeRojinegro)hermano.izquierdo;
            sobrinoNoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.izquierdo : (VerticeRojinegro)hermano.derecho;
        }else{
            sobrinoCruzado = null;
            sobrinoNoCruzado = null;
        }

        //Ahora si, a chambear
        if(padre == null) //Caso 1
            return;

        if(getColor(hermano) == Color.ROJO){ //Caso 2, no se corta.
            padre.color = Color.ROJO;
            hermano.color = Color.NEGRO;
            if(padre.izquierdo == vertice)
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
            padre = (VerticeRojinegro)vertice.padre;
            hermano = padre.izquierdo == vertice ? (VerticeRojinegro)padre.derecho : (VerticeRojinegro)padre.izquierdo; //Act. referencia
            sobrinoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.derecho : (VerticeRojinegro)hermano.izquierdo;
            sobrinoNoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.izquierdo : (VerticeRojinegro)hermano.derecho;
        }

        if(getColor(padre) == Color.NEGRO && getColor(hermano) == Color.NEGRO && getColor(sobrinoCruzado) == Color.NEGRO &&
           getColor(sobrinoNoCruzado) == Color.NEGRO){ //Caso 3
            hermano.color = Color.ROJO;
            rebalanceaElimina(padre);
            return;
        }

        if(getColor(padre) == Color.ROJO && getColor(hermano) == Color.NEGRO && getColor(sobrinoCruzado) == Color.NEGRO &&
           getColor(sobrinoNoCruzado) == Color.NEGRO){ //Caso 4
            hermano.color = Color.ROJO;
            padre.color = Color.NEGRO;
            return;
        }

        if(getColor(sobrinoCruzado) == Color.NEGRO && getColor(sobrinoNoCruzado) == Color.ROJO){ //Caso 5
            hermano.color = Color.ROJO;
            sobrinoNoCruzado.color = Color.NEGRO;
            if(padre.izquierdo == vertice){
                super.giraDerecha(hermano);
            }else{
                super.giraIzquierda(hermano);
            }
        }

        //Actualizamos referencias
        hermano = padre.izquierdo == vertice ? (VerticeRojinegro)padre.derecho : (VerticeRojinegro)padre.izquierdo;
        sobrinoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.derecho : (VerticeRojinegro)hermano.izquierdo;
        sobrinoNoCruzado = padre.izquierdo == vertice ? (VerticeRojinegro)hermano.izquierdo : (VerticeRojinegro)hermano.derecho;

        //Caso 6
        hermano.color = getColor(padre);
        sobrinoCruzado.color = Color.NEGRO;
        padre.color = Color.NEGRO;
        if(padre.izquierdo == vertice){
            super.giraIzquierda(padre);
        }else{
            super.giraDerecha(padre);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
