package modelo;

public class Agujero extends Casilla {

	public Agujero() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		boolean encontrado = false;

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
