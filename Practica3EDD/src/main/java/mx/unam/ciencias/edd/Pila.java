package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        String resultado = "";
        if(esVacia()){
            return resultado;
        }
        Nodo actual = cabeza;
        while(actual != null){
            resultado += actual.elemento + "\n";
            actual = actual.siguiente;
        }
        return resultado;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("Elemento invalido");
        }
        Nodo nuevo = new Nodo(elemento);
        if(cabeza == null){
            cabeza = nuevo;
            rabo = nuevo;
            return;
        }
        nuevo.siguiente = cabeza;
        cabeza = nuevo;

    }
}
