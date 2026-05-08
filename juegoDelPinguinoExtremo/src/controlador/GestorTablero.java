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

	public ArrayList<String> estadoPeleas() {

		ArrayList<Jugador> jugadores = tablero.getArrayListJugador();

		ArrayList<String> estado = new ArrayList<>();

		for (Jugador jugador : jugadores) {

			String pelea = jugador.getPelea();

			if (pelea != null)
				estado.add(pelea);
		}

		return estado;
	}
	
	public ArrayList<String> estadoGolpeos() {

		Foca foca = (Foca) tablero.getArrayListJugador().getLast();

		ArrayList<String> estado = foca.getGolpeados(); 

		return estado;
	}

	public ArrayList<ArrayList<Integer>> ejecutarTurnoCompleto(Foca foca) {

		ArrayList<ArrayList<Integer>> acciones = new ArrayList<>();

		Random rand = new Random();

		Inventario inventarioFoca = foca.getInventario();

		// Elegir dado

		ArrayList<Integer> dadoRapido = new ArrayList<>();
		ArrayList<Integer> dadoLento = new ArrayList<>();

		for (int i = 0; i < inventarioFoca.getDado().size(); i++) {

			Dado dado = inventarioFoca.getDado().get(i);

			if (dado instanceof DadoRapido) {
				dadoRapido.add(i);
			} else if (dado instanceof DadoLento) {
				dadoLento.add(i);
			}
		}

		// Opciones que puede usar el rand

		int limiteSuperiorRand = -1;

		if (dadoRapido.size() != 0 && dadoLento.size() != 0) { // Encontrado los dos tipos
			limiteSuperiorRand = 3;
		} else if (dadoRapido.size() != 0 || dadoLento.size() != 0) { // Encontrado rapido o lento
			limiteSuperiorRand = 2;
		} else { // Encontrado ninguno
			limiteSuperiorRand = 1;
		}

		// Elegir dado

		ArrayList<Integer> dadoUsar = new ArrayList<>();

		while (!dadoUsar.contains(0)) {
			dadoUsar.add(rand.nextInt(limiteSuperiorRand));
		}

		// Limpiar no existentes o agotados

		for (int i = 0; i < dadoUsar.size(); i++) {

			int eleccion = dadoUsar.get(i);

			switch (eleccion) {

			case 0:
				break;

			case 1:

				if (dadoLento.size() == 0 && dadoRapido.size() == 0) { // No queda, borrar
					dadoUsar.remove(i);
				} else if (dadoLento.size() == 0 && dadoRapido.size() != 0) { // Solo quedan rapidos, intercambio
					dadoUsar.add(i, 2); // Pones un dos en el lugar del uno
					dadoUsar.remove(i + 1); // El uno se ha movido al siguiente indicie por lo que sumas 1
				} else { // Restar un uso
					dadoLento.removeLast();
				}

				break;

			case 2:

				if (dadoRapido.size() == 0) { // No queda, borrar
					dadoUsar.remove(i);
				} else { // Restar un uso
					dadoRapido.removeLast();
				}

				break;
			}
		}

		acciones.add(dadoUsar);

		if (inventarioFoca.getBolas().size() != 0) {

			// Usar bola de nieve

			boolean usarBola = rand.nextBoolean();

			if (usarBola) {

				ArrayList<Jugador> objetivosTotales = tablero.getArrayListJugador();

				objetivosTotales.removeLast();

				limiteSuperiorRand = objetivosTotales.size();

				int cantidadDeDisparos = rand.nextInt(inventarioFoca.getBolas().size());

				ArrayList<Integer> listaDisparos = new ArrayList<>();

				if (objetivosTotales.size() != 0) {
					while (objetivosTotales.size() != 0 && cantidadDeDisparos > 0) {
						int eleccion = rand.nextInt(limiteSuperiorRand);

						listaDisparos.add(objetivosTotales.get(eleccion).getTurnoEnArray());

						objetivosTotales.remove(eleccion);
						cantidadDeDisparos--;
					}
				}

				// Usar bola de nieve antes o despues de los dados

				boolean despues = rand.nextBoolean();

				if (despues) {
					acciones.add(listaDisparos);
				} else {
					acciones.add(0, listaDisparos);
				}
			}
		}
		
		return acciones;
	}

	public ArrayList<PairMovimiento> procesarTurnoJugador(Jugador jugador) {

		ArrayList<PairMovimiento> jugadorYMovimientos = new ArrayList<>();

		ArrayList<Integer> posiciones = new ArrayList<>();

		posiciones.addAll(gestorcasilla.ejecutarCasilla(this.tablero, jugador));

		if (!posiciones.isEmpty()) {
			for (int posicion : posiciones) {
				jugadorYMovimientos.add(new PairMovimiento(jugador.getNombre(), posicion));
			}
		}

		int oldPos;
		int newPos;

		do {

			oldPos = jugador.getPos();

			ArrayList<Jugador> listaJugadoresEnCasilla = BuscarJugadoresEnCasilla(jugador);

			if (listaJugadoresEnCasilla.size() == 0) {
				System.out.println("NO HAY NADIE EN  CASILLA");
				return jugadorYMovimientos;
			}

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

						jugadorYMovimientos.add(new PairMovimiento(pinguino.getNombre(),
								jugadorFoca.golpearJugador(pinguino, tablero)));
						jugadorYMovimientos.addAll(procesarTurnoJugador(pinguino));

					} else {

						pinguino.usarItem(inventarioPinguino.getPez().getFirst());
						jugadorFoca.esSobornado();

						return jugadorYMovimientos;
					}
				}
			} else {

				Random rand = new Random();
				Jugador jugadorEnCasilla;

				do {

					if (listaJugadoresEnCasilla.size() == 0)
						return jugadorYMovimientos;

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
					pinguino1.resultadoGuerra(pinguino2, null); // Empate jugador 1
					pinguino2.resultadoGuerra(pinguino1, null); // Empate jugador 2
					return jugadorYMovimientos;
				} else if (PosFinalPinguino1 != PosInicialPinguino1) {
					jugadorYMovimientos.add(new PairMovimiento(pinguino1.getNombre(), PosFinalPinguino1));
					jugadorYMovimientos.addAll(procesarTurnoJugador(pinguino1));
					pinguino1.resultadoGuerra(pinguino2, false); // Pierde jugador 1
					pinguino2.resultadoGuerra(pinguino1, true); // Gana jugador 2
				} else if (PosFinalPinguino2 != PosInicialPinguino2) {
					jugadorYMovimientos.add(new PairMovimiento(pinguino2.getNombre(), PosFinalPinguino2));
					jugadorYMovimientos.addAll(procesarTurnoJugador(pinguino2));
					pinguino1.resultadoGuerra(pinguino2, true); // Gana jugador 1
					pinguino2.resultadoGuerra(pinguino1, false); // Pierde jugador 2
				}
			}

			newPos = jugador.getPos();

		} while (oldPos != newPos);

		return jugadorYMovimientos;
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

	public void setTablero(Tablero tablero) {

		this.tablero = tablero;

	}
}
