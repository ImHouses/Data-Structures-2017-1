package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException("Eliminar con el iterador " +
                                                    "no está soportado");
        }
    }

    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Implementan VerticeGrafica. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice vecino;
        /* El peso de vecino conectando al vértice con el vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Grafica<T>.Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T getElemento() {
            return this.vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return this.vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return this.vecino.color;
        }

        /* Define el color del vecino. */
        @Override public void setColor(Color color) { 
            this.vecino.color = color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Grafica<T>.Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            vecinos = new Lista<Grafica<T>.Vecino>();
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.distancia = -1;
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
            return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (this.distancia < vertice.distancia)
                return -2;
            if (this.distancia > vertice.distancia)
                return 1;
            return 0;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<Vertice>();
        this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return this.vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return this.aristas;
    }

    /**
    * Método auxiliar. Hace un cast a los vértices para poder modificarlos y
    * acceder a sus atributos.
    * @param el vértice a hacer cast.
    * @return el vertice como instancia de {@link Vertice}
    */
    private Vertice castVertice(VerticeGrafica<T> v) {
        return (Vertice)v;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("El elemento es null");
        for(Vertice v : vertices) 
            if (v.elemento.equals(elemento))
                throw new IllegalArgumentException();
        vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        Vertice vA = castVertice(vertice(a));
        Vertice vB = castVertice(vertice(b));
        if (sonVecinos(a,b) || a.equals(b))
            throw new IllegalArgumentException("Elementos ya conectados o iguales.");
        if (peso <= 0)
            throw new IllegalArgumentException("Se esperaba un peso positivo.");
        Vecino vecinoA = new Vecino(vA,peso);
        Vecino vecinoB = new Vecino(vB,peso);
        vA.vecinos.agrega(vecinoB);
        vB.vecinos.agrega(vecinoA);
        this.aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice vA = castVertice(vertice(a));
        Vertice vB = castVertice(vertice(b));
        if (!sonVecinos(a,b) || a.equals(b))
            throw new IllegalArgumentException("Elementos no conectados.");
        /* Primero tenemos A y buscamos a B en los vecinos de A. */        
        Vecino vab = null;
        Vecino vba = null;
        /* Buscamos a B en los vecinos de A. */
        for (Vecino ve : vA.vecinos)
            if (ve.vecino.equals(vB))
                vab = ve;
        /* Buscamos a A en los vecinos de B. */
        for (Vecino ve : vB.vecinos)
            if (ve.vecino.equals(vA))
                vba = ve;
        vA.vecinos.elimina(vab);
        vB.vecinos.elimina(vba);
        aristas--;
    }

    /**
    * Método axuliar que busca el vecino y lo regresa.
    * @param vertice El vértice en el que vamos a buscar el vecino.
    * @param elemento El elemento que debe tener el vecino a buscar.
    * @return el vecino del vertice, null en otro caso.
    */
    private Vecino buscaVecino(Vertice vertice, T elemento) {
        for (Vecino vecino : vertice.vecinos)
            if (vecino.getElemento().equals(elemento))
                return vecino;
        return null;
    }   

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice v : vertices)
            if (elemento.equals(v.elemento))
                return true;
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if (!contiene(elemento))
            throw new NoSuchElementException("El elemento no está en la gráfica.");
        Vertice vertice = castVertice(vertice(elemento));
        for (Vecino vecino : vertice.vecinos)
            desconecta(vecino.getElemento(),elemento);
        vertices.elimina(vertice);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if (!this.contiene(a) || !this.contiene(b))
            throw new NoSuchElementException("Algún elemento no está en la gráfica.");
        Vertice vA = castVertice(vertice(a));
        Vertice vB = castVertice(vertice(b));
        for (Vecino ve : vA.vecinos)
            if (ve.vecino.equals(vB))
                return true;
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if (!this.contiene(a) || !this.contiene(b))
            throw new NoSuchElementException("Algún elemento no está en la gráfica.");
        if (!sonVecinos(a,b))
            throw new IllegalArgumentException("Los elementos no están conectados.");
        return buscaVecino(castVertice(vertice(a)),b).peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        if (!this.contiene(elemento))
            throw new NoSuchElementException("El elemento no está en la gráfica.");
        for (Vertice v : vertices)
            if (v.elemento.equals(elemento))
                return v;
        return null;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorrido(elemento,new Cola<Vertice>(),accion);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorrido(elemento,new Pila<Vertice>(),accion);
    }
    
    private void recorrido(T elemento, MeteSaca<Vertice> meteSaca,
                             AccionVerticeGrafica<T> accion) {
        if (vertices.esVacio())
            return;
        Vertice v = castVertice(vertice(elemento));
        for (Vertice ve : vertices)
            ve.color = Color.NINGUNO;
        meteSaca.mete(v);
        while (!meteSaca.esVacia()) {
            v = meteSaca.saca();
            v.color = Color.ROJO;
            accion.actua(v);
            for (Vecino vv : v.vecinos) {
                if (vv.getColor() == Color.ROJO)
                    continue;
                vv.setColor(Color.ROJO);
                meteSaca.mete(vv.vecino);
            }
        }
        for (Vertice vv : vertices)
            vv.color = Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacio() {
        return this.vertices.getElementos() == 0;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        if (inalcanzables(origen,destino))
            return new Lista<VerticeGrafica<T>>();
        for (Vertice v : vertices)
            v.distancia = -1;
        Vertice ini = castVertice(vertice(origen));
        ini.distancia = 0;
        tm(ini);
        Vertice fin = castVertice(vertice(destino));
        Lista<Vertice> l = new Lista<Vertice>();
        Lista<VerticeGrafica<T>> t = new Lista<>();
        l.agrega(fin);
        l = reconstruyeTrayectoria(l,origen).reversa();
        for (Vertice v : l)
            t.agrega(v);
        return t;
    }

    private void tm(Vertice v) {
        Cola<Vertice> q = new Cola<Vertice>();
        q.mete(v);
        while (!q.esVacia()) {
            v = q.saca();
            for (Vecino u : v.vecinos) {
                if (u.vecino.distancia == -1) {
                    u.vecino.distancia = v.distancia + 1;
                    q.mete(u.vecino);
                }
            }
        }
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        if (inalcanzables(origen,destino))
            return new Lista<VerticeGrafica<T>>();
        for (Vertice v : vertices) {
            v.distancia = -1;
            v.color = Color.NINGUNO;
        }
        Vertice ini = castVertice(vertice(origen));
        ini.distancia = 0;
        MonticuloMinimo<Vertice> mm = new MonticuloMinimo<Vertice>();
        Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
        mm.agrega(ini);    
        while (!mm.esVacio()) {
            ini = mm.elimina();
            ini.color = Color.ROJO;
            for (Vecino ve : ini.vecinos) {
                if (ve.getColor() != Color.ROJO && 
                    ve.vecino.distancia < ini.distancia + ve.peso) {
                    ve.vecino.distancia = ini.distancia + ve.peso;
                    mm.agrega(ve.vecino);
                }
            }
        }
        Lista<Vertice> l = new Lista<>();
        Lista<VerticeGrafica<T>> t = new Lista<>();
        l.agrega(castVertice(vertice(destino)));
        l = reconstruyeTrayectoria(l,origen).reversa();
        for (Vertice v : l )
            t.agrega(v);
        return t;
    }


    /**
    * Reconstruye la trayectoria una vez que se actualizaron las distancias en 
    * los vértices de la gráfica.
    * @param l La lista que contiene el vértice desde donde empezar la construcción.
    * @param origen El elemento al que hay qué llegar.
    * @return una lista con la trayectoria más corta.
    */
    private Lista<Vertice> reconstruyeTrayectoria(Lista<Vertice> l,
                                                            T origen) {
        while (!l.getUltimo().elemento.equals(origen)) {
            MonticuloMinimo<Vertice> mm = new MonticuloMinimo<Vertice>();
            for (Vecino ve : l.getUltimo().vecinos) {
                if (ve.vecino.elemento.equals(origen)) {
                    Vertice v = new Vertice(origen);
                    v.distancia = ve.peso;
                    mm.agrega(v);
                } else mm.agrega(ve.vecino); 
           }
            l.agrega(mm.elimina());
            mm = new MonticuloMinimo<Vertice>();
        }
        return l;
    }

    /**
    * Nos dice si los dos elementos son inalcanzables.
    * @param origen El origen.
    * @param destino El destino.
    * @return <code>true</code> si son inalcanzables, <code>false</code> en el 
    * otro caso.
    */
    private boolean inalcanzables(T origen, T destino) {
        Vertice ini = castVertice(vertice(origen));
        Vertice fin = castVertice(vertice(destino));
        bfs(origen, (v) -> {});
        return ini.color != fin.color;
    }
}
