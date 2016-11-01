package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MeteSaca;

/**
* Clase pública Graficador que nos provee métodos para dibujar las estructuras 
* de datos  en código SVG.
* Posee cadenas constantes para cada campo del archivo SVG.
*/
public class Graficador {

	private String dibujo;
	/* Abre etiqueta svg. */
	private static final String INICIO_SVG = "<svg width='%d' height='%d'>\n";
	/* Cierra etiqueta svg. */
	private static final String CIERRA_SVG = "</svg>\n";
	/* Abre etiqueta g. */
	private static final String INICIO_G = "\t<g>\n";
	/* Cierra etiqueta g. */
	private static final String CIERRA_G = "\t</g>\n";
	/* Etiqueta para rectángulos. */
	private static final String RECT =	"\t<rect x='%d' y='%d' width='%d' height='%d'" +
								"stroke='black' fill='white'/>\n";
    /* Etiqueta para texto. */
    private static final String TEXT = "\t<text fill='black' font-family='sans-serif' font-size='10' x='%d' y='%d'" +
             "text-anchor='middle'>%s</text>\n";
    /* Etiqueta para conectar las listas. */
    private static final String CONECTOR_LISTA = "\t<text fill='black' font-family='sans-serif' font-size='%d' x='%d' y='%d'" +
             "text-anchor='middle'>%s</text>\n";         

    /**
    * Método para construir el código SVG para dibujar listas.
    * @return el código SVG para dibujar listas.
    */
	public String dibujaLista(Lista<?> l) {
		dibujo = "";
		int longitud = 50*l.getLongitud() + 10*l.getLongitud()-1;
		dibujo += String.format(INICIO_SVG,longitud,20);
		dibujo += INICIO_G;
		int i = 0;
		for (Object e : l){ 
			dibujo += String.format(RECT,60*i,0,50,20);
			if (i+1 < l.getLongitud())
				dibujo += dibujaConector(CONECTOR_LISTA,10,60*(i+1)-5,13,
											"&#x2194;");
			dibujo += String.format(TEXT,60*i+25,13,e.toString());
			i++;
		}
		dibujo += CIERRA_G;
		dibujo += CIERRA_SVG;
		return dibujo;
	}

	public String dibujaMeteSaca(MeteSaca<?> ms) {
		dibujo = "";
		Lista<String> lineas = new Lista<String>();
		int alturaTotal;
		int i = 0;
		while (!ms.esVacia()) {
			Object e = ms.saca();
			lineas.agrega(String.format(RECT,0,30*i,50,20));
			i++;
		}
		alturaTotal = 20*(i+1) + 10*i;
		dibujo += String.format(INICIO_SVG,50,alturaTotal);
		dibujo += INICIO_G;
		for (String s : lineas)
			dibujo += s;
		dibujo += CIERRA_G;
		dibujo += CIERRA_SVG;
		return dibujo;
	}

	private String dibujaConector(String cons, int size,int x, int y, 
									String conector) {
		return String.format(cons,size,x,y,conector);
	}

}