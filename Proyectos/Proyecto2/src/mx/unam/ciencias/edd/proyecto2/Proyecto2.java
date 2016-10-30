package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Pila;

public class Proyecto2 {

public static void main(String[] args) {
	Graficador graf = new Graficador();
	Lista<Integer> l = new Lista<Integer>();
	for (int i = 0; i < 11 ; i++  )
		l.agrega(i);
	System.out.println(graf.dibujaLista(l));
	Pila<Integer> p = new Pila<Integer>();
	for (int j = 0;j < 11 ;j++ )
			p.mete(j);
	System.out.println(graf.dibujaMeteSaca(p));
	}
}