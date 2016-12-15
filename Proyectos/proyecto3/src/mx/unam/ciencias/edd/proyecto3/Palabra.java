package mx.unam.ciencias.edd.proyecto3;

import java.text.Normalizer;

/**
* Clase privada para objetos de tipo <code>Palabra</code>. Nuestras palabras
* tienen un contador llamado <code>ocurrencias</code> para contar el número
* de las mismas.
*/
public class Palabra implements Comparable<Palabra> {

	/* Representa la palabra. */
	private String palabra;
	/* El número de ocurrencias. */
	private int ocurrencias;
	/* El porcentaje. */
	private double porcentaje;

	/**
	* Constructor que normaliza la palabra para no tener qué lidiar con
	* los caracteres especiales.
	*/
	public Palabra(String p) {
		this.palabra = Normalizer
							.normalize(p,Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]","")
							.replaceAll("[.¡!¿?/#$&()=\\-,'\"]","");
		this.ocurrencias = 1;
	}
	/**
	* Compara palabras usando el número de ocurrencias; si el parámetro
	* es la misma palabra que esta entonces se incrementa el contador, si no
	* entonces regresa el valor tal cual.
	* 
	*/
	@Override
	public int compareTo(Palabra p) {
		p.palabra = Normalizer
				.normalize(p.palabra,Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]","")
				.replaceAll("[.¡!¿?/#$&()=\\-,'\"]","");
		return (p.ocurrencias == ocurrencias) 
				? this.palabra.compareTo(p.palabra) 
				: this.ocurrencias - p.ocurrencias;
	}

	/**
	* Regresa la palabra.
	* @return la palabra.
	*/
	public String getPalabra() {
		return this.palabra;
	}

	/**
	* Regresa el número de ocurrencias.
	* @return el número de ocurrencias.
	*/
	public int getOcurrencias() {
		return this.ocurrencias;
	}

	/**
	* Establece un valor nuevo al número de ocurrencias.
	* @param o El nuevo número de ocurrencias.
	*/
	public void setOcurrencias(int n) {
		this.ocurrencias = n;
	}

	/**
	* Regresa la palabra con las ocurrencias.
	* @return la palabra con las ocurrencias en cadena.
	*/
	public String toString() {
		return String.format("'%s' Se repite: %d veces",
							this.palabra,this.ocurrencias);
	}

	/**
	* Regresa el porcentaje de aparición de la palabra.
	* @return el porcentaje.
	*/
	public double getPorcentaje() {
		return this.porcentaje;
	}

	/**
	* Establece el porcentaje de aparición de la palabra.
	* @param el porcentaje.
	*/
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

}