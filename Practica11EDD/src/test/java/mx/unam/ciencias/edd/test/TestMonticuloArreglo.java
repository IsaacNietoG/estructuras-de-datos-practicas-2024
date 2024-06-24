package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloArreglo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link MonticuloArreglo}.
 */
public class TestMonticuloArreglo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El montículo mínimo. */
    private MonticuloArreglo<Integer> monticulo;
    /* Arreglo auxiliar. */
    private Integer[] arreglo;

    /* Método que verifica que un montículo de arreglo cumpla con sus
     * propiedades. */
    private static <T extends Comparable<T>> void
    verificaMonticuloArreglo(T[] arreglo, MonticuloArreglo<T> monticulo) {
        for (int i = 0; i < monticulo.getElementos(); i++) {
            T e1 = monticulo.get(i);
            T e2 = arreglo[i];
            Assert.assertTrue(e1 == e2);
            if (e1 != null) {
                Assert.assertTrue(e1.equals(e2));
            } else {
                Assert.assertTrue(e2 == null);
            }
        }
    }

    /**
     * Crea un montículo de arreglo para cada prueba.
     */
    public TestMonticuloArreglo() {
        random = new Random();
        total = 10 + random.nextInt(90);
        arreglo = new Integer[total];
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++) {
            arreglo[i] = random.nextInt();
            lista.agrega(arreglo[i]);
        }
        monticulo = new MonticuloArreglo<Integer>(lista);
        verificaMonticuloArreglo(arreglo, monticulo);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#MonticuloArreglo}.
     */
    @Test public void testConstructor() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(monticulo.get(i));
        MonticuloArreglo<Integer> m2;
        m2 = new MonticuloArreglo<Integer>(lista, total);
        for (int i = 0; i < total; i++)
            Assert.assertTrue(monticulo.get(i).equals(m2.get(i)));
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#elimina}.
     */
    @Test public void testElimina() {
        Lista<Integer> ordenada =
            new Lista<Integer>();
        for (int i = 0; i < arreglo.length; i++)
            ordenada.agrega(arreglo[i]);
        ordenada = Lista.mergeSort(ordenada);
        while (!monticulo.esVacia()) {
            Integer a = monticulo.elimina();
            Integer b = ordenada.eliminaPrimero();
            Assert.assertTrue(a.equals(b));
            for (int i = 0; i < arreglo.length; i++)
                if (a.equals(arreglo[i]))
                    arreglo[i] = null;
            verificaMonticuloArreglo(arreglo, monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
        try {
            monticulo.elimina();
            Assert.fail();
        } catch (IllegalStateException ise) {}
        for (int i = 0; i < arreglo.length; i++)
            Assert.assertTrue(arreglo[i] == null);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#esVacia}.
     */
    @Test public void testEsVacio() {
        Assert.assertFalse(monticulo.esVacia());
        Lista<Integer> lista = new Lista<Integer>();
        monticulo = new MonticuloArreglo<Integer>(lista);
        Assert.assertTrue(monticulo.esVacia());
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#reordena}.
     */
    @Test public void testReordena() {
        Lista<Caja<Double>> l = new Lista<Caja<Double>>();
        for (int i = 0; i < total; i++)
            l.agregaFinal(new Caja<Double>((double)random.nextInt()));
        MonticuloArreglo<Caja<Double>> monticulo =
            new MonticuloArreglo<Caja<Double>>(l);
        @SuppressWarnings("unchecked")
            Caja<Double>[] arreglo = (Caja<Double>[])new Caja[total];
        int c = 0;
        for (Caja<Double> caja : l)
            arreglo[c++] = caja;
        int n = monticulo.getElementos();
        for (int i = 0; i < n; i++) {
            verificaMonticuloArreglo(arreglo, monticulo);
            Caja<Double> caja = monticulo.get(random.nextInt(n));
            caja.setElemento(caja.getElemento() / 10.0);
            monticulo.reordena(caja);
            verificaMonticuloArreglo(arreglo, monticulo);
            for (int j = 0; j < arreglo.length; j++)
                Assert.assertTrue(monticulo.get(j).equals(arreglo[j]));
        }
        for (int i = 0; i < n; i++) {
            verificaMonticuloArreglo(arreglo, monticulo);
            Caja<Double> caja = monticulo.get(random.nextInt(n));
            caja.setElemento(caja.getElemento() * 10.0);
            monticulo.reordena(caja);
            verificaMonticuloArreglo(arreglo, monticulo);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#getElementos}.
     */
    @Test public void testGetElementos() {
        while (!monticulo.esVacia()) {
            Assert.assertTrue(monticulo.getElementos() == total--);
            monticulo.elimina();
        }
        Assert.assertTrue(monticulo.getElementos() == 0);
    }

    /**
     * Prueba unitaria para {@link MonticuloArreglo#get}.
     */
    @Test public void testGet() {
        try {
            monticulo.get(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            monticulo.get(total);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < arreglo.length; i++)
            Assert.assertTrue(monticulo.get(i).equals(arreglo[i]));
    }
}
