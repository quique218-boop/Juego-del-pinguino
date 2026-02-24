package juegoDelPinguinoExtremo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Tablero {

	private ArrayList<Jugador> listaJugador;
	private ArrayList<Casilla> listaCasillas;
	private int turnos;
	private int jugadorActual;
	private boolean finalizada;
	private Jugador ganador;

	private Random ran = new Random();

	public Tablero() {

		listaJugador = new ArrayList<>();

		listaCasillas = new ArrayList<>();

	}

	/*
	 * for (int i = 0; i < 50; i++) {
	 * 
	 * switch (ran.nextInt(6)) {
	 * 
	 * case 0: casillas.add(new Oso()); break;
	 * 
	 * case 1: casillas.add(new Trineo()); break; case 2: casillas.add(new
	 * Agujero()); break;
	 * 
	 * case 3: casillas.add(new Evento()); break;
	 * 
	 * case 4: casillas.add(new SueloQuebradizo()); break;
	 * 
	 * case 5: casillas.add(new Normal()); break;
	 * 
	 * } }
	 * 
	 * 
	 */

	public void addJugador(Jugador jugador) {

		this.listaJugador.add(jugador);
	}

	public Jugador getJugador(int posicion) {

		return this.listaJugador.get(posicion);
	}

	public void addCasilla(Casilla casilla) {

		this.listaCasillas.add(casilla);

	}

	public Casilla getCasilla(int posicion) {

		return this.listaCasillas.get(posicion);

	}

	public void setjugadorActual(int jugadorActual) {

		this.jugadorActual = jugadorActual;
	}

	public int getjugadorActual() {

		return this.jugadorActual;
	}

	public void setTurnos(int turnos) {

		this.turnos = turnos;
	}

	public int getTurnos() {

		return this.turnos;
	}

	public void setFinalizada(boolean finalizada) {

		this.finalizada = finalizada;
	}

	public boolean getFinalizada() {

		return this.finalizada;
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
