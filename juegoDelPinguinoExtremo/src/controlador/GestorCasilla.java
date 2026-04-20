package controlador;

import java.util.ArrayList;

import modelo.*;

public class GestorCasilla {

	public GestorCasilla() {

	}

	public ArrayList<Integer> ejecutarCasilla(Tablero partida, Jugador jugador) {

		ArrayList<Integer> Posiciones = new ArrayList<>();
		Casilla casilla;
		int oldPos;
		int newPos;

		casilla = partida.getCasilla(jugador.getPos());

		oldPos = jugador.getPos();

		casilla.realizarAccion(partida, jugador);

		newPos = jugador.getPos();

		if (oldPos != newPos)
			Posiciones.add(newPos);

		return Posiciones;
	}

	public void comprobarFinTurno(Tablero partida) {

		if (!partida.getFinalizada()) {

			partida.setFinalizada(true);

		}

	}
}
