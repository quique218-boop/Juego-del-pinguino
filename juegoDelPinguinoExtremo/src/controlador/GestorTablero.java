package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

		this.gestorjugador = new GestorJugador(this.tablero);

		this.gestorbbdd = new GestorBBDD();

		this.gestorcasilla = new GestorCasilla();

	}

	public void NuevoTablero() {

		tablero.inicializarTablero();

	}

	public void añadirJugador(Jugador jugador) {

		tablero.getArrayListJugador().add(jugador);
	}

	public int tirarDado(Jugador jugador) {

		Dado dadoDefault = new Dado();

		int resultado = dadoDefault.tirarDado();

		gestorjugador.jugadorSeMueve(jugador, resultado);

		return resultado;

	}

	public int tirarDado(Jugador jugador, Dado dadoOpcional) {

		jugador.quitarItem(dadoOpcional);

		int resultado = dadoOpcional.tirarDado();

		gestorjugador.jugadorSeMueve(jugador, resultado);

		return resultado;

	}

	public void ejecutarTurnoCompleto() {

		if (tablero.getJugadorActual() instanceof Foca) {

			Dado d;

			if (tablero.getJugadorActual().getInventario().getDado().size() > 0) {

				d = tablero.getJugadorActual().getInventario().getDado().get(rand.nextInt(2)); // Tomamos un dado
																								// aleatoría de entre
																								// los de la foca

			}

			else {

				d = new Dado();

			}

			int movimiento = tirarDado(tablero.getJugadorActual(), d);

			gestorjugador.jugadorSeMueve(tablero.getJugadorActual(), movimiento);

			tablero.getCasilla(tablero.getJugadorActual().getPos()).realizarAccion(tablero, tablero.getJugadorActual());

			for (int i = 0; i < tablero.getArrayListJugador().size(); i++) {

				if (tablero.getJugadorActual().getPos() == tablero.getJugador(i).getPos()) {

					if (tablero.getJugador(i).getInventario().getPez().size() > 0) {

						tablero.getJugador(i).getInventario().getPez().remove(0);

					}

					else {

						((Foca) tablero.getJugadorActual()).golpearJugador(((Pinguino) tablero.getJugador(i)), tablero);

					}

				}

			}

		}

		else {

			Dado d = new Dado(); // AQUÍ DEBEREMOS DE TOMAR EL DADO ELEGIDO EN LA VISTA

			int movimiento = tirarDado(tablero.getJugadorActual(), d);

			gestorjugador.jugadorSeMueve(tablero.getJugadorActual(), movimiento);

			tablero.getCasilla(tablero.getJugadorActual().getPos()).realizarAccion(tablero, tablero.getJugadorActual());

			for (int i = 0; i < tablero.getArrayListJugador().size(); i++) {

				if (tablero.getJugadorActual().getPos() == tablero.getJugador(i).getPos()) {

					((Pinguino) tablero.getJugadorActual()).gestionarBatalla(((Pinguino) tablero.getJugador(i)));

				}

			}

		}

		siguienteTurno();

	}

	public HashMap<String, ArrayList<Integer>> procesarTurnoJugador(Jugador jugador) {

		HashMap<String, ArrayList<Integer>> listaPosiciones = new HashMap<>();

		ArrayList<Integer> Posiciones = new ArrayList<>();

		Posiciones = gestorcasilla.ejecutarCasilla(this.tablero, jugador);

		//ArrayList<Jugador> listaJugadoresEnCasilla = BuscarJugadoresEnCasilla(jugador);

		//if (listaJugadoresEnCasilla.size() == 0) {
			listaPosiciones.put(jugador.getNombre(), Posiciones);
			return listaPosiciones;
		//}

		/*if (jugador instanceof Foca) {

			Foca jugadorFoca = (Foca) jugador;

			ArrayList<Pinguino> listaPinguinosEnCasilla = new ArrayList<>();

			// Convertir el arraylist a arraylist de pinguinos ya que la foca es el jugador
			for (Jugador pinguino : listaJugadoresEnCasilla) {
				listaPinguinosEnCasilla.add((Pinguino) pinguino);
			}

			for (Pinguino pinguino : listaPinguinosEnCasilla) {

				Inventario inventarioPinguino = pinguino.getInventario();

				if (inventarioPinguino.getPez().size() == 0) {

					Posiciones.add(jugadorFoca.golpearJugador(pinguino, tablero));

				} else {

					pinguino.usarItem(inventarioPinguino.getPez().getFirst());
					jugadorFoca.esSobornado();
					return listaPosiciones;

				}
			}
		} else {

			Random rand = new Random();
			Jugador jugadorEnCasilla;

			if (listaJugadoresEnCasilla.size() != 0) {
				do {

					int i = rand.nextInt(listaJugadoresEnCasilla.size());

					jugadorEnCasilla = listaJugadoresEnCasilla.get(i);

					if (jugadorEnCasilla instanceof Foca)
						listaJugadoresEnCasilla.remove(i);

				} while (jugadorEnCasilla instanceof Foca);

				Pinguino pinguino1 = (Pinguino) jugador;
				Pinguino pinguino2 = (Pinguino) jugadorEnCasilla;

				int PosInicialPinguino1 = pinguino1.getPos();
				int PosInicialPinguino2 = pinguino2.getPos();

				pinguino1.gestionarBatalla(pinguino2);

				int PosFinalPinguino1 = pinguino1.getPos();
				int PosFinalPinguino2 = pinguino2.getPos();

				if (PosFinalPinguino1 == PosFinalPinguino2) {
					return listaPosiciones;
				} else if (PosFinalPinguino1 != PosInicialPinguino1) {

					Posiciones.add(PosFinalPinguino1);

				} else if (PosFinalPinguino2 != PosInicialPinguino2) {

					ArrayList<Integer> PosicionesJugador2 = new ArrayList<Integer>();
					PosicionesJugador2.add(PosFinalPinguino2);

					listaPosiciones.put(pinguino2.getNombre(), PosicionesJugador2);

				}
			}
		}
		
		listaPosiciones.put(jugador.getNombre(), Posiciones);

		return listaPosiciones;*/
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

	public void actualizarEstadoTablero() {

	}

	public void siguienteTurno() {

		int i = tablero.getJugadorActual().getPos();

		if (i + 1 <= tablero.getArrayListJugador().size()) {

			tablero.setJugadorActual(tablero.getJugador(i + 1));

		} else {

			tablero.setJugadorActual(tablero.getJugador(0));

		}

	}

	public Tablero getPartida() {

		return tablero;

	}

	public void guardarPartida() {

		gestorbbdd.guardar(tablero);

	}

	public void cargarPartida(int id) {

		// Aquí cargaremos nuestra partida en la BBDD

	}

}
