package juegoDelPinguinoExtremo;

import java.util.ArrayList;

public class Tablero {

	private ArrayList<Jugador> listaJugador;
	private ArrayList<Casilla> casillas;
	private int turnos;
	private int jugadorActual;
	private boolean finalizada;
	private Jugador ganador;

	public Tablero() {

	}

	public void setTurnos(int turnos) {

		this.turnos = turnos;
	}

	public int getTurnos() {

		return this.turnos;
	}

	public void setjugadorActual(int jugadorActual) {

		this.jugadorActual = jugadorActual;
	}

	public int getjugadorActual() {

		return this.jugadorActual;
	}

	public void setGanador(Jugador ganador) {

		this.ganador = ganador;
	}

	public Jugador getGanador() {

		return this.ganador;
	}

	public void inicializar(ArrayList<Jugador> jugadores) {

	}

	public void siguienteTurno() {
		this.turnos++;
	}

	public void marcarFinalizada() {

		this.finalizada = true;

	}
}
