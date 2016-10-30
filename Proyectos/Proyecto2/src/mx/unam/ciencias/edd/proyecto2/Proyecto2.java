package mx.unam.ciencias.edd.proyecto2;

public class Proyecto2 {

public static void main(String[] args) {
	Graficador graf = new Graficador();
	GraficadorLista<Integer> gli = new GraficadorLista<Integer>();
	for (int i = 0; i < 10 ; i++  )
		gli.agrega(i);
	System.out.println(graf.dibuja(gli));
	}
}