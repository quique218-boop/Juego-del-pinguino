package juegoDelPinguinoExtremo;

public class Evento extends Casilla {

	String[] eventos;

	public Evento() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {
		// TODO Auto-generated method stub

	}

	public void setEventos(String[] eventos) {

		this.eventos = eventos;

	}

	public String[] getEventos() {

		return this.eventos;

	}

}
