package controlador;

import java.util.ArrayList;
import java.util.Random;

import modelo.*;

public class GestorJugador {

	Tablero tablero;

	public GestorJugador(Tablero tablero) {

		this.tablero = tablero;

	}

	public void jugadorUsarItem(Pinguino jugador, Item nItem) {

		if (jugador == null || nItem == null)
			return;

		Inventario inventario = jugador.getInventario();

		if (inventario == null)
			return;

		if (nItem instanceof BolaDeNieve) {

			if (!inventario.getBolas().isEmpty()) { // Si la lista no es vacia

				jugador.usarItem(inventario.getBolas().get(0)); // El jugador coge la primera bola de nieve de la lista
			}
		}

		else if (nItem instanceof Pez) {

			if (!inventario.getPez().isEmpty()) {

				jugador.usarItem(inventario.getPez().get(0));

			}
		}

		else if (nItem instanceof Dado) {

			if (!inventario.getDado().isEmpty()) {

				jugador.usarItem(inventario.getDado().get(0));
			}
		}
	}

	public void jugadorSeMueve(Jugador jugador, int pasos) {

		if (jugador == null && this.tablero == null)
			return;

		jugador.moverPosicion(pasos);

		CaerEnCasilla(jugador);

		ArrayList<Jugador> listaJugadoresEnCasilla = BuscarJugadoresEnCasilla(jugador);

		if (listaJugadoresEnCasilla.size() == 0)
			return;

		if (jugador instanceof Foca) {

			Foca jugadorFoca = (Foca) jugador;

			ArrayList<Pinguino> listaPinguinosEnCasilla = new ArrayList<>();

			// Convertir el arraylist a arraylist de pinguinos ya que la foca es el jugador
			for (Jugador pinguino : listaJugadoresEnCasilla) {
				listaPinguinosEnCasilla.add((Pinguino) pinguino);
			}

			for (Pinguino pinguino : listaPinguinosEnCasilla) {

				Inventario inventarioPinguino = pinguino.getInventario();

				if (inventarioPinguino.getPez().size() == 0) {

					jugadorFoca.golpearJugador(pinguino, tablero);

				} else {

					pinguino.usarItem(inventarioPinguino.getPez().getFirst());
					jugadorFoca.esSobornado();
					return;

				}
			}
		} else {

			Random rand = new Random();
			Jugador jugadorEnCasilla;

			do {

				int i = rand.nextInt(listaJugadoresEnCasilla.size());

				jugadorEnCasilla = listaJugadoresEnCasilla.get(i);

				if (jugadorEnCasilla instanceof Foca)
					listaJugadoresEnCasilla.remove(i);

			} while (jugadorEnCasilla instanceof Foca);

			Pinguino pinguino1 = (Pinguino) jugador;
			Pinguino pinguino2 = (Pinguino) jugadorEnCasilla;

			pinguino1.gestionarBatalla(pinguino2);

			if (pinguino1.getPos() == pinguino2.getPos())
				return;
		}
	}

	private ArrayList<Jugador> BuscarJugadoresEnCasilla(Jugador jugador) {

		ArrayList<Jugador> listaJugadores = new ArrayList<>();

		int jugadorPrincipalPos = jugador.getPos();

		for (Jugador jugadorEnCasilla : tablero.getArrayListJugador()) {

			if (jugadorPrincipalPos == jugadorEnCasilla.getPos()) {

				if (jugadorEnCasilla != jugador) {

					listaJugadores.add(jugadorEnCasilla);

				}
			}
		}

		return listaJugadores;
	}

	private void CaerEnCasilla(Jugador jugador) {

		System.out.println("caerEnCasilla ejecutada");

		int oldPos = jugador.getPos();

		Casilla casillaAterrizada = this.tablero.getCasilla(oldPos);

		casillaAterrizada.realizarAccion(tablero, jugador);

		int newPos = jugador.getPos();

		if (oldPos != newPos) {

			CaerEnCasilla(jugador);

		}
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
