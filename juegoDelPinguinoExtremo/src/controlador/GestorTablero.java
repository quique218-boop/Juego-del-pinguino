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

	public void procesarTurnoJugador(Jugador jugador) {

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
