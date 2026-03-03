package juegoDelPinguinoExtremo;

import java.util.ArrayList;
import java.util.Random;

public class Tablero {

	private ArrayList<Jugador> listaJugador;
	private ArrayList<Casilla> listaCasillas;
	private int turnos;
	private int jugadorActual;
	private boolean finalizada;
	private Jugador ganador;
	
	Random rand = new Random();

	public Tablero() {

		listaJugador = new ArrayList<>();

		listaCasillas = new ArrayList<>();

	}

	public Jugador getJugador(int posicion) {

		return this.listaJugador.get(posicion);
	}

	public Casilla getCasilla(int posicion) {

		return this.listaCasillas.get(posicion);

	}
	
	public ArrayList<Jugador> getArrayListJugador() {

		return this.listaJugador;
	}

	public ArrayList<Casilla> getArrayListCasilla() {

		return this.listaCasillas;

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

	public void inicializarJugadores(ArrayList<Jugador> jugadores) {

		this.listaJugador = jugadores;

	}

	public void inicializarCasillas(ArrayList<Casilla> casillas) {

		this.listaCasillas = casillas;

	}

	public void siguienteTurno() {
		this.turnos++;
	}

	public void marcarFinalizada() {

		this.finalizada = true;

	}
	
	public ArrayList<Casilla> inicializarTablero(){
		
		ArrayList<Casilla> tablero = new ArrayList<>();
		
		
		for(Casilla f : tablero) {
			
			int opcion = rand.nextInt(10);
			
			switch(opcion){
			
				case 1, 3, 5, 7, 9 :{
					
					Normal casilla = new Normal();
					
					tablero.add(casilla);
				}
				
				case 0 :{
					
					Agujero casilla = new Agujero();
					
					tablero.add(casilla);
					
				}
				
				case 2 :{
					
					Evento casilla = new Evento();
					
					tablero.add(casilla);
					
				}
				
				case 4 :{
					
					SueloQuebradizo casilla = new SueloQuebradizo();
					
					tablero.add(casilla);
					
				}
				
				case 6 :{
					
					Oso casilla = new Oso();
					
					tablero.add(casilla);
					
				}
				
				case 8 :{
					
					Trineo casilla = new Trineo();
					
					tablero.add(casilla);
					
				}
			}
		}
		
		return tablero;
		
	}
}
