package controlador;

import modelo.*;

public class GestorCasilla {

	public GestorCasilla() {
		
	}
	
	public void ejecutarCasilla(Tablero partida, Pinguino jugador, Casilla casilla) {
		
		partida.getCasilla(jugador.getPos());
		
		casilla.realizarAccion(partida, jugador);
		
	}
	
	public void comprobarFinTurno(Tablero partida) {
		
		if(!partida.getFinalizada()) { 
			
			partida.setFinalizada(true); 
			
		}
		
	}
}
