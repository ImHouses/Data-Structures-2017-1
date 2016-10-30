package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<ArbolBinario<T>.Vertice>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            try {
            return cola.mira() != null;
        } catch (NoSuchElementException e) {
                return false;
            }
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            Vertice v = cola.saca();
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
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
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("El elemento es null.");
        if (esVacio()) {
            raiz = nuevoVertice(elemento);
            ultimoAgregado = raiz;
            elementos++;
            return;
        }
        Cola<Vertice> c = new Cola<Vertice>();
        c.mete(raiz);
        agrega(elemento,c);
    }
    private void agrega(T e, Cola<Vertice> c) {
        while (!c.esVacia()) {
            Vertice v = c.saca();

            if (v.izquierdo == null) {
                v.izquierdo = nuevoVertice(e);
                v.izquierdo.padre = v;
                ultimoAgregado = v.izquierdo;
                elementos++;
                return;
            }
            if (v.derecho == null) {
                v.derecho = nuevoVertice(e);
                v.derecho.padre = v;
                ultimoAgregado = v.derecho;
                elementos++;
                return;
            }
            c.mete(v.izquierdo);
            c.mete(v.derecho);
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        /*Si el elemento a eliminar es la raíz.*/
        if (raiz.elemento.equals(elemento)) {
            /*Si la raíz no tiene hijos.*/
            if (raiz.izquierdo == null && raiz.derecho == null) {
            raiz = null;
            elementos--;
            return;
            }
            /*Si la raíz tiene hijos.*/
            if(raiz.izquierdo != null || raiz.derecho != null) {
                Vertice u = ultimoBFS();
                raiz.elemento = u.elemento;
                if(u.padre.izquierdo == u)
                    u.padre.izquierdo = null;
                else u.padre.derecho = null;
                u.elemento = null;
                elementos--;
                return;
            }
        }
        /*Si el elemento a eliminar está en los subárboles de la raíz.*/
        Lista<Vertice> vertices = recorridoBFS(elemento);
        Vertice ultimo = vertices.getPrimero();
        vertices.elimina(ultimo);
        Vertice v = vertices.getPrimero();
        if (v == raiz)
            return;
        v.elemento = ultimo.elemento;
        if (ultimo.padre.izquierdo == ultimo)
            ultimo.padre.izquierdo = null;
        else ultimo.padre.derecho = null;
        ultimo.padre = null;
        ultimo = null;
        elementos--;
    }

    /* Método privado auxiliar para conseguir el vértice que contiene
        al elemento buscado y al último vértice vía BFS.*/
    private Lista<Vertice> recorridoBFS(T elemento) {
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        Vertice ultimo = raiz;
        Vertice ve = raiz;
        while (!cola.esVacia()) {
            Vertice v = cola.saca();
            if (v.elemento.equals(elemento))
                ve = v;
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
            ultimo = v;
        }
        Lista<Vertice> vertices = new Lista<Vertice>();
        vertices.agregaFinal(ultimo);
        vertices.agregaFinal(ve);
        return vertices;
    }
    
    /* Método privado auxiliar para conseguir el último elemento del árbol vía
        BFS.*/
    private Vertice ultimoBFS() {
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        Vertice ultimo = null;
        while (!cola.esVacia()) {
            Vertice v = cola.saca();
            if (v.izquierdo != null)
                cola.mete(v.izquierdo);
            if (v.derecho != null)
                cola.mete(v.derecho);
            ultimo = v;
        }
        return ultimo;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
