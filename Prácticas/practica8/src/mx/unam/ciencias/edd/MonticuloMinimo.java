package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear un montículo
 * mínimo con <em>n</em> elementos en tiempo <em>O</em>(<em>n</em>), y podemos
 * agregar y actualizar elementos en tiempo <em>O</em>(log <em>n</em>). Eliminar
 * el elemento mínimo también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice != siguiente;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (indice == siguiente)
                throw new NoSuchElementException();
            return arbol[indice++];
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* El siguiente índice dónde agregar un elemento. */
    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)}, pero se ofrece este constructor por
     * completez.
     */
    public MonticuloMinimo() {
        arbol = creaArregloGenerico(1000);
        siguiente = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *              montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        int n = coleccion.getElementos();
        arbol = creaArregloGenerico(n+100);

        for (T e : coleccion) {
            arbol[siguiente] = e;
            e.setIndice(siguiente++);
        }
        for (int i = n / 2; i >= 0 ; i--)
            minimizaMonticulo(i);
    }

    /**
    * Regresa el índice del padre, -1 en otro caso.
    * @return el índice del padre
    */
    private int padre (int i) {
        if (0 < i && i < siguiente)
            return (i-1) / 2;
        return -1;
    }

    private void intercambia(int i, int j) {
        arbol[i].setIndice(j);
        arbol[j].setIndice(i);
        T e = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = e;
    }

    /**
    * Minimiza el montículo.
    * @param i El índice desde donde vamos a minimizar.
    */
    private void minimizaMonticulo(int i) {
        int izq = (2 * i) + 1;
        int der = (2 * i) + 2;
        int min = i;
        if (siguiente <= i)
            return;
        if (izq < siguiente && arbol[izq].compareTo(arbol[i]) < 0)
            min = izq;
        if (der < siguiente && arbol[der].compareTo(arbol[min]) < 0)
            min = der;
        if (min == i)
            return;
        else intercambia(min,i);
        minimizaMonticulo(min); 
    }

    /**
    * Actualiza hacia arrba.
    * @param i El índice desde donde vamos a actualizar.
    */
    private void update(int i) {
        if (i < 0)
            return;
        int padre = padre(i);
        while (0 < i && 0 <= padre && arbol[i].compareTo(arbol[padre]) < 0) {
            intercambia(padre,i);
            i = padre;
            padre = padre(i);
        }
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (siguiente == arbol.length)
            creceArreglo();
        elemento.setIndice(siguiente);
        arbol[siguiente] = elemento;
        siguiente++;
        update(siguiente-1);
    }

    /*
    * Método auxiliar para crecer el arreglo.
    */
    private void creceArreglo() {
        T[] n = creaArregloGenerico(arbol.length * 2);
        siguiente = 0;
        for (T e : arbol)
            n[siguiente++] = e;
        this.arbol = n;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        if (this.esVacio())
            throw new IllegalStateException("El monticulo es vacío");
        T elemento = arbol[0];
        elimina(elemento);
        return elemento;
    }   

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if (elemento == null || esVacio())
            return;
        if (!contiene(elemento))
            return;
        int indice = elemento.getIndice();
        if (indice < 0 || siguiente <= indice)
            return;
        intercambia(indice,siguiente - 1);
        arbol[siguiente - 1] = null;
        siguiente--;
        update(indice);
        minimizaMonticulo(indice);
        elemento.setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (T e : arbol)
            if (elemento.equals(e))
                return true;
        return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacio() {
        return siguiente == 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
        if (elemento == null || !contiene(elemento))
            return;
        int indice = 0;
        for (int i = 0; i < siguiente ; i++) {
            if (elemento.equals(arbol[i]))
                break;
            indice++;
        }
        update(indice);
        minimizaMonticulo(indice);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    public T get(int i) {
        if (i < 0 || i > siguiente-1)
            throw new NoSuchElementException("Índice inválido.");
        return arbol[i];
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si el montículo mínimo es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof MonticuloMinimo))
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> mm =
            (MonticuloMinimo<T>)o;
        if (mm.getElementos() != this.getElementos())
            return false;
        for (int i = 0; i < this.getElementos() ; i++)
            if (!arbol[i].equals(mm.get(i)))
                return false;
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
