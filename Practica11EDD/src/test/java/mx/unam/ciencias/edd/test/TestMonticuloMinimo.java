package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloMinimo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link MonticuloMinimo}.
 */
public class TestMonticuloMinimo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* El montículo mínimo. */
    private MonticuloMinimo<String> monticulo;

    /* Método que verifica que un montículo mínimo cumpla con sus
     * propiedades. */
    private static <T extends Comparable<T>> void
    verificaMonticuloMinimo(MonticuloMinimo<T> monticulo) {
        int n = monticulo.getElementos();
        if (n == 0)
            return;
        for (int i = 0; i < n; i++) {
            T e = monticulo.get(i);
            if (e == null)
                continue;
            int izq = 2*i + 1;
            int der = 2*i + 2;
            if (izq >= n)
                continue;
            Assert.assertTrue(monticulo.get(izq).compareTo(e) >= 0);
            if (der >= n)
                continue;
            Assert.assertTrue(monticulo.get(der).compareTo(e) >= 0);
        }
    }

    /**
     * Crea un montículo mínimo para cada prueba.
     */
    public TestMonticuloMinimo() {
        random = new Random();
        total = 10 + random.nextInt(90);
        Lista<String> l = new Lista<String>();
        for (int i = 0; i < total; i++)
            l.agregaFinal(Integer.toString(random.nextInt()));
        monticulo = new MonticuloMinimo<String>(l);
        verificaMonticuloMinimo(monticulo);
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#MonticuloMinimo(Coleccion)} y
     * {@link MonticuloMinimo#MonticuloMinimo(Iterable,int)}.
     */
    @Test public void testConstructores() {
        int n = monticulo.getElementos();
        MonticuloMinimo<String> monticulo2 =
            new MonticuloMinimo<String>(monticulo, n);
        Assert.assertTrue(monticulo.equals(monticulo2));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total * 4; i++) {
            String s = Integer.toString(random.nextInt());
            monticulo.agrega(s);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == total + i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina}.
     */
    @Test public void testElimina() {
        while (!monticulo.esVacia()) {
            String a = monticulo.elimina();
            for (int i = 0; i < monticulo.getElementos(); i++) {
                String b = monticulo.get(i);
                Assert.assertTrue(a.compareTo(b) <= 0);
            }
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
        try {
            monticulo.elimina();
            Assert.fail();
        } catch (IllegalStateException ise) {}
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina(Object)}.
     */
    @Test public void testEliminaElemento() {
        while (!monticulo.esVacia()) {
            int n = monticulo.getElementos();
            String a = monticulo.get(random.nextInt(n));
            monticulo.elimina(a);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < monticulo.getElementos(); i++) {
            String a = monticulo.get(i);
            Assert.assertTrue(monticulo.contiene(a));
        }
        Assert.assertFalse(monticulo.contiene("a"));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#esVacia}.
     */
    @Test public void testEsVacio() {
        monticulo = new MonticuloMinimo<String>();
        Assert.assertTrue(monticulo.esVacia());
        String s = Integer.toString(random.nextInt());
        monticulo.agrega(s);
        Assert.assertFalse(monticulo.esVacia());
        Assert.assertTrue(s == monticulo.elimina());
        Assert.assertTrue(monticulo.esVacia());
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertFalse(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == total);
        monticulo.limpia();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        monticulo = new MonticuloMinimo<String>();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        for (int i = 0; i < total; i++)
            monticulo.agrega(String.valueOf(i));
        Assert.assertFalse(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == total);
        monticulo.limpia();
        Assert.assertTrue(monticulo.esVacia());
        Assert.assertTrue(monticulo.getElementos() == 0);
        for (int i = 0; i < total; i++) {
            try {
                monticulo.get(i);
                Assert.fail();
            } catch (NoSuchElementException nsee) {}
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#reordena}.
     */
    @Test public void testReordena() {
        Lista<Caja<Double>> l = new Lista<Caja<Double>>();
        for (int i = 0; i < total; i++)
            l.agregaFinal(new Caja<Double>((double)random.nextInt()));
        MonticuloMinimo<Caja<Double>> monticulo =
            new MonticuloMinimo<Caja<Double>>(l);
        int n = monticulo.getElementos();
        for (int i = 0; i < n; i++) {
            verificaMonticuloMinimo(monticulo);
            Caja<Double> c = monticulo.get(random.nextInt(n));
            c.setElemento(c.getElemento() / 10.0);
            monticulo.reordena(c);
            verificaMonticuloMinimo(monticulo);
        }
        for (int i = 0; i < n; i++) {
            verificaMonticuloMinimo(monticulo);
            Caja<Double> c = monticulo.get(random.nextInt(n));
            c.setElemento(c.getElemento() * 10.0);
            monticulo.reordena(c);
            verificaMonticuloMinimo(monticulo);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#getElementos}.
     */
    @Test public void testGetElementos() {
        monticulo = new MonticuloMinimo<String>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            Assert.assertTrue(monticulo.getElementos() == i);
            monticulo.agrega(s);
            Assert.assertTrue(monticulo.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#get}.
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
        monticulo = new MonticuloMinimo<String>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            monticulo.agrega(s);
            Assert.assertTrue(monticulo.getElementos() == i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#toString}.
     */
    @Test public void testToString() {
        Lista<String> lista = new Lista<String>();
        for (int i = 0; i < total; i++)
            lista.agrega(Integer.toString(random.nextInt()));
        monticulo = new MonticuloMinimo<String>(lista);
        String s = "";
        for (int i = 0; i < monticulo.getElementos(); i++) {
            Assert.assertFalse(s.equals(monticulo.toString()));
            s += String.format("%s, ", monticulo.get(i));
        }
        Assert.assertTrue(s.equals(monticulo.toString()));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#equals}.
     */
    @Test public void testEquals() {
        Lista<String> lista = new Lista<String>();
        for (int i = 0; i < total; i++)
            lista.agrega(Integer.toString(random.nextInt()));
        monticulo = new MonticuloMinimo<String>(lista);
        verificaMonticuloMinimo(monticulo);
        MonticuloMinimo<String> otro;
        otro = new MonticuloMinimo<String>();
        for (int i = 0; i < monticulo.getElementos(); i++) {
            Assert.assertFalse(monticulo.equals(otro));
            otro.agrega(monticulo.get(i));
        }
        Assert.assertTrue(monticulo.equals(otro));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIterator() {
        MonticuloMinimo<Integer> monticulo = new MonticuloMinimo<Integer>();
        for (int i = 0; i < total; i++)
            monticulo.agrega(i);
        int i = 0;
        for (Integer n : monticulo)
            Assert.assertTrue(n.equals(i++));
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#hasNext} a través
     * del método {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIteradorHasNext() {
        Iterator<String> it = monticulo.iterator();
        for (int i = 0; i < total; i++) {
            Assert.assertTrue(it.hasNext());
            try {
                it.next();
            } catch (NoSuchElementException nsee) {
                Assert.fail();
            }
        }
        Assert.assertFalse(it.hasNext());
    }

    /**
     * Prueba unitaria para la implementación {@link Iterator#next} a través del
     * método {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIteradorNext() {
        Iterator<String> it = monticulo.iterator();
        while (it.hasNext()) {
            try {
                it.next();
            } catch (NoSuchElementException nsee) {
                Assert.fail();
            }
        }
        try {
            it.next();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#heapSort}.
     */
    @Test public void testHeapSort() {
        Random random = new Random();
        int total = 10 + random.nextInt(90);
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 0; i < total; i++)
            lista.agrega(random.nextInt() % total);
        Lista<Integer> ordenada = MonticuloMinimo.heapSort(lista);
        Lista<Integer> control = Lista.mergeSort(lista);
        Assert.assertTrue(ordenada.equals(control));
    }
}
