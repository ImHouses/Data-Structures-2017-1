package mx.unam.ciencias.edd.proyecto1;

/**
* <p>Clase Texto para textos</p>
* 
* <p>Los textos permiten ordenarse de la A a la Z o en sentido 
* invertido.</p>
* 
* <p> Los textos se componen de lineas por lo que la clase tiene una clase
* 	  interna para las lineas. </p>
*/
public class Texto {

		/**
		* <p>Clase que representa una linea de caracteres en el 
		* intervalo a-z.
		* <br>
		* Las lineas se pueden comparar respecto a su orden en el 
		* código ASCII.
		* </p>
		* <p>Ĺas lineas se comparan respecto a su primer caracter y se
		* ignora siempre a los caracteres que no forman parte del 
		* intervalo a-z y A-Z tomando el siguiente caracter en la 
		* cadena como referencia.</p>
		*/

	private class Linea implements Comparable<Linea> {

		

		/* Representacion de la linea.*/
		private String linea;
		/* Caracter inicial. */
		private char inicial;
		/* El orden de cada linea. */
		private int orden;
		/* Linea a comparar. */
		private String lineaComp;

		/* Constructor*/
		public Linea(String linea) {
			 this.linea = linea;
			 this.lineaComp = linea.toLowerCase();
			 this.inicial = lineaComp.charAt(0);
			 int o = (char) inicial;
			 for (int i = 0 ; i < linea.length() ; i++) {
			 	if (o >= 97 && o <= 122) {
			 		this.orden = inicial;
			 		return;
			 	}
			 	this.inicial = linea.charAt(i);
			 	o = (char) inicial;
			 }
		}

		/**
		* Método para hacer comparaciones entre las lineas con el orden ASCII.
		* Si el parámetro de linea es mayor que el objeto que manda a llamar el
		* método
		* el método regresa un número menor a 0.
		* Si el parámetro de linea es menor que el objeto que manda a llamar el
		* método
		* el método regresa un número mayor a 0.
		* Si los dos objetos son iguales, regresa 0.
		* @return número mayor a 0 si el parámetro es menor, menor a 0 si es
		* mayor, 0 si son iguales.
		*/
		public int compareTo(Linea l) {
			return this.orden - l.orden;
		}

		/**
		* Método toString Regresa una representación en cadena del objeto.
		* @return linea Que es la representación del objeto.
		*/
		public String toString() {
			return linea;
		}
	}
	/* El texto */
	private String texto;
	/* El texto en lineas */
	private Lista<Linea> textoLineas;

	/**
	* Constructor único de textos.
	* @param Una cadena que representa un texto.
	*/
	public Texto(String texto) {
		textoLineas = new Lista<Linea>();
		this.texto = texto;
		String[] a = texto.split("\n");
		for (String s : a )
			textoLineas.agregaFinal(new Linea(s));
	}
	/**
	* Método para ordenar un texto de la A a la Z. Lo ordena y regresa
	* una cadena con el texto ordenado.
	* @return la cadena del texto ordenado.
	*/
	public String ordenaTexto() {
		Lista<Linea> l = Lista.mergeSort(textoLineas);
		String m = "";
		for (Linea s : l)
			m += "\n" + s.toString();
		return m;
	}

	/**
	* Método para ordenar en reversa un texto de la Z a la A. Lo ordena y
	* regresa una cadena con el texto ordenado.
	* @return la cadena del texto ordenado e invertido.
	*/
	public String reversa() {
		Lista<Linea> l = Lista.mergeSort(textoLineas);
		l = l.reversa();
		String m = "";
		for (Linea s : l )
			m += "\n" + s.toString();
		return m;
	}
}
