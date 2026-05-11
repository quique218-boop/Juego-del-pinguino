/*Guarda el nombre del jugador y la posición a la que
 * se ha movido.*/

package modelo;

public class PairMovimiento {

	public final String jugador;
	public final int posicion;

	public PairMovimiento(String jugador, int posicion) {
		this.jugador = jugador;
		this.posicion = posicion;
	}
}
