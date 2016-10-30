package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MeteSaca;

/**
* Interface Graficador que nos provee un sólo método para dibujar en código SVG.
*/
public class Graficador {

	private String dibujo;
	private static final String START_SVG = "<svg width='%d' height='%d'>\n";
	private static final String END_SVG = "</svg>\n";
	private static final String START_G = "\t<g>\n";
	private static final String END_G = "\t</g>\n";
	private static final String RECT =	"\t<rect x='%d' y='%d' width='%d' height='%d'" +
								"stroke='black' fill='white'/>\n";
    private static final String TEXT = "\t<text fill='black' font-family='sans-serif' font-size='10' x='%d' y='13'" +
             "text-anchor='middle'>%s</text>\n";


	public String dibujaLista(Lista<?> l) {
		dibujo = "";
		int longitud = 50*l.getLongitud() + 10*l.getLongitud()-1;
		int altura = 20;
		int longitudNodo = 50;
		dibujo += String.format(START_SVG,longitud,altura);
		dibujo += START_G;
		int i = 0;
		for (Object e : l){ 
			dibujo += String.format(RECT,60*i,0,longitudNodo,altura);
			dibujo += String.format(TEXT,60*i+25,e.toString());
			i++;
		}
		dibujo += END_G;
		dibujo += END_SVG;
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
		dibujo += String.format(START_SVG,50,alturaTotal);
		dibujo += START_G;
		for (String s : lineas)
			dibujo += s;
		dibujo += END_G;
		dibujo += END_SVG;
		return dibujo;
	}

}