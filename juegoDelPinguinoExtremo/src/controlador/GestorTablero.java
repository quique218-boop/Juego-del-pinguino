package controlador;

import java.util.ArrayList;
import java.util.Random;
import modelo.*;

public class GestorTablero {

	Random rand = new Random();

	private Tablero tablero;

	private GestorJugador gestorjugador;

	private GestorCasilla gestorcasilla;

	public GestorTablero() {

		this.tablero = new Tablero();

		this.gestorjugador = new GestorJugador(this.tablero);

		this.gestorcasilla = new GestorCasilla();

	}

	public void NuevoTablero() {

		tablero.inicializarTablero();

	}

	public void añadirJugador(Jugador jugador) {

		tablero.getArrayListJugador().add(jugador); //Añadimos jugador
	}

	public int tirarDado(Jugador jugador) {

		Dado dadoDefault = new Dado();

		int resultado = dadoDefault.tirarDado(); //guardamos número que salga en el dado

		gestorjugador.jugadorSeMueve(jugador, resultado); //El jugador avança las casillas correspondientes

		return resultado;

	}

	public int tirarDado(Jugador jugador, Dado dadoOpcional) {

		jugador.quitarItem(dadoOpcional);

		int resultado = dadoOpcional.tirarDado(); 

		gestorjugador.jugadorSeMueve(jugador, resultado);

		return resultado;

	}

	public ArrayList<String> estadoPeleas() {
		// Devuelve una lista con los mensajes/estados de pelea de los jugadores
		ArrayList<Jugador> jugadores = tablero.getArrayListJugador(); //Obtiene todos los jugadores

		ArrayList<String> estado = new ArrayList<>(); // Lista donde se guardarán los textos de las peleas

		for (Jugador jugador : jugadores) { //Recorre todos los jugadores

			String pelea = jugador.getPelea();

			if (pelea != null) // Si el jugador tiene información de pelea, la añadimos a la lista
				estado.add(pelea);
		}

		return estado; //Devuelve todos los estados
	}

	public ArrayList<String> estadoGolpeos() {

		Foca foca = (Foca) tablero.getArrayListJugador().getLast();

		ArrayList<String> estado = foca.getGolpeados();

		return estado;
	}

	public ArrayList<ArrayList<Integer>> ejecutarTurnoCompleto(Foca foca) { //Se ejecuta el turno de la foca

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
			limiteSuperiorRand = 3; //Tiene los 3 tipos de dados
		} else if (dadoRapido.size() != 0 || dadoLento.size() != 0) { // Encontrado rapido o lento
			limiteSuperiorRand = 2; //Dado normal o especial
		} else { // Encontrado ninguno
			limiteSuperiorRand = 1; //Solamente dado normal
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

				ArrayList<Jugador> objetivosTotales = new ArrayList<>();
						
				objetivosTotales.addAll(tablero.getArrayListJugador());

				objetivosTotales.removeLast();
				
				System.out.println(tablero.getArrayListJugador());

				limiteSuperiorRand = objetivosTotales.size();

				int cantidadDeDisparos = rand.nextInt(inventarioFoca.getBolas().size());

				ArrayList<Integer> listaDisparos = new ArrayList<>();

				if (objetivosTotales.size() != 0) {
					while (objetivosTotales.size() != 0 && cantidadDeDisparos > 0) {
						int eleccion = rand.nextInt(limiteSuperiorRand);

						listaDisparos.add(objetivosTotales.get(eleccion).getTurnoEnArray());

						objetivosTotales.remove(eleccion);
						cantidadDeDisparos--;
						limiteSuperiorRand--;
					}
				}

				listaDisparos.add(9); // Identificador de que la lista de numeros es de objetivos y no dados
				
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

	public ArrayList<PairMovimiento> procesarTurnoJugador(Jugador jugador) { // Procesa las consecuencias del turno de un jugador:

		ArrayList<PairMovimiento> jugadorYMovimientos = new ArrayList<>(); //Guarda los movimientos y  PairMovimiento relaciona nombre del jugador + nueva posición.

		ArrayList<Integer> posiciones = new ArrayList<>();

		posiciones.addAll(gestorcasilla.ejecutarCasilla(this.tablero, jugador)); // Ejecuta la acción de la casilla donde está el jugador.

		if (!posiciones.isEmpty()) {
			for (int posicion : posiciones) {
				jugadorYMovimientos.add(new PairMovimiento(jugador.getNombre(), posicion)); // Si la casilla movió al jugador, guardamos ese movimiento.
			}
		}

		int oldPos;
		int newPos;

		do {

			oldPos = jugador.getPos(); //Posicion antes de comprobar

			ArrayList<Jugador> listaJugadoresEnCasilla = BuscarJugadoresEnCasilla(jugador); //Miramos si hay otro jugador en la misma casilla

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

					if (inventarioPinguino.getPez().size() == 0) { //Si el pinguino no tiene peces la foca lo golpe

						jugadorYMovimientos.add(new PairMovimiento(pinguino.getNombre(),
								jugadorFoca.golpearJugador(pinguino, tablero))); //Guardamos el movimineto provocado
						jugadorYMovimientos.addAll(procesarTurnoJugador(pinguino));

					} else {

						pinguino.usarItem(inventarioPinguino.getPez().getFirst());
						jugadorFoca.esSobornado();

						return jugadorYMovimientos;
					}
				}
			} else { //Si no es foca

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

		if (jugadorPrincipalPos != 0) {
			for (Jugador jugadorEnCasilla : tablero.getArrayListJugador()) {

				if (jugadorPrincipalPos == jugadorEnCasilla.getPos()) { //Si estan en la misma pos

					if (jugadorEnCasilla != jugador) { // No es el mismo jugador

						listaJugadores.add(jugadorEnCasilla); //Lo añadimos

					}
				}
			}
		}

		return listaJugadores;
	}

	public void actualizarEstadoTablero() {

	}

	public void siguienteTurno() { //Cambiamos al siguiente jugador de la lista

		int i = tablero.getJugadorActual().getPos();

		if (i + 1 <= tablero.getArrayListJugador().size()) {

			tablero.setJugadorActual(tablero.getJugador(i + 1)); //Pasa al siguiente jugadir

		} else {

			tablero.setJugadorActual(tablero.getJugador(0)); //Si llega al final vuelve al primer jugador

		}

	}

	public Tablero getPartida() {

		return tablero;

	}

	public void setTablero(Tablero tablero) {

		this.tablero = tablero;

	}
}
