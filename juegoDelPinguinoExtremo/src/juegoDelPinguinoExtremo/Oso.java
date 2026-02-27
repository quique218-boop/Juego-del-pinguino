package juegoDelPinguinoExtremo;

public class Oso extends Casilla {

	public Oso() {}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		jugador.setPos(0);
	}

}
