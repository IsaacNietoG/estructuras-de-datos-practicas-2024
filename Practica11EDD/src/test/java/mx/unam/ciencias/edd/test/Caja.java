package mx.unam.ciencias.edd.test;

/**
 * Clase para empacar elementos.
 */
public class Caja<T extends Comparable<T>>
    implements Comparable<Caja<T>> {

    /* El elemento. */
    private T elemento;

    /**
     * Construye una caja con un elemento.
     * @param elemento el elemento.
     */
    public Caja(T elemento) {
        this.elemento = elemento;
    }

    /**
     * Regresa el elemento.
     * @return el elemento.
     */
    public T getElemento() {
        return elemento;
    }

    /**
     * Define el elemento.
     * @param elemento el nuevo elemento.
     */
    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    /**
     * Compara la caja con otra.
     * @param caja la caja con la cual comparar.
     * @return <code>true</code> si ambas cajas tienen el mismo elemento;
     *         <code>false</code> en otro caso.
     */
    @Override public int compareTo(Caja<T> caja) {
        return elemento.compareTo(caja.elemento);
    }
}
