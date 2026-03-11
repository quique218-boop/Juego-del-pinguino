package juegoDelPinguinoExtremo;

public class GestorPartida {

	public GestorPartida() {
		//TODO
		
	}
	
	public void NuevaPartida() {

		
	}
	
	public int tirarDado(Jugador jugador, Dado dadoOpcional) {
		
		jugador.quitarItem(dadoOpcional);
		
		int resultado = dadoOpcional.tirarDado();
		
		GestorJugador.jugadorSeMueve(jugador, resultado);
		
		return resultado;
		

		
	}
	
	public void ejecutarTurnoCompleto() {

		
	}
	
	public void procesarTurnoJugador(Jugador jugador) {

	}
	
	public void actualizarEstadoTablero() {

		
	}
	
	public void SiguienteTurno() {

		
	}
	
	public Partida getPartida() {

		
	}
	
	public void guardarPartida() {

		
	}
	
	public void cargarPartida(int id) {

		
	}
}
