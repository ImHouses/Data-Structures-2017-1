package mx.unam.ciencias.edd;

import java.lang.Math;

/**
 * Clase para poder utilizar comparables indexables.
 */
public class Indexable<T> implements ComparableIndexable<Indexable<T>> {

    /* El elemento */
    private T elemento;
    /* Su valor */
    private double valor;
    /* Su índice. */
    private int indice;

    /**
     * Crea un nuevo indexable con el elemento y valor dados.
     * @param elemento el elemento.
     * @param valor su valor.
     */
    public Indexable(T elemento, double valor) {
        this.elemento = elemento;
        this.valor = valor;
        this.indice = -1;
    }

    /**
     * Regresa el elemento del indexable.
     * @return el elemento del indexable.
     */
    public T getElemento() {
        return this.elemento;
    }

    /**
     * Compara el indexable con otro indexable.
     * @param indexable el indexable.
     * @return un valor menor que cero si el indexable que llama el método es
     *         menor que el parámetro; cero si son iguales; o mayor que cero si
     *         es mayor.
     */
    @Override public int compareTo(Indexable<T> indexable) {
        if (this.valor < indexable.valor)
            return -1;
        if (this.valor > indexable.valor)
            return 1;
        return 0;
    }

    /**
     * Define el índice del indexable.
     * @param indice el nuevo índice.
     */
    @Override public void setIndice(int indice) {
        this.indice = indice;
    }

    /**
     * Regresa el índice del indexable.
     * @return el índice del indexable.
     */
    @Override public int getIndice() {
        return this.indice;
    }

    /**
     * Define el valor del indexable.
     * @param valor el nuevo valor.
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * Regresa el valor del indexable.
     * @return el valor del indexable.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Nos dice si el indexable es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si el indexable es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Indexable))
            return false;
        @SuppressWarnings("unchecked") Indexable<T> indexable = (Indexable<T>)o;
        return (this.valor == indexable.valor && this.indice == indexable.indice);
    }
}
