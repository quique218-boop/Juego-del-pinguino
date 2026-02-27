package juegoDelPinguinoExtremo;

public class Agujero extends Casilla {

	public Agujero() {}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		boolean encontrado = false;

		for (int i = jugador.getPos() - 1; i > 0 && encontrado == false; i--) {

			if (tablero.getCasilla(i) instanceof Agujero) {
				jugador.setPos(i);
				encontrado = true;
			}
		}
	}
}
