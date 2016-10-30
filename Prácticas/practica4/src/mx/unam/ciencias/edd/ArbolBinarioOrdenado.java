    package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
            pila = new Pila<Vertice>();
            Vertice v = raiz;
            while (v != null) {
                pila.mete(v);
                v = v.izquierdo;
            }
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
            Vertice v = pila.saca();
            Vertice vi;
            if (v.derecho != null) {
                vi = v.derecho;
                while (vi != null) {
                    pila.mete(vi);
                    vi = vi.izquierdo;
                }
            }
            return v.elemento;
         }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException();
        if (raiz == null) {
            raiz = ultimoAgregado = nuevoVertice(elemento);
            elementos++;
            return;
        }
        agrega(elemento,raiz);
    }
    private void agrega(T elemento, Vertice v) {
        if (elemento.compareTo(v.elemento) <= 0 ) {
            if (v.izquierdo == null) {
                v.izquierdo = nuevoVertice(elemento);
                v.izquierdo.padre = v;
                ultimoAgregado = v.izquierdo;
                elementos++;
                return;
            }
            agrega(elemento,v.izquierdo);
        } else {
            if(v.derecho == null) {
                v.derecho = nuevoVertice(elemento);
                v.derecho.padre = v;
                ultimoAgregado = v.derecho;
                elementos++;
                return;
            }
            agrega(elemento,v.derecho);
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice e = busca(raiz,elemento);
        if(e == null)
            return;
        if (e.izquierdo == null && e.derecho == null)
            eliminaHoja(e);
        else if(e.izquierdo == null && e.derecho != null)
            eliminarConHijoDerecho(e);
        else if (e.izquierdo != null && e.derecho == null)
                eliminarConHijoIzquierdo(e);
        else if (e.izquierdo != null && e.derecho != null)
                eliminarConDosHijos(e);
        return;
    }

    /**
    * Método auxiliar para eliminar una hoja
    * @param el vertice a eliminar
    */
    private void eliminaHoja(Vertice v) {
        if (v == raiz) {
            raiz = null;
            elementos--;
            return;
        }
        if(v.padre.izquierdo == v)
            v.padre.izquierdo = null;
        else
            v.padre.derecho = null;
        v = null;
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con un hijo izquierdo.
    * @param el vértice a eliminar
    */
    private void eliminarConHijoIzquierdo(Vertice v) {
        if (v == raiz) {
            raiz = v.izquierdo;
            v.izquierdo.padre = null;
            elementos--;
            return;
        }

        if (v.padre.izquierdo == v) {
        v.padre.izquierdo = v.izquierdo;
        v.izquierdo.padre = v.padre;
        //v.padre = null;
        v = null;
        } else {
            v.padre.derecho = v.izquierdo;
            v.izquierdo.padre = v.padre;
            //v.padre = null;
            v = null;
        }
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con hijo derecho.
    * @param el vértice a eliminar
    */
    private void eliminarConHijoDerecho(Vertice v) {
        if (v == raiz) {
            raiz = v.derecho;
            v.derecho.padre = null;
            elementos--;
            return;
        }
        if (v.padre.derecho == v) {
        v.padre.derecho = v.derecho;
        v.derecho.padre = v.padre;
        //v.padre = null;
        v = null;
        } else {
            v.padre.izquierdo = v.derecho;
            v.derecho.padre = v.padre;
            //v.padre = null;
            v = null;
        }
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con dos hijos.
    * @param el vértice a eliminar
    */
    private void eliminarConDosHijos(Vertice v) {
        Vertice maximoIzq = maximoEnSubarbol(v.izquierdo);
        v.elemento = maximoIzq.elemento;
        if (maximoIzq.izquierdo == null)
            eliminaHoja(maximoIzq);
        else eliminarConHijoIzquierdo(maximoIzq);
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz,elemento);
    }

    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {
        if (vertice == null)
            return null;
        if (elemento.equals(vertice.elemento))
            return vertice;
        return elemento.compareTo(vertice.elemento) <= 0 ?
                busca(vertice.izquierdo,elemento) :
                busca(vertice.derecho,elemento);
    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {
        Vertice v = vertice;
        while (v.derecho != null) {
            v = v.derecho;
        }
        return v;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        Vertice v = vertice(vertice);
        if (v.izquierdo == null)
            return;
        Vertice vi = v.izquierdo;
        Vertice derechoIzquierdo = vi.derecho;
        vi.derecho = v;
        vi.padre = v.padre;
        v.izquierdo = derechoIzquierdo;
        if (v.izquierdo != null)
            v.izquierdo.padre = v;
        if (vi.padre == null) {
            raiz = vi;
            v.padre = vi;
            return;
        }  
        if (esDerecho(v))
            v.padre.derecho = vi;
        else v.padre.izquierdo = vi;
        v.padre = vi;
    }

    protected boolean esDerecho(Vertice v) {
        if (v.padre.derecho == v)
            return true;
        else return false;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice v = vertice(vertice);
        if (v.derecho == null)
            return;
        Vertice vd = v.derecho;
        Vertice izquierdoDerecho = vd.izquierdo;
        v.derecho = izquierdoDerecho;
        vd.izquierdo = v;
        vd.padre = v.padre;
        if (v.derecho != null)
            v.derecho.padre = v;
        if (vd.padre == null) {
            raiz = vd;
            v.padre = vd;
            return;
        }
        if (esDerecho(v))
            v.padre.derecho = vd;
        else v.padre.izquierdo = vd;
        v.padre = vd;
    }
}
