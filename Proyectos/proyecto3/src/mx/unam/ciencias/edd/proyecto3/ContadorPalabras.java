package mx.unam.ciencias.edd.proyecto3;

import java.io.File;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;

public class ContadorPalabras {

	/* Para leer archivos. */
	private Lector lector;
	/* Archivo. */
	private String archivo;
	/* Texto original. */
	private String texto;
	/* Diccionario. */
	private Diccionario<String, Palabra> d;
	/* Total de palabras. */
	private int total;	

	/* Constructor sin parámetros.*/
	public ContadorPalabras(String archivo) {
		this.lector = new Lector(new File(archivo));
		this.archivo = archivo;
		this.d = new Diccionario<>();
		this.texto = "";
		this.total = 0;
	}

	/**
	* Regresa una lista ordenada según las coincidencias de las palabras.
	* @return la lista con las palabras ordenadas.	
	*/
	public Lista<Palabra> cuenta() {
		String s = null;
		while ((s = lector.leeLinea()) != null) {
			texto += s + "\n";
			String[] palabrasLinea = s.split("\\s+");
			for (String p : palabrasLinea) {
				if (p.trim().isEmpty())
					continue; 
				total++;
				Palabra palabra = new Palabra(p.toLowerCase());
				String llave = palabra.getPalabra();
				if (d.contiene(llave)) {
					int o = d.get(llave).getOcurrencias() + 1;
					d.get(llave).setOcurrencias(o);
				} else {
					d.agrega(llave, palabra);
				}
			}
		}
		Lista<Palabra> lp = new Lista<>();
		for (Palabra p : d) {
			double veces = p.getOcurrencias();
			double t = total;
			p.setPorcentaje((veces / t) * 100.0);
			lp.agrega(p);
		}
		return Lista.mergeSort(lp);
	}

	/**
	* Regresa el nombre del archivo.
	* @return el nombre del archivo.
	*/
	public String getArchivo() {
		return this.archivo;
	}

	/**
	* Regresa el texto original.
	* @return el texto original.
	*/
	public String getTexto() {
		return this.texto;
	}

	/**
	* Regresa el diccionario.
	* @return el diccionario.
	*/
	public Diccionario<String, Palabra> getDiccionario() {
		return this.d;	
	}

	/**
	* Regresa el total de palabras.
	* @return el total de palabras.
	*/
	public int getTotal() {
		return this.total;
	}

}