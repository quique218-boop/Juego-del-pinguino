package controlador;

import java.util.ArrayList;

import modelo.*;

public class GestorCasilla {

	public GestorCasilla() {

	}

	public ArrayList<Integer> ejecutarCasilla(Tablero partida, Jugador jugador) { 
		// Ejecuta la acción de la casilla en la que está el jugador

		ArrayList<Integer> Posiciones = new ArrayList<>(); 	// Lista donde se guardarán las nuevas posiciones si el jugador se mueve
		Casilla casilla; // Casilla donde está actualmente el jugador
		int oldPos; // Posición antes de ejecutar la acción de la casilla
		int newPos; // Posición después de ejecutar la acción de la casilla

		casilla = partida.getCasilla(jugador.getPos()); // Obtiene la casilla en la que está el jugador según su posición

		oldPos = jugador.getPos(); //Guarda la pos antes de que la casilla haga su efecto

		casilla.realizarAccion(partida, jugador); //Ejecuta la acción

		newPos = jugador.getPos();

		if (oldPos != newPos)
			Posiciones.add(newPos); // Si la posición ha cambiado, guarda la nueva posición en la lista

		return Posiciones; // Devuelve la lista con las posiciones nuevas.
	}

	public void comprobarFinTurno(Tablero partida) { //Comprobamos si la partida ha finalizado

		if (!partida.getFinalizada()) {

			partida.setFinalizada(true);

		}

	}
}
