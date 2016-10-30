package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends ArbolBinario<T>.Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        public String toString() {
            String s = String.format("%s %d/%d",elemento,altura,equilibrio());
            return s;
        }

        protected int equilibrio() {
            VerticeAVL vi = (VerticeAVL)this.izquierdo;
            VerticeAVL vd = (VerticeAVL)this.derecho;
            int i = vi == null ? 0 : vi.altura;
            int d = vd == null ? 0 : vd.altura;
            return (i-d);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return equals(this,vertice);
        }

        private boolean equals(VerticeAVL v1, VerticeAVL v2) {
            if (v1 == null && v2 != null)
                return false;
            if (v1 != null && v2 == null)
                return false;
            if (v1 == null & v2 == null)
                return true;
            if (v1.altura != v2.altura)
                return false;
            VerticeAVL v1izq = (VerticeAVL)v1.izquierdo;
            VerticeAVL v2izq = (VerticeAVL)v2.izquierdo;
            VerticeAVL v1der = (VerticeAVL)v1.derecho;
            VerticeAVL v2der = (VerticeAVL)v2.derecho;
            boolean izq = equals(v1izq,v2izq);
            boolean der = equals(v1der,v2der);
            if (izq == der)
                return true;
            return false;
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeAVL ultimo = verticeAVL(ultimoAgregado);
        rebalancea(ultimo);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL e = verticeAVL(busca(raiz,elemento));
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
    }
    
    /**
    * Método auxiliar para eliminar una hoja
    * @param el vertice a eliminar
    */
    private void eliminaHoja(VerticeAVL v) {
        if (v == raiz) {
            raiz = null;
            elementos--;
            return;
        }
        VerticeAVL papa = verticeAVL(v.padre);
        if(v.padre.izquierdo == v)
            v.padre.izquierdo = null;
        else
            v.padre.derecho = null;
        rebalancea(papa);
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con un hijo izquierdo.
    * @param el vértice a eliminar
    */
    private void eliminarConHijoIzquierdo(VerticeAVL v) {
        if (v == raiz) {
            raiz = v.izquierdo;
            v.izquierdo.padre = null;
            elementos--;
            return;
        }
        VerticeAVL papa = verticeAVL(v.padre);
        if (v.padre.izquierdo == v) {
        v.padre.izquierdo = v.izquierdo;
        v.izquierdo.padre = v.padre;
        } else {
            v.padre.derecho = v.izquierdo;
            v.izquierdo.padre = v.padre;
        }
        v = null;
        rebalancea(papa);
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con hijo derecho.
    * @param el vértice a eliminar
    */
    private void eliminarConHijoDerecho(VerticeAVL v) {
        if (v == raiz) {
            raiz = v.derecho;
            v.derecho.padre = null;
            elementos--;
            return;
        }
        VerticeAVL papa = verticeAVL(v.padre);
        if (v.padre.derecho == v) {
        v.padre.derecho = v.derecho;
        v.derecho.padre = v.padre;
        } else {
            v.padre.izquierdo = v.derecho;
            v.derecho.padre = v.padre;
        }
        v = null;
        rebalancea(papa);
        elementos--;
    }

    /**
    * Método auxiliar para eliminar un vértice con dos hijos.
    * @param el vértice a eliminar
    */
    private void eliminarConDosHijos(VerticeAVL v) {
        VerticeAVL maximoIzq = verticeAVL(maximoEnSubarbol(v.izquierdo));
        v.elemento = maximoIzq.elemento;
        if (maximoIzq.izquierdo == null)
            eliminaHoja(maximoIzq);
        else eliminarConHijoIzquierdo(maximoIzq);
    }

    /**
    * Método auxiliar recursivo para rebalancear un árbol hasta la raíz.
    * @param el vertice a rebalancear
    */
    private void rebalancea(VerticeAVL v) {
        if (v == null)
            return;
        actualizaAltura(v);
        int e = getAltura(izquierdo(v)) - getAltura(derecho(v));
        if (e == -2) {
            VerticeAVL der = derecho(v);
            if (getAltura(izquierdo(der)) - getAltura(derecho(der)) == 1)
                giraDerechaAVL(der);
            giraIzquierdaAVL(v);
        } else if (e == 2) {
            VerticeAVL izq = izquierdo(v);
            if (getAltura(izquierdo(izq)) - getAltura(derecho(izq)) == -1)
                giraIzquierdaAVL(izq);
            giraDerechaAVL(v);
        }
        rebalancea(padre(v));
    }

    /**
    * Método auxiliar que obtiene el hijo izquierdo del vértice V.
    * @param vertice del que se obtendrá el hijo
    * @return el hijo izquierdo del vertice V, null si no tiene hijo izquierdo.
    */
    private VerticeAVL izquierdo(VerticeAVL v) {
        if (v.izquierdo == null)
            return null;
        return verticeAVL(v.izquierdo);
    }

    /**
    * Método auxiliar que obtiene el hijo derecho del vértice V.
    * @param vertice del que se obtendrá el hijo
    * @return el hijo derecho del vertice V, null si no tiene hijo derecho.
    */
    private VerticeAVL derecho(VerticeAVL v) {
        if (v.derecho == null)
            return null;
        return verticeAVL(v.derecho);
    }

    /**
    * Método auxiliar que obtiene el padre del vértice V.
    * @param vertice del que se obtendrá el padre.
    * @return el padre de V, null si no tiene padre.
    */
    private VerticeAVL padre(VerticeAVL v) {
        return verticeAVL(v.padre);
    }
    
    /**
    * Método auxiliar que actualiza la altura del vértice V.
    * @param El vértice al que se le actualizará la altura.
    */
    protected void actualizaAltura(VerticeAVL v) {
        VerticeAVL vi = (VerticeAVL)v.izquierdo;
        VerticeAVL vd = (VerticeAVL)v.derecho;
        if (vi == null && vd == null) {
            v.altura = 0;
            return;
        }
        v.altura = Math.max(getAltura(vi),getAltura(vd)) + 1; 
    }


    /**
     * Regresa la altura del vértice AVL.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    public int getAltura(VerticeArbolBinario<T> vertice) {
        if (vertice == null)
            return -1;
        if (!(vertice instanceof ArbolAVL<?>.VerticeAVL))
            throw new ClassCastException();
        return verticeAVL(vertice).altura;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice AVL.
     * @return el vértice recibido visto como vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    private void giraDerechaAVL(VerticeArbolBinario<T> vertice) {
        super.giraDerecha(vertice);
        VerticeAVL v = verticeAVL(vertice);
        actualizaAltura(v);
    }

    private void giraIzquierdaAVL(VerticeArbolBinario<T> vertice) {
        super.giraIzquierda(vertice);
        VerticeAVL v = verticeAVL(vertice);
        actualizaAltura(v);
    }
}
