package juegoDelPinguinoExtremo;

public class Foca extends Jugador {

	private boolean soborno;

	public Foca(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

		this.soborno = false;

	}

	public boolean getSoborno() {

		return this.soborno;

	}

	public void setSoborno(boolean soborno) {

		this.soborno = soborno;

	}

	public void aplastarJugador(Pinguino jugador) {

		jugador.getInventario().RobarInventario(); // La foca roba la mitad del inventario del jugador

	}

	public void golpearJugador(Pinguino jugador, Tablero tablero) {

		if (this.getPos() == jugador.getPos())
			new Agujero().realizarAccion(tablero, jugador);

	}

	public void esSobornado() {

		this.soborno = this.soborno ? false : true;

	}

}
