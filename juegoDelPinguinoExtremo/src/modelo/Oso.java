package modelo;

public class Oso extends Casilla {

	public Oso() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		Inventario inventario = jugador.getInventario();

		if (inventario.getPez().size() == 0)
			jugador.setPos(0);
		else
			jugador.quitarItem(new Pez());
	}

}
