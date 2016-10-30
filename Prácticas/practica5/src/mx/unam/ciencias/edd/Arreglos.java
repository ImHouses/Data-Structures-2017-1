package mx.unam.ciencias.edd;

/**
 * Clase para manipular arreglos genéricos de elementos comparables.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] a) {
      if (a.length == 1)
        return;
      quickSort(a, 0, a.length-1);
    }
    /*
    * Método privado auxiliar para el algoritmo QuickSort.
    *
    */
    private static <T extends Comparable<T>> void quickSort(T[] a, int i,
    int j) {
      if (i >= j)
        return;
      int ini = i+1;
      int fin = j;

      while (ini < fin) {
        if (a[i].compareTo(a[ini]) < 0 && a[fin].compareTo(a[i]) <= 0)
          swap(a,ini++,fin--);
        else if (a[ini].compareTo(a[i]) <= 0)
              ini++;
              else fin--;
      }
      if (a[ini].compareTo(a[i]) > 0)
        ini--;
      swap(a, i,ini);
      quickSort(a,i,ini-1);
      quickSort(a,ini+1,j);
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] a) {

      for (int i = 0;i < a.length ;i++ ) {
        int min = i;
        for (int j = i+1;j< a.length ;j++ ) {
          min = (a[j].compareTo(a[min]) < 0) ? j : min;
        }
        swap(a,i,min);
      }
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e) {
      if (a.length == 1)
        return (e.compareTo(a[0]) == 0) ?  0 :  -1;
      if (a.length == 0)
        return -1;
      return busquedaBinaria(a, e, 0, a.length-1);
    }

    private static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e,
    int i, int j) {
      if (j < i)
        return -1;
      int m = (i + j) / 2;
      if (a[m].compareTo(e) == 0)
        return m;
      return a[m].compareTo(e) < 0 ? busquedaBinaria(a,e,m+1,j) :
                                      busquedaBinaria(a,e,i,m-1);
    }

    private static <T extends Comparable<T>> void swap(T[] a, int i, int j) {
      T e = a[i];
      a[i] = a[j];
      a[j] = e;
    }
}
