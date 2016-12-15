package mx.unam.ciencias.edd.proyecto2;

/**
* Clase para excepciones de archivos inválidos.
*/
public class ExcepcionArchivoInvalido extends IllegalArgumentException {

	/**
	* Constructor vacío.
	*/
	public ExcepcionArchivoInvalido() {}

	/**
	* Constructor que recibe un mensaje para el usuario.
	* @param mensaje.
	*/
	public ExcepcionArchivoInvalido(String mensaje) {
		super(mensaje);
	}
}