package mx.unam.ciencias.edd.proyecto3;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
* Clase pública para leer entradas y salidas.
* Proporciona métodos para leer cadenas, enteros, archivos y así mismo también
* cuenta con métodos para escribirlos.
* Cuenta con métodos para escribir y leer archivos.
*/
public class Lector {

	/* La entrada. */
	private InputStream input;
	/* La salida. */
	private OutputStream output;
	/* Para leer lineas. */
	private BufferedReader br;
	/* Para escribir líneas.*/
	private BufferedWriter bw;
	/* Un archivo. */
	private File archivo;

	/* Constructor sin parámetros. */
	public Lector() {}

	/* Constructor para usos de entradas. */
	public Lector(InputStream is) {
		this.input = is;
		this.br = new BufferedReader(
					new InputStreamReader(is));
	}

	/* Constructor para escritura y con un archivo. */
	public Lector(InputStream is, File archivo) {
		try {
		this.input = is;
			this.br = new BufferedReader(
						new InputStreamReader(is));
			this.bw = new BufferedWriter(
						new OutputStreamWriter(
							new FileOutputStream(archivo)));
		} catch(FileNotFoundException fnfe) {
			System.err.println("No se encuentra el archivo especificado.");
		}
	}

	/* Constructor para usos de salidas. */
	public Lector(OutputStream os) {
		this.output = os;
	}

	/* Constructor para leer archivos. */
	public Lector(File archivo) {
		try {
			this.archivo = archivo;
			if (archivo.isDirectory())
				throw new IOException("Se esperaba un archivo.");
			this.br = new BufferedReader(
						new FileReader(archivo));
		} catch(FileNotFoundException fnfe) {
			System.err.println("No se encuentra un archivo.");
		} catch(IOException ioe) {
			System.err.println("Problema de entrada/salida.");
		}
	}

	/**
	* Establece la entrada del Lector manualmente.
	* @param InputStream la entrada.
	*/
	public void setEntrada(InputStream is) {
		this.br = new BufferedReader(
					new InputStreamReader(is));
	}

	/**
	* Establece la salida del Lector manualmente.
	* @param OutputStream la salida.
	*/
	public void setSalida(OutputStream os) {
		this.bw = new BufferedWriter(
					new OutputStreamWriter(os));
	}


	/**
	* Regresa el siguiente entero que se encuentre en la entrada.
	* @return el siguiente entero que se encuentre en la entrada.
	* @throw IOException en caso de que haya un problema con la entrada.
	* @throw NumberFormatException en caso de que la entrada no contenga un int.
	*/	
	public int siguienteInt() throws IOException, NumberFormatException {
		return Integer.parseInt(br.readLine());
	}

	/**
	* Escribe un entero en la entrada.
	* @param n El número que se va a mandar a la salida.
	* @throw IOException en caso de que haya un problema con la salida.
	*/
	public void escribeNumero(Integer n) throws IOException {
			bw.write(n.toString());
			bw.flush();
	}

	/**
	* Escribe una línea en la salida.
	* @param linea La línea que se va a escribir en la salida.
	* @throw IOException en caso de que haya un problema al escribir la salida.
	*/
	public void escribeLinea(String linea) throws IOException {
		bw.write(linea);
		bw.flush();
	}

	/**
	* Lee una linea.
	* @return la linea La línea que se escribe en la entrada.
	* @throw IOException en caso de que haya un problema al recibir la entrada.
	*/
	public String leeLinea() {
		String s = null;
		try {
		s = br.readLine();
		} catch (IOException ioe) {
			System.err.println("Ocurrió un problema de entrada/salida");
		}
		return s;
	}
}