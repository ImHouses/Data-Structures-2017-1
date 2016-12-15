package mx.unam.ciencias.edd.proyecto3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import mx.unam.ciencias.edd.proyecto3.Lector;
import mx.unam.ciencias.edd.Lista;

public class Proyecto3 {
	public static void main(String[] args) {
		//Probando, no es definitivo.
		String archivo = args[0];
		String directorio = args[1];
		ReporteTexto reporte = new ReporteTexto(archivo);
		String r = GeneradorHTML.genera(reporte);
		File dir = new File(directorio); 
		dir.mkdir();
		Lector l = new Lector();
		try {
		String nombre = archivo.split("\\.")[0];
		l.setSalida(new FileOutputStream(
						new File(directorio + "/" + nombre + ".html")));
		l.escribeLinea(r);
		} catch (FileNotFoundException fnfe) {
			System.out.println("Algún archivo no existe.");
		} catch (IOException ioe) {
			System.out.println("Ocurrió un error de entrada y/o salida.");
		}
		
	}

	private static void prueba1() {
		ContadorPalabras cp = new ContadorPalabras("archivo.txt");
		long tiempoInicial;
		long tiempoFinal;
		long tiempoTotal;
		tiempoInicial = System.nanoTime();
		Lista<Palabra> lp = cp.cuenta();
		tiempoFinal = System.nanoTime() - tiempoInicial;
        tiempoInicial = System.nanoTime();
        for (Palabra p : lp)
        	System.out.println(p);
       tiempoTotal = System.nanoTime() - tiempoInicial;
       System.out.printf("\n%2.9f segundos en imprimir las palabras."
       					 ,(tiempoTotal/1000000000.0));
       System.out.printf("\n%2.9f segundos en contar las palabras en " +
                          "un archivo de texto.\n",
                          (tiempoFinal/1000000000.0));
       System.out.printf("Número de palabras (sin repeticiones): %d",
       						lp.getElementos());
	}
}	