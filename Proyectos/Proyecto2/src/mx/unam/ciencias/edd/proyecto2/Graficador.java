package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MeteSaca;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import java.lang.Math;

/**
* Clase pública Graficador que nos provee métodos para dibujar las estructuras 
* de datos en código SVG.
* Posee cadenas constantes para cada campo del archivo SVG.
*/
public class Graficador {

	/* La cadena del código SVG. */
	private String dibujo;
	/*Abre etiqueta xml.*/
	private static final String INICIO_XML = "<?xml version='1.0' encoding='UTF-"+
										   "8' ?>\n";
	/* Abre etiqueta svg. */
	private static final String INICIO_SVG = "<svg width='%d' height='%d'>\n";
	/* Cierra etiqueta svg. */
	private static final String CIERRA_SVG = "</svg>\n";
	/* Abre etiqueta g. */
	private static final String INICIO_G = "\t<g>\n";
	/* Cierra etiqueta g. */
	private static final String CIERRA_G = "\t</g>\n";
	/* Etiqueta para rectángulos. */
	private static final String RECT = "\t<rect x='%d' y='%d' width='%d' heigh"+
								       "t='%d'stroke='black' fill='white'/>\n";
    /* Etiqueta para texto. */
    private static final String TEXT = "\t<text fill='black' font-family='sans"+
    					    		   "-serif' font-size='8' x='%d' y='%d'te"+
    					    		   "xt-anchor='middle'>%s</text>\n";
    /* Etiqueta para conectar las listas. */
    private static final String CONECTOR_LISTA = "\t<text fill='black' font-fa"+
                                                 "mily='sans-serif' font-size="+
                                                 "'%d' x='%d' y='%d' text-anch"+
                                                 "or='middle'>%s</text>\n";
	/* Etiqueta para círculo. */
	private static final String CIRCLE = "\t<circle cx='%d' cy='%d' r='5' str"+
										 "oke='%s' fill='%s' stroke-width='1'/"+
										 ">\n";
	/* Etiqueta para línea. */
	private static final String LINE ="\t<line x1='%d' y1='%d' x2='%d' y2='%d'"+
	                                  "stroke-width='1' stroke='black'/>\n";         

    /**
    * Método para construir el código SVG para dibujar listas.
    * @return el código SVG para dibujar listas.
    */
	public String dibujaLista(Lista<?> l) {
		dibujo = "";
		dibujo += INICIO_XML;
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
		dibujo += INICIO_XML;
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

	public String dibujaArbolBinario(ArbolBinario<?> arbol) {
		dibujo = "";
		String lineas;
		int profundidad = arbol.profundidad() * 50;
		int anchura = arbol.getElementos() * 20;
		dibujo += String.format(INICIO_SVG,anchura,profundidad);
		dibujo += INICIO_G;
		VerticeArbolBinario<?> raiz = arbol.raiz();
		dibujaArbolBinario(raiz,0,0,anchura/2);
		dibujo += CIERRA_G;
		dibujo += CIERRA_SVG;
		return dibujo;
	}

	private void dibujaArbolBinario(VerticeArbolBinario<?> raiz, int ini, int y, int m) {
		dibujo += dibujaVertice(raiz,m,y+5);
		if (raiz.hayIzquierdo()) {
			int mi = (m - ini) / 2; 
			dibujo += dibujaLinea(m,y+5,m-mi,y+15);
			dibujaArbolBinario(raiz.getIzquierdo(),ini,y+15,m-mi);
		}
		if (raiz.hayDerecho()) {
			int md = (m - ini) / 2;
			dibujo += dibujaLinea(m,y+5,m+md,y+15);
			dibujaArbolBinario(raiz.getDerecho(),m,y+15,m+md);
		}
	}
	/*
	private void dibujaDerecho(VerticeArbolBinario<?> v, int inicio, int fin, int y) {

	}*/

	private String dibujaVertice(VerticeArbolBinario<?> v, int x, int y) {
		String vertice = String.format(CIRCLE,x,y,"black","white");
		vertice += String.format(TEXT,x,y+3,v.get().toString());
		return vertice;
	}

	private String dibujaConector(String cons, int size, int x, int y, 
									String conector) {
		return String.format(cons,size,x,y,conector);
	}

	private String dibujaLinea(int x1, int y1, int x2, int y2) {
		return String.format(LINE,x1,y1,x2,y2);
	}

}