package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        String retorno = "";
        Nodo actual = cabeza;
        while(actual != null){
            retorno += actual.elemento + ",";
            actual = actual.siguiente;
        }
        return retorno;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento invalido");
        Nodo nuevo = new Nodo(elemento);
        if (rabo == null){
            cabeza = nuevo;
            rabo = nuevo;
            return;
        }
        rabo.siguiente = nuevo;
        rabo = nuevo;
    }
}
