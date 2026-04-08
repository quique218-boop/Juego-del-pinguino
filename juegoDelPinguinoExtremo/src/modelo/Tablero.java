package modelo;

import java.util.ArrayList;
import java.util.Random;

public class Tablero {

	private ArrayList<Jugador> listaJugador;
	private ArrayList<Casilla> listaCasillas;
	private String fechaInicio;
	private int turnos;
	private Jugador jugadorActual;
	private boolean finalizada;
	private Jugador ganador;
	
	Random rand = new Random();

	public Tablero() {

		listaJugador = new ArrayList<>();

		listaCasillas = new ArrayList<>();
		
		turnos = 0;

	}

	public Jugador getJugador(int posicion) {

		return this.listaJugador.get(posicion);
	}

	public Casilla getCasilla(int posicion) {

		return this.listaCasillas.get(posicion);

	}
	
	public String getFecha() {
		
		return fechaInicio;
		
	}
	
	public void setFecha(String fecha) {
		
		fechaInicio = fecha;
		
	}
	
	public ArrayList<Jugador> getArrayListJugador() {

		return this.listaJugador;
	}

	public ArrayList<Casilla> getArrayListCasilla() {

		return this.listaCasillas;

	}
	
	public void setArrayListJugador(ArrayList<Jugador> listaJugador) {

		this.listaJugador = listaJugador;
	}
	

	public void setArrayListCasilla(ArrayList<Casilla> listaCasillas) {

		this.listaCasillas = listaCasillas;

	}

	public void setJugadorActual(Jugador jugadorActual) {

		this.jugadorActual = jugadorActual;
	}

	public Jugador getJugadorActual() {

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
	
	public void inicializarTablero(){
		
		ArrayList<Casilla> tablero = new ArrayList<>();
		
		tablero.add(new Normal());
		
		for(int i = 0; i < 48; i++) {
			
			int opcion = rand.nextInt(10);
			
			switch(opcion){
			
				case 1, 3, 5, 7, 9 :{
					
					tablero.add(new Normal());
					break;
				}
				
				case 0 :{
					
					tablero.add(new Agujero());
					break;
					
				}
				
				case 2 :{
					
					tablero.add(new Evento());
					break;
					
				}
				
				case 4 :{
					
					tablero.add(new SueloQuebradizo());
					break;
					
				}
				
				case 6 :{
					
					tablero.add(new Oso());
					break;
					
				}
				
				case 8 :{
					
					
					tablero.add(new Trineo());
					break;
					
				}
			}
		}
		
		tablero.add(new Normal());
		
		listaCasillas = tablero;
		
	}
}
