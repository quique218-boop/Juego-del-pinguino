package juegoDelPinguinoExtremo;

public class Trineo extends Casilla {

	public Trineo() {}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		
		boolean encontrado = false;

		for (int i = jugador.getPos() + 1; i < tablero.getArrayListCasilla().size() && encontrado == false; i--) {

			if (tablero.getCasilla(i) instanceof Trineo) {
				jugador.setPos(i);
				encontrado = true;
			}
		}
	}
}
