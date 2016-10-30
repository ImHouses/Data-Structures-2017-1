package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

/**
* Interface Graficador que nos provee un sólo método para dibujar en código SVG.
*/
public class Graficador {

	private GraficadorLista<?> grafl;
	private String dibujo;
	private static final String START_SVG = "<svg width='%d' height='%d'>\n";
	private static final String END_SVG = "</svg>\n";
	private static final String START_G = "\t<g>\n";
	private static final String END_G = "\t</g>\n";
	private static final String RECT =	"\t<rect x='%d' y='0' width='%d' height='20'" +
								"stroke='black' fill='white'/>\n";


	/**
	* Método para dibujar en código SVG.
	* @param 
	* @return El código SVG para dibujar.
	*/
	public String dibuja(Graficable<?> estructura) {
		String s;
		 @SuppressWarnings("unchecked") GraficadorLista<?> g = 
		 (GraficadorLista<?>)estructura;
		 grafl = g;
		return g.dibuja((gr) -> {
			return dibujaLista();
		});
	}

	public String dibujaLista() {
		dibujo = "";
		int longitud = 50*grafl.getLongitud() + 10*grafl.getLongitud()-1;
		int altura = 20;
		int longitudNodo = 50;
		dibujo += String.format(START_SVG,longitud,altura);
		dibujo += START_G;
		for (int i = 0;i < grafl.getLongitud();i++ )
			dibujo += String.format(RECT,60*i,longitudNodo);
		dibujo += END_G;
		dibujo += END_SVG;
		return dibujo;
	}

}