package modelo;

public class Trineo extends Casilla {

	public Trineo() {}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		
		boolean encontrado = false;

		for (int i = jugador.getPos() + 1; i < tablero.getArrayListCasilla().size() && encontrado == false; i++) { //Mira de la posición del jugador hacia adelante para encontrar otro trineo y mover al jugador allí

			if (tablero.getCasilla(i) instanceof Trineo) {
				jugador.setPos(i);
				encontrado = true;
				efectos_de_sonido.sonidoTrineo();
			}
		}
	}
}
