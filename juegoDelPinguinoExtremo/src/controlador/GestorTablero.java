package controlador;

import java.util.ArrayList;
import java.util.Random;

import modelo.*;

public class GestorTablero {
	
	Random rand = new Random();
	
	private Tablero tablero;
	
	private GestorJugador gestorjugador;
	
	private GestorBBDD gestorbbdd;
	
	private GestorCasilla gestorcasilla;
	
	public GestorTablero() {
		
		this.tablero = new Tablero();
		
		this.gestorjugador = new GestorJugador();
		
		this.gestorbbdd = new GestorBBDD();
		
		this.gestorcasilla = new GestorCasilla();
			
	}

	

	
	public void NuevoTablero() {
		
		 tablero.inicializarTablero();

		
	}
	
	public void añadirJugador(Jugador jugador) {
		
		tablero.getArrayListJugador().add(jugador);
	}
	
	public int tirarDado(Jugador jugador, Dado dadoOpcional) {
		
		jugador.quitarItem(dadoOpcional);
		
		int resultado = dadoOpcional.tirarDado();
		
		gestorjugador.jugadorSeMueve(jugador, resultado);
		
		return resultado;
		
	}
	
	public void ejecutarTurnoCompleto() {
		
		
		if(tablero.getjugadorActual() instanceof Foca) {
			
			Dado d;
			
			if(tablero.getjugadorActual().getInventario().getDado().size() > 0) {
				
				 d = tablero.getjugadorActual().getInventario().getDado().get(rand.nextInt(2)); //Tomamos un dado aleatoría de entre los de la foca
			
			}
			
			else {
				
				 d = new Dado("normal");
				 
			}
			
			int movimiento = tirarDado(tablero.getjugadorActual(), d);
			
			gestorjugador.jugadorSeMueve(tablero.getjugadorActual(), movimiento);
			
			tablero.getCasilla(tablero.getjugadorActual().getPos()).realizarAccion(tablero, tablero.getjugadorActual());
			
			for(int i = 0; i < tablero.getArrayListJugador().size(); i++) {
				
				if(tablero.getjugadorActual().getPos() == tablero.getJugador(i).getPos()) {
					
					if(tablero.getJugador(i).getInventario().getPez().size() > 0) {
						
						tablero.getJugador(i).getInventario().getPez().remove(0);
						
					}
					
					else {
						
						((Foca) tablero.getjugadorActual()).golpearJugador( ( (Pinguino) tablero.getJugador(i) ), tablero);
						
					}	
				}
				
			}
			
		}
		
		else {
			
			Dado d = new Dado("eleccion"); //AQUÍ DEBEREMOS DE TOMAR EL DADO ELEGIDO EN LA VISTA
			
			int movimiento = tirarDado(tablero.getjugadorActual(), d);
			
			gestorjugador.jugadorSeMueve(tablero.getjugadorActual(), movimiento);
			
			tablero.getCasilla(tablero.getjugadorActual().getPos()).realizarAccion(tablero, tablero.getjugadorActual());
			
			for(int i = 0; i < tablero.getArrayListJugador().size(); i++) {
				
				if(tablero.getjugadorActual().getPos() == tablero.getJugador(i).getPos()) {
					
					( (Pinguino) tablero.getjugadorActual() ).gestionarBatalla(((Pinguino) tablero.getJugador(i)));
					
				}
				
				
			}
			
		}
		
		siguienteTurno();
		
	}
	
	public void procesarTurnoJugador(Jugador jugador) {

	}
	
	public void actualizarEstadoTablero() {

		
	}
	
	public void siguienteTurno() {
		
		int i = tablero.getjugadorActual().getPos();
		
		if( i+1 <= tablero.getArrayListJugador().size()) {
			
		tablero.setjugadorActual(tablero.getJugador(i + 1));
		
		}
		else {
			
			tablero.setjugadorActual(tablero.getJugador(0));
			
		}

		
	}
	
	public Tablero getPartida() {

		return tablero;
		
	}
	
	public void guardarPartida() {

		
	}
	
	public void cargarPartida(int id) {

		
	}
}
