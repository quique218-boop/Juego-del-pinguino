package juegoDelPinguinoExtremo;

public class Foca extends Jugador {

	public Foca(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

	}


	public void aplastarJugador(Pinguino jugador) {

		jugador.getInventario().RobarInventario(); // La foca roba la mitad del inventario del jugador

	}

	public void golpearJugador(Pinguino jugador, Tablero tablero) {

		if (this.getPos() == jugador.getPos())
			new Agujero().realizarAccion(tablero, jugador);

	}

	public void esSobornado() {
		
		this.setDeudaTurnos(2);

	}

}
