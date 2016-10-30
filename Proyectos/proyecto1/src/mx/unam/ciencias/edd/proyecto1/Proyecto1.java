package mx.unam.ciencias.edd.proyecto1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class Proyecto1 {

	static String bandera = "";

	public static void main(String[] args) {
		
		Lista<Texto> t;
		OrdenadorLexicografico ol;
		Lista<String> resultado;

		/*
		* Si no nos pasan un archivo de texto como parámetro.
		*/
		if (args.length == 0) {
			System.out.println("Escribe por líneas para terminar escribe /END");
			resultado = new Lista<String>();
			t = new Lista<Texto>();
			t.agregaFinal(new Texto(leeArchivo(System.in)));
			ol = new OrdenadorLexicografico(t);
			resultado = ol.ordena();
			for (String s : resultado ) {
				System.out.printf(s);
			System.exit(1);
			}		
		}

		/* Si hay parámetros. */
		if (args.length >= 1){
			resultado = new Lista<String>();
			t = leeArchivos(args);
			ol = new OrdenadorLexicografico(t);

			if(bandera.equals("-r")){
				resultado = ol.reversa();
				for (String s : resultado )
					System.out.printf(s+"\n");

			} else {
			resultado = ol.ordena();
			for (String s : resultado ) {
				System.out.printf(s+"\n");
				}
			}
			System.exit(1);
		}
	}

	private static Lista<Texto> leeArchivos(String[] args) {
		Lista<Texto> lt = new Lista<Texto>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-r")) {
				bandera = args[i];
				break;
			}
			try {
			lt.agregaFinal(new Texto(
			leeArchivo(
			new FileInputStream(
			new File(
			args[i])))));
			} catch(IOException ioe) {
				System.out.printf("Ocurrió un error al tratar de leer uno " + 
								  "de los archivos, se descartará.");
				continue;
			}
		}
		return lt; 
	}

	private static String leeArchivo(InputStream is) {
		String s = "";
		try {
			String l;
			BufferedReader in = new BufferedReader(
								new InputStreamReader(
								is));
			while (true) {
				l = in.readLine();
				if (l == null)
					break;
				if (l.equals("/END"))
					break;
				if (l.length() == 0)
					continue;
				s += l+"\n";
			}
		}
	 catch (IOException ioe) {
		System.out.printf("Ocurrió un error al tratar de abrir un archivo.");	
	}
		return s;
	}
}
