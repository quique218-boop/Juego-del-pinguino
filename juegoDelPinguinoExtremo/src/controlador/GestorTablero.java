package controlador;

import modelo.*;

public class GestorTablero {
	
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
	
	public int tirarDado(Jugador jugador, Dado dadoOpcional) {
		
		jugador.quitarItem(dadoOpcional);
		
		int resultado = dadoOpcional.tirarDado();
		
		gestorjugador.jugadorSeMueve(jugador, resultado);
		
		return resultado;
		

		
	}
	
	public void ejecutarTurnoCompleto() {
		
		if(tablero.getjugadorActual() instanceof Foca) {
			
			Dado d = new Dado("eleccion"); //AQUÍ DEBEREMOS DE TOMAR ELDADO ELEGIDO ENLA VISTA
			
			int movimiento = tirarDado(tablero.getjugadorActual(), d);
			
			gestorjugador.jugadorSeMueve(tablero.getjugadorActual(), movimiento);
			
		}

		
	}
	
	public void procesarTurnoJugador(Jugador jugador) {

	}
	
	public void actualizarEstadoTablero() {

		
	}
	
	public void SiguienteTurno() {

		
	}
	
	public Tablero getPartida() {

		
	}
	
	public void guardarPartida() {

		
	}
	
	public void cargarPartida(int id) {

		
	}
}
