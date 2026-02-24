package juegoDelPinguinoExtremo;

public class Oso extends Casilla {

	public Oso() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		jugador.setPos(0);
	}

}
