package mx.unam.ciencias.edd.proyecto3;

import java.lang.StringBuffer;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.ArbolAVL;

public class GeneradorHTML {
	
	private static final String ABRE_HTML = "<!DOCTYPE html>\n"
											+ "<html>\n"
											+ "<head>\n"
											+ "<title>%s</title>\n"
											+ "<meta charset=\"UTF-8\">"
											+ "</head>\n"
											+ "<body>\n";
	private static final String H1 = "\t<h1>%s</h1>\n";
	private static final String PAR = "\t<p>%s</h1>\n";
	private static final String CIERRA_HTML = "</body>\n"
												+ "</html>";
    
    /**
    * Genera un HTML con los datos del reporte.
    * @return la cadena con el HTML.
    */
	public static String genera(ReporteTexto r) {
		/* Por el momento el método sólo genera un HTML con el texto original 
			y las coincidencias. */
		StringBuffer s = new StringBuffer();
		Graficador g = new Graficador();
		s.append(String.format(ABRE_HTML, r.getTitulo()));
		s.append(String.format(H1, "Texto original."));
		s.append(String.format(PAR,"<i>Total de palabras:</i> " + r.getTotal()));
		s.append(String.format(PAR, r.getTexto().replaceAll("\n","<br>\n")));
		s.append(String.format(H1, "Coincidencias de palabras"));
		StringBuffer c = new StringBuffer();
		for (Palabra p : r.getCoincidencias())
			c.append("\t" + p.getInfo() + "<br>\n");
		s.append(String.format(PAR,c.toString()));
		s.append(String.format(H1, "Gráfica de barras."));
		s.append(g.barras(r.getTop()));
		s.append(String.format(H1, "Árbol rojinegro."));
		int i = 1;
		ArbolRojinegro<Palabra> rojinegro = new ArbolRojinegro<Palabra>();
		ArbolAVL<Palabra> avl = new ArbolAVL<>();
		for (Palabra p : r.getCoincidencias().reversa()) {
			if (i != 16) {
				rojinegro.agrega(p);
				avl.agrega(p);
				i++;
			}
		}
		s.append(g.dibujaArbolBinario(rojinegro));
		s.append(String.format(H1, "Árbol AVL"));
		s.append(g.dibujaArbolBinario(avl));
		s.append(CIERRA_HTML);
		return s.toString();
	}

	/**
	* Genera el index con los reportes.
	* @param la lista de los reportes que se van a indexar.
	* @return el index.
	*/
	public static String generaIndex(Lista<ReporteTexto> reportes) {
		return null;
	}
}