package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

/**
* Clase GraficadorLista que nos permite graficar listas.
* Implementa la inferfaz <code>Graficador</code>.
*/

public class GraficadorLista<T> extends Lista<T> implements Graficable<T> {

	@Override
	public String dibuja(Graficable<T> g) {
		return g.dibuja(this);
	} 

}