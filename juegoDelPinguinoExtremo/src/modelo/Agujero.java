package modelo;

public class Agujero extends Casilla {

	public Agujero() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		boolean encontrado = false;

		/*
		 * Cuando un jugador cae en un agujero se realiza un bucle
		 * que recorre el tablero hacia atrás desde la posición actual
		 * del jugador buscando otro agujero.
		 *
		 * Si encuentra otro agujero:
		 * - Se reproduce el sonido.
		 * - Se mueve al jugador a esa posición.
		 * - Se cambia la variable "encontrado" para detener el bucle.
		 *
		 * En caso contrario, el jugador vuelve al inicio del tablero.
		 */

		
		for (int i = jugador.getPos() - 1; i > 0 && encontrado == false; i--) {

			if (tablero.getCasilla(i) instanceof Agujero) {
				efectos_de_sonido.sonidoAgujero();
				jugador.setPos(i);
				encontrado = true;
			}
		}

		if (encontrado == false)
			jugador.setPos(0);
	}
}
