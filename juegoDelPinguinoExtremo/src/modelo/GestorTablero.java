package controlador;

public class GestorTablero {
	
	private Tablero tablero;
	
	private GestorJugador gestorjugador;
	
	private GestorBBDD gestorbbdd;
	
	private GestorCasilla gestorcasilla;

	
	public void NuevoTablero() {
		
		tablero.setArrayListCasilla(null); = tablero.inicializarTablero();

		
	}
	
	public int tirarDado(Jugador jugador, Dado dadoOpcional) {
		
		jugador.quitarItem(dadoOpcional);
		
		int resultado = dadoOpcional.tirarDado();
		
		gestorjugador.jugadorSeMueve(jugador, resultado);
		
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
