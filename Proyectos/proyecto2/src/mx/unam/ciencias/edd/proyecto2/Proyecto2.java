package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.Grafica;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class Proyecto2 {

	static Graficador graficador;

	public static void main(String[] args) {

	String estructura = "";
	String archivo = "";
	graficador = new Graficador();
	/* Si no tenemos nombre de archivo leemos de la entrada standard. */
	if (args.length == 0) {
			estructura = leeArchivo(System.in);
			if (estructura.equals("")) {
				System.err.println("No hay archivo.");
				System.exit(1);
			} else dibujaEstructura(estructura);
			/* Hay archivo. */
		} else if (args.length == 1) {
			try {
			estructura = leeArchivo(new FileInputStream(
				new File(args[0])));
			if (estructura.equals("")) {
				System.err.println("Archivo inválido");
				System.exit(1);
			}
			dibujaEstructura(estructura);
			} catch(IOException ioe) {
				System.err.println("Archivo inválido.");
				System.exit(1);
			}
		}
	}

	private static void dibujaEstructura(String estructura) {
		String e = estructura.trim();
		String[] lineas = e.split("\n");
		String clase = lineas[0];
		Lista<Integer> elementos = interpretaElementos(lineas[1]);
		String relaciones;
		if (clase.contains("ArbolRojinegro")) {
			ArbolRojinegro<Integer> rojinegro = new ArbolRojinegro<>();
			for(Integer n : elementos)
				rojinegro.agrega(n);
			System.out.println(graficador.dibujaArbolBinario(rojinegro));
		} else if(clase.contains("ArbolAVL")) {
			ArbolAVL<Integer> avl = new ArbolAVL<>();
			for (Integer n : elementos)
				avl.agrega(n);
			System.out.println(graficador.dibujaArbolBinario(avl));
		} else if (clase.contains("ArbolBinarioCompleto")) {
			ArbolBinarioCompleto<Integer> completo = 
			new ArbolBinarioCompleto<>(elementos);
			System.out.println(graficador.dibujaArbolBinario(completo));
		} else if (clase.contains("ArbolBinarioOrdenado")) {
			ArbolBinarioOrdenado<Integer> ordenado = 
			new ArbolBinarioOrdenado<>(elementos);
			System.out.println(graficador.dibujaArbolBinario(ordenado));
		} else if (clase.contains("Cola")) {
			Cola<Integer> cola = new Cola<>();
			for (int n : elementos)
				cola.mete(n);
			System.out.println(graficador.dibujaMetesaca(cola));
		} else if (clase.contains("Pila")) {
			Pila<Integer> pila = new Pila<>();
			for ( int n : elementos )
				pila.mete(n);
			System.out.println(graficador.dibujaMetesaca(pila)); 
		} else if (clase.contains("Lista")) {
			System.out.println(graficador.dibujaLista(elementos));
		} else if (lineas.length == 3) {
			relaciones = "";
			for (int i = 2; i < lineas.length; i++)
				relaciones += lineas[i];
			System.out.println(graficador.dibujaGrafica(
														interpretaRelaciones(
																relaciones,
																elementos))); 
		} else System.err.println("Archivo inválido.");
	}

	private static Lista<Integer> interpretaElementos(String elementos) {
		String[] e = elementos.split(",");
		Lista<Integer> r = new Lista<>();
		for (String s : e ) {
			r.agrega(Integer.parseInt(s.trim()));
		}
		return r;
	}

	private static Grafica<Integer> interpretaRelaciones(String relaciones,
													Lista<Integer> elementos) {
		Grafica<Integer> g = new Grafica<>();
		String[] r = relaciones.split(";");
		try {
		for (String s : r ) {
			String[] e = s.trim().split(",");
			int a = Integer.parseInt(e[0]);
			int b = Integer.parseInt(e[1]);
			if (!g.contiene(a))
			g.agrega(a);
			if (!g.contiene(b))
			g.agrega(b);
			g.conecta(a,b);
			elementos.elimina(a);
			elementos.elimina(b);
		}
		for (int n : elementos )
			g.agrega(n);
		} catch(IllegalArgumentException iae) {
			System.err.println("Archivo inválido.");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException aindex) {
			System.err.println("Archivo inválido.");
			System.exit(1);
		}
		return g;
	}

	private static String leeArchivo(InputStream is) {
		String s = "";
		try {
			String l;
			BufferedReader in = new BufferedReader(
								new InputStreamReader(
								is));
			while ((l = in.readLine()) != null) {
				if (l.equals(""))
					break;
				if (l.length() == 0)
					continue;
				s += l+"\n";
			}
			in.close();
		}
	 catch (IOException ioe) {
		System.err.printf("Ocurrió un error al tratar de abrir el archivo.");	
	}
		return s;
	}
}