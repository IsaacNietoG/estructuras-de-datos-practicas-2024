package mx.unam.ciencias.edd;


/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        int resultado = 0;

        int i = 0;
        while(i < llave.length)
            resultado ^= combina("BIG ENDIAN",
                                 getByte(llave, i++),
                                 getByte(llave, i++),
                                 getByte(llave, i++),
                                 getByte(llave, i++));

        return resultado;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        int i = 0;
        boolean ejecucion = true;

        while (ejecucion) {
            a += combina("LITTLE ENDIAN",
                         getByte(llave, i++),
                         getByte(llave, i++),
                         getByte(llave, i++),
                         getByte(llave, i++));
            b += combina("LITTLE ENDIAN",
                         getByte(llave, i++),
                         getByte(llave, i++),
                         getByte(llave, i++),
                         getByte(llave, i++));
            if (llave.length - i >= 4) {
                c += combina("LITTLE ENDIAN",
                        getByte(llave, i++),
                        getByte(llave, i++),
                        getByte(llave, i++),
                        getByte(llave, i++));
            } else {
                ejecucion = false;
                c += llave.length;
                c += combina("LITTLE ENDIAN",
                        getByte(llave, llave.length),
                        getByte(llave, i++),
                        getByte(llave, i++),
                        getByte(llave, i++));
            }

            // Mezcla de los valores para dispersión
            a -= b + c;
            a ^= (c >>> 13);
            b -= c + a;
            b ^= (a << 8);
            c -= a + b;
            c ^= (b >>> 13);

            a -= b + c;
            a ^= (c >>> 12);
            b -= c + a;
            b ^= (a << 16);
            c -= a + b;
            c ^= (b >>> 5);

            a -= b + c;
            a ^= (c >>> 3);
            b -= c + a;
            b ^= (a << 10);
            c -= a + b;
            c ^= (b >>> 15);
        }

        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;

        for(int i = 0; i < llave.length ; i++){
            h += (h << 5) + enmascara(llave[i]);
        }

        return h;
    }

    /**
     * Combina cuatro bytes en un entero.
     * @param modo para determinar el modo de combinar los bytes, b para big endian, l para little endian
     * @param a b c d los bytes a combinar
     *
    */
    private static int combina(String modo, int a, int b, int c, int d){
        switch(modo){
            case "BIG ENDIAN":
                return (((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | (d & 0xFF));
            case "LITTLE ENDIAN":
                return (((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | (a & 0xFF));
            default:
                return 0;
        }
    }

    /**
     *  Obtiene los bytes de un arreglo
     *
     *  Si el indice a obtener es mayor a la longitud del arreglo, retorna el byte 0. Para facilitar
     *  la disperson XOR y BobJenkins
     *  */
    private static int getByte(byte[] array, int index){
        if(index >= array.length)
            return 0x00;

        return (array[index]);
    }

    /**
     *  Aplica la mascara correspondiente al byte, para evitar conflictos en su inevitable casteo a entero
     *  */
    private static int enmascara(byte a){
        return (a & 0xFF);
    }
}
