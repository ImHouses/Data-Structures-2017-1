package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null)
          throw new IllegalArgumentException("El elemento es null.");
        Nodo n = new Nodo(elemento);
        if (esVacia()) {
          cabeza = n;
          rabo = n;
        } else {
          rabo.siguiente = n;
          rabo = n;
        }
    }
}
