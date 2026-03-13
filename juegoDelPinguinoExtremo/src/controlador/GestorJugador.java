package controlador;

import modelo.*;

public class GestorJugador {

	public GestorJugador() {

	}

	public void jugadorUsarItem(Pinguino jugador, String nItem) {

		if (jugador == null || nItem == null)
			return;

		Inventario inventario = jugador.getInventario();

		if (inventario == null)
			return;

		if (nItem.equalsIgnoreCase("Bola de Nieve")) {

			if (!inventario.getBolas().isEmpty()) { // Si la lista no es vacia

				jugador.usarItem(inventario.getBolas().get(0)); // El jugador coge la primera bola de nieve de la lista
			}

			if (nItem.equalsIgnoreCase("Pez")) {

				if (!inventario.getPez().isEmpty()) {

					jugador.usarItem(inventario.getPez().get(0));

				}

				if (nItem.equalsIgnoreCase("Dado")) {

					if (!inventario.getDado().isEmpty()) {

						jugador.usarItem(inventario.getDado().get(0));
					}
				}
			}
		}
	}

	public void jugadorSeMueve(Jugador jugador, int pasos) {

		if (jugador == null)
			return;

		jugador.moverPosicion(pasos);

	}

	public void jugadorFinalizaTurno(Jugador jugador) {

		jugador.setTurnoTerminado(true);

	}

	public void pinguinoGuerra(Pinguino jugador, Pinguino jugador2) {

		jugador.gestionarBatalla(jugador2);
	}

	public void focaInteractua(Pinguino jugador, Foca foca, String opc, Tablero tablero) {

		if (jugador == null || foca == null)
			return;

		if (opc.equalsIgnoreCase("Sobornar")) {

			foca.esSobornado();

		} else if (opc.equalsIgnoreCase("Robar")) {

			foca.aplastarJugador(jugador);

		} else if (opc.equalsIgnoreCase("Golpear Jugador")) {

			foca.golpearJugador(jugador, tablero);

		}

	}

}
