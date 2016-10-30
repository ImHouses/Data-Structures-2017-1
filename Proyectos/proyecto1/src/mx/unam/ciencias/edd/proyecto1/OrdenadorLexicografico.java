package mx.unam.ciencias.edd.proyecto1;

import java.lang.IllegalArgumentException;
/**
*	<p>Clase para ordenadores lexicograficos. <br>
*	Los ordenadores lexicograficos nos permiten ordenar un conjunto de lineas
*	respecto a su orden en el alfabeto.</p>
*
*/
public class OrdenadorLexicografico {

	/* Los textos */
	private Lista<Texto> textos;
	/* Los textos ordenados */
	private Lista<String> ordenados;

	/**
	* Constructor de la clase.
	* @param <code>texto<code> Los textos a ordenar.
	*/
	public OrdenadorLexicografico(Lista<Texto> textos) {
		this.textos = textos;
		this.ordenados = new Lista<String>();
	}

	/**
	* Ordena el texto de la A a la Z y devuelve
	* una cadena que lo representa.
	* @return la cadena del texto ordenado.
	*/
	public Lista<String> ordena() {
		for (Texto t : textos )
			ordenados.agregaFinal(t.ordenaTexto());
		return ordenados;
	}

	/**
	* Ordena el texto en reversa de la Z a la A y devuelve
	* una cadena que lo representa.
	* @return la cadena del texto ordenado en reversa.
	*/
	public Lista<String> reversa() {
		for (Texto t : textos )
			ordenados.agregaFinal(t.reversa());
		return ordenados;
	}
}
