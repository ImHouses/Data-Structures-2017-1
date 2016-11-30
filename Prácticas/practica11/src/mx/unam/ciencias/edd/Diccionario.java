package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, permitiendo (en general, dependiendo de qué tan bueno
 * sea su método para dispersar) agregar, eliminar, y buscar valores en tiempo
 * <i>O</i>(1) (amortizado) en cada uno de estos casos.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador implements Iterator<V> {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Diccionario<K,V>.Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            indice = 0;
        }

        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
            return indice != entradas.length;
        }

        /* Regresa el siguiente elemento. */
        public V next() {
            /* 
              Si estamos en una entrada vacía avanzamos hasta encontrar una
              entrada ocupada.
             */
            while (entradas[indice] == null && indice < entradas.length)
                indice++;
            if (iterador == null)
                iterador = entradas[indice].iterador();
            if (iterador.hasNext())
                return iterador.next();
            else iterador = null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Tamaño mínimo; decidido arbitrariamente a 2^6. */
    private static final int MIN_N = 64;

    /* Máscara para no usar módulo. */
    private int mascara;
    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private Lista<Entrada>[] nuevoArreglo(int n) {
        Lista[] arreglo = new Lista[n];
        return (Lista<Entrada>[])arreglo;
    }

    /**
     * Construye un diccionario con un tamaño inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this.dispersor = FabricaDispersores(AlgoritmoDispersor.BJ_STRING);
        this.entradas = nuevoArreglo(MIN_N);
        this.elementos = 0;
        this.mascara = MIN_N - 1;
    }

    /**
     * Construye un diccionario con un tamaño inicial definido por el usuario, y
     * un dispersor predeterminado.
     * @param tam el tamaño a utilizar.
     */
    public Diccionario(int tam) {
        int l = longitud(tam);
        this.dispersor = FabricaDispersores(AlgoritmoDispersor.BJ_STRING);
        this.entradas = nuevoArreglo(l);
        this.elementos = 0;
        this.mascara = l - 1;
    }

    /**
     * Construye un diccionario con un tamaño inicial predeterminado, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        this.entradas = nuevoArreglo(MIN_N);
        this.elementos = 0;
        this.mascara = MIN_N - 1;
    }

    /**
     * Construye un diccionario con un tamaño inicial, y un método de dispersor
     * definidos por el usuario.
     * @param tam el tamaño del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int tam, Dispersor<K> dispersor) {
        int l = longitud(tam);
        this.dispersor = dispersor;
        this.entradas = nuevoArreglo(l);
        this.elementos = 0;
        this.mascara = l - 1;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null)
            throw new IllegalArgumentException();
        if (estaCargado())
            creceArreglo();
        int indice = getIndice(llave);
        if (entradas[indice] == null)
            entradas[indice] = Lista<Entrada> entrada = new Lista<Entrada>();
        entradas[indice].agregaFinal(new Entrada(llave,valor));
        elementos++; 
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        int indice = getIndice(llave);
        if (contiene(llave)) {
        for (Entrada e : entradas[indice])
            if (e.llave.equals(llave))
                return e.valor;
        } else throw new NoSuchElementException("La llave no se encuentra.");
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
        int indice = getIndice(llave);
        if (entradas[indice] == null)
            return false;
        for (Entrada e : entradas[indice])
            if (e.llave.equals(llave))
                return true;
        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        int indice = getIndice(llave);
        if (!contiene(llave))
            throw new NoSuchElementException("La llave no se encuentra.");
        for (Entrada e : entradas[indice])
            if (e.llave.equals(llave))
                entradas[indice].elimina(e);
    }

    /**
     * Regresa una lista con todas las llaves con valores asociados en el
     * diccionario. La lista no tiene ningún tipo de orden.
     * @return una lista con todas las llaves.
     */
    public Lista<K> llaves() {
        Lista<K> llaves = new Lista<K>();
        for (int i = 0 ; i < entradas.length ; i++) {
            if (entradas[i] == null)
                continue;
            for (Entrada e : entradas[i])
                llaves.agrega(e.llave);
        }
        return llaves;
    }

    /**
     * Regresa una lista con todos los valores en el diccionario. La lista no
     * tiene ningún tipo de orden.
     * @return una lista con todos los valores.
     */
    public Lista<V> valores() {
        Lista<K> llaves = llaves();
        Lista<V> valores = new Lista<V>();
        for (K k : llaves)
            valores.agrega(get(k));
        return valores;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int c = 0;
        for (int i = 0 ; i < entradas.length ; i++) {
            if (entradas[i] == null)
                continue;
            c += entradas[i].getElementos() - 1;
        }
        return c;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int c = 0;
        for (int i = 0 ; i < entradas.length ; i++ ) {
            if (entradas[i] == null)
                continue;
            if (entradas[i].getElementos() - 1 > c)
                c += entradas[i].getElementos - 1;
        }
        return c;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return elementos / entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacio() {
        return elementos == 0;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Diccionario))
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d = (Diccionario<K, V>)o;
        if (d.elementos != elementos)
            return false;
        Lista<Entrada>[] ld = d.entradas;
        for (int i = 0 ; i < entradas.length; ) {
            if ((ld[i] == null && entradas[i] != null) ||
                 (ld[i] != null && entradas[i] == null))
                return false;
            if (!ld[i].equals(entradas[i]))
                return false;
        }
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar el diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new Iterator();
    }

    /**
    * Regresa el tamaño del arreglo en una potencia de 2.
    * @param n el número de elementos
    * @return el tamaño del arreglo.
    */
    private int longitud(int n) {
        int r = 1;
        while (r < n)
            r <<= 1;
        r << 1 < 64 ? 64 : r << 1;
    }
    /**
    * Nos dice si el diccionario está en su carga máxima.
    * @return <code>true</code> si el diccionario está cargado.
    *         <code>false</code> en otro caso.
    */
    private boolean estaCargado() {
        return carga() >= MAXIMA_CARGA - .10;
    }

    /**
    * Crece el arreglo de las entradas; hay qué volver a agregar todos los 
    * elementos uno por uno por el problema de las diferentes máscaras.
    */
    private void creceArreglo() {
        int tam = longitud(entradas.length);
        this.mascara = tam - 1;
        this.elementos = 0;
        Lista<Entrada>[] antiguo = this.entradas;
        Lista<Entrada>[] nuevo = nuevoArreglo(tam);
        this.entradas = nuevo;
        for (Entrada e : antiguo)
            agrega(e.llave,e.valor);
    }

    private int getIndice(K llave) {
        return (mascara & (dispersor.dispersa(llave)));
    }
}
