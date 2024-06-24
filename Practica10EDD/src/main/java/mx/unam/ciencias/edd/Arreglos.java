package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSortRecursivo(arreglo, 0, arreglo.length-1, comparador);
    }

    public static <T> void quickSortRecursivo(T[] arreglo, int ini, int fini, Comparator<T> comparador) {
        if (fini <= ini) {
            return;
        }
        int i = ini + 1;
        int j = fini;
        while (i < j) {
            if ((comparador.compare(arreglo[i], arreglo[ini]) > 0)
                    && comparador.compare(arreglo[j], arreglo[ini]) <= 0) {
                intercambia(arreglo, i, j);
                i++;
                j--;
            } else if (comparador.compare(arreglo[i], arreglo[ini]) <= 0) {
                i++;
            } else {
                j--;
            }
        }
        if (comparador.compare(arreglo[i], arreglo[ini]) > 0) {
            i--;
        }
        intercambia(arreglo, ini, i);
        quickSortRecursivo(arreglo, ini, i - 1, comparador);
        quickSortRecursivo(arreglo, i + 1, fini, comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i=0; i<arreglo.length-1; i++){
            int menor = i;
            for(int j=i+1; j<arreglo.length; j++){
                if(comparador.compare(arreglo[j], arreglo[menor])<0){
                    menor = j;
                }
            }
            intercambia(arreglo, i, menor);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    public static <T> void intercambia(T[] arreglo, int a, int b) {
        T aux = arreglo[a];

        arreglo[a] = arreglo[b];
        arreglo[b] = aux;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaBinariaRecursiva(arreglo, elemento, 0, arreglo.length - 1, comparador);
    }

    public static <T> int busquedaBinariaRecursiva(T[] arreglo, T elemento, int ini, int fini,
                                                   Comparator<T> comparador) {
        if (fini < ini) {
            return -1;
        }
        int medio = ((fini - ini) / 2) + ini;
        if (comparador.compare(elemento, arreglo[medio]) == 0)
            return medio;
        else if (comparador.compare(elemento, arreglo[medio]) < 0) {
            return busquedaBinariaRecursiva(arreglo, elemento, ini, medio - 1, comparador);
        } else {
            return busquedaBinariaRecursiva(arreglo, elemento, medio + 1, fini, comparador);
        }
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
