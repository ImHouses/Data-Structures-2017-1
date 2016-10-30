package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las operaciones de
 * inserción, eliminación y búsqueda pueden realizarse en <i>O</i>(log
 * <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends ArbolBinario<T>.Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.ROJO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            String el = elemento.toString();
            String colorString;
            colorString = (color == Color.ROJO) ? "R" : "N";
            colorString = String.format("%s{%s}", colorString,el);
            return colorString;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = 
                                                            (VerticeRojinegro)o;
            return equals(this,vertice);
        }

        private boolean equals(VerticeRojinegro v1, VerticeRojinegro v2) {
            if(v1 == null && v2 != null)
                return false;
            if (v2 == null && v1 != null)
                return false;
            if (v1 == null && v2 == null)
                return true;
            if(!v2.elemento.equals(v1.elemento) || v2.color != v1.color)
                return false;
            VerticeRojinegro v1izq = (VerticeRojinegro)v1.izquierdo;
            VerticeRojinegro v2izq = (VerticeRojinegro)v2.izquierdo;
            VerticeRojinegro v1der = (VerticeRojinegro)v1.derecho;
            VerticeRojinegro v2der = (VerticeRojinegro)v2.derecho;
            boolean izq = equals(v1izq,v2izq);
            boolean der = equals(v1der,v2der);
            if (izq == der)
                return izq;
            return false;
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice
     *                rojinegro.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        if (!(vertice instanceof ArbolRojinegro<?>.VerticeRojinegro))
            throw new ClassCastException();
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
       super.agrega(elemento);
       VerticeRojinegro v = (VerticeRojinegro)ultimoAgregado;
       rebalancea(v);

    }

    private void rebalancea(VerticeRojinegro v) {
        VerticeRojinegro n = v;
        VerticeRojinegro padre = getPadre(n); 
        /* Caso 1 */
        if (padre == null) {
            n.color = Color.NEGRO;
            return;
        }
        /* Caso 2 */
        if (esNegro(padre)) {
            return;
        }
        /* Caso 3*/
        VerticeRojinegro tio = getTio(n);
        if (tio != null) {
            VerticeRojinegro abuelo = getAbuelo(n);
            if (esRojo(padre) && esRojo(tio)) {
                padre.color = Color.NEGRO;
                tio.color = Color.NEGRO;
                abuelo.color = Color.ROJO;
                rebalancea(abuelo);
                return;
            }
        }
        /* Caso 4 */
        if (sonContrarios(n)) {
            if (esDerecho(padre))
                rotarDerecha(padre);
            else rotarIzquierda(padre);
            /* Seguimos a caso 5. */
            caso5(padre);
            return;
        }
        /* Caso 5 */
            caso5(n);

    }
    /**
    * Método auxiliar que representa el caso 5 del método {@link agrega}
    */
    private void caso5(VerticeRojinegro v) {
        VerticeRojinegro n = v;
        VerticeRojinegro abuelo = getAbuelo(n);
        VerticeRojinegro padre = getPadre(n);
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if(esDerecho(n))
            rotarIzquierda(abuelo);
        else rotarDerecha(abuelo);
    }

    /**
    * Método auxiliar. Obtiene el padre del vértice.
    * @param El vértice del que se obtendrá el padre.
    * @return El padre del vértice; null si no tiene padre.
    */
    private VerticeRojinegro getPadre(VerticeRojinegro v) {
        return v.padre == null ? null : verticeRojinegro(v.padre);
    }

    /**
    * Método privado. Obtiene el tío del vértice que recibe.
    * @param el vértice del que se sacará el tío. 
    * @return El tío del vértice v. null si no hay padre, abuelo, o tío. 
    */
    private VerticeRojinegro getTio(VerticeRojinegro v) {
        /* No hay padre. */
        if (v.padre == null)
            return null;
        /* No hay abuelo. */
        if (getAbuelo(v) == null)
            return null;
        VerticeRojinegro papa = getPadre(v);
        
        if (esDerecho(papa)) { /* Entonces el tío es izquierdo. */
            /* No hay tio izquierdo. */
            if (papa.padre.izquierdo == null)
                return null;
            return verticeRojinegro(papa.padre.izquierdo);
        }
        else { /* Entonces el tío es derecho. */
            /* No hay tío derecho. */
            if (papa.padre.derecho == null)
                return null;
         return verticeRojinegro(papa.padre.derecho);
        }
    }

    /**
    * Método privado. Obtiene el abuelo del vértice que recibe.
    * @param el vértice del que se obtendrá el abuelo.
    * @return el ebuelo del vértice. null si no hay padre o abuelo.
    */
    private VerticeRojinegro getAbuelo(VerticeRojinegro v) {
        if (getPadre(v) == null)
            return null;
        if (v.padre.padre == null)
            return null;
        return verticeRojinegro(v.padre.padre);
    }

    /**
    * Método auxiliar. Regresa <code>true</code> si los vértices son contrarios,
    * es decir; si el vertice   
    * <code>false</code> en otro caso.
    * @return <code>true</code> si es ROJO. <code>false</code> si es NEGRO.
    */
    private boolean sonContrarios(VerticeRojinegro v) {
        VerticeRojinegro padre = getPadre(v);
        if (esDerecho(v) && !esDerecho(padre))
            return true;
        if (!esDerecho(v) && esDerecho(padre))
            return true;
        return false;
    }

    /**
    * Método auxiliar. Regresa <code>true</code> si el vertice es NEGRO.  
    * <code>false</code> en otro caso.
    * @return <code>true</code> si es NEGRO. <code>false</code> si es ROJO.
    */
    private boolean esNegro(VerticeRojinegro v) {
        if (v == null)
            return true;
        return v.color != Color.ROJO;
    }

    /**
    * Método auxiliar. Regresa <code>true</code> si el vertice es ROJO.  
    * <code>false</code> en otro caso.
    * @return <code>true</code> si es ROJO. <code>false</code> si es NEGRO.
    */
    private boolean esRojo(VerticeRojinegro v) {
        if (v == null)
            return false;
        return v.color != Color.NEGRO;
    }
    


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = verticeRojinegro(super.busca(elemento));
        if (v == null)
            return;
        VerticeRojinegro anterior = 
                        verticeRojinegro(super.maximoEnSubarbol(v.izquierdo));
        /* Si no hay anterior. */
        if (anterior == null)
            anterior = v;
        /* Intercambiamos de todas maneras. */
        intercambia(v,anterior);
        VerticeRojinegro hijo = hijo(anterior,anterior.izquierdo);
        subir(anterior,hijo);
        if (anterior.color == Color.NEGRO && hijo.color == Color.ROJO)
            hijo.color = Color.NEGRO;
        if (anterior.color == Color.ROJO)
            eliminarNodo(hijo);
        if (anterior.color == Color.NEGRO && hijo.color == Color.NEGRO)
            rebalanceaEliminado(hijo);


    }

    private void eliminarNodo(Vertice v) {
        if (esDerecho(v)) {
            v.padre.derecho = null;
            v.padre = null;
            elementos--;
        } else {
            v.padre.izquierdo = null;
            v.padre = null;
            elementos--;
        }
    }

    private void rebalanceaEliminado(VerticeRojinegro n){
        return;
    }

    private void intercambia(VerticeRojinegro v1, VerticeRojinegro v2) {
        T p = v1.elemento;
        v1.elemento = v2.elemento;
        v2.elemento = p;
    }
    /**
    * Método auxiliar; como usamos el máximo del subárbol izquierdo entonces
    * se regresa el izquierdo de v, en dado caso que sea null, regresamos un
    * vértice fantasma (vertice que su elemento es null)
    */
    private VerticeRojinegro hijo(Vertice padre, Vertice v) {
        VerticeRojinegro hijo;
        if (v == null) {
            hijo = new VerticeRojinegro(null);
            hijo.color = Color.NEGRO;
            hijo.padre = padre;
            padre.izquierdo = hijo;
            return verticeRojinegro(hijo);
        } 
        return verticeRojinegro(v);
    }

    private void subir(Vertice papa, Vertice hijo) {
            papa.padre.derecho = hijo;
            hijo.padre = papa.padre;
            hijo.izquierdo = papa;
            papa.padre = hijo;
            if (papa == raiz)
                raiz = hijo;
    }


    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    /**
    * Método auxiliar para los métodos de {@link agrega} y {@link elimina}
    * Gira el vértice recibido a la derecha.
    */
    private void rotarDerecha(VerticeArbolBinario<T> vertice) {
        super.giraDerecha(vertice);
    }

    /**
    * Método auxiliar para los métodos de {@link agrega} y {@link elimina}
    * Gira el vértice recibido a la izquierda.
    */
    private void rotarIzquierda(VerticeArbolBinario<T> vertice) {
        super.giraIzquierda(vertice);
    }
}
