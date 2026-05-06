package vista;

import javafx.animation.Interpolator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import controlador.GestorBBDD;
import controlador.GestorTablero;
import modelo.*;

public class PantallaJuego {

	// Items de menu
	@FXML
	private MenuItem newGame;
	@FXML
	private MenuItem saveGame;
	@FXML
	private MenuItem loadGame;
	@FXML
	private MenuItem quitGame;

	// Botones
	@FXML
	private Button dado;
	@FXML
	private Button rapido;
	@FXML
	private Button lento;
	@FXML
	private Button nieve;
	@FXML
	private Button finalizarTurno;

	// Textos
	@FXML
	private Text dadoResultText;
	@FXML
	private Text rapido_t;
	@FXML
	private Text lento_t;
	@FXML
	private Text peces_t;
	@FXML
	private Text nieve_t;

	// Tablero y piezas
	@FXML
	private GridPane tablero;
	@FXML
	private Circle P1;
	@FXML
	private Circle P2;
	@FXML
	private Circle P3;
	@FXML
	private Circle P4;
	@FXML
	private Circle FOCA;

	// Lista de eventos
	@FXML
	private ListView<Text> ListaEventos;
	private ObservableList<Text> ListaObservable = FXCollections.observableArrayList();

	// Variables finales
	private final Random random = new Random();
	private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";
	// Calculo tirar bola de nieve
	private final double RATIO_DESCENSO_PROBABILIDAD = 0.39d;
	private final double e = 2.718281828459045235360d;
	// Medidas tablero
	private static final int COLUMNS = 5;
	private final int cellWidth = 120;
	private final int cellHeight = 80;
	// Animacion: Relentiza al inicio y final para un poco mas de visibilidad
	private final Interpolator interpolador = Interpolator.EASE_BOTH;

	// Crear gestores
	private GestorTablero gestorTablero;
	private GestorBBDD gestorBBDD;

	// Variables modificables
	private ArrayList<Circle> jugadores = new ArrayList<>();
	private int[] posiciones = { 0, 0, 0, 0, 0 };
	private int turno = 0;
	private boolean modoBola = false;
	private boolean focaSobornadaEsteTurno = false;
	private boolean focaJuega = false;

	private static boolean sonidosInicializados = false;
	
	private ArrayList<Usuario> usuarios;

	public void setUsuarios(ArrayList<Usuario> usuarios) {
	    this.usuarios = usuarios;
	}

  
        

	
	public void inicio(Tablero tablero) {
		
		

		if (!sonidosInicializados) {
            efectos_de_sonido.init();
            sonidosInicializados = true;
        }
		
		// UI

		// Añadir la funcionalidad al texto de cambiar de linea al llegar al final

		/*
		 * Cambiar la creación de celdas a una personalizada Como parámetro se crea un
		 * callback con input de ListView y output es ListCell. Entonces crea la llamada
		 * donde se sobreescribe la función de la celda de updateItem. Simplemente se
		 * añade que el ítem tenga la función de cambio de línea a la longitud
		 * especificada.
		 */

		ListaEventos.setCellFactory(new Callback<ListView<Text>, ListCell<Text>>() {
			@Override
			public ListCell<Text> call(ListView<Text> list) {
				final ListCell<Text> cell = new ListCell<Text>() {
					@Override
					protected void updateItem(Text item, boolean empty) {
						super.updateItem(item, empty);

						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							item.setWrappingWidth(ListaEventos.getPrefWidth() - 20);
							setGraphic(item);
						}
					}
				};

				return cell;
			}
		});

		AddEventoHistorial("¡El juego ha comenzado!");

		P1.getStyleClass().add("current-player"); // Pone el efecto del jugador actual al jugador 1

		finalizarTurno.setDisable(true); // Desactivamos el boton de pasar de turno al principio

		// Creacion gestor Base de datos
		gestorBBDD = new GestorBBDD();

		// Creacion gestor tablero y crear un nuevo tablero
		gestorTablero = new GestorTablero();
		if(tablero == null) {
		gestorTablero.NuevoTablero();
		
		for(int i = 0; i < usuarios.size(); i++){
			
			gestorTablero.añadirJugador(new Pinguino(usuarios.get(i).getNombre(), "Azul", new Inventario(), usuarios.get(i)));
			
		}
		
		gestorTablero.añadirJugador(new Foca("Foca", "Amarillo", new Inventario()));
		
		}
		else {
			gestorTablero.setTablero(tablero);
		}

		// Pone el texto del tipo de casilla
		mostrarTiposDeCasillasEnTablero(gestorTablero.getPartida());

		

		// Asignar el jugador actual como el primero de la lista (Jugador 1)
		gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getArrayListJugador().getFirst()));

		// Definir el atributo del orden de turno de jugadores
		ponerTurnoEnArray();

		// Borramos los circulos cuando no hay jugadores para ellos.
		BorrarFichasSinJugador();
	
		// Poner el menu acorde al inventario del jugador actual (Jugador 1)
		ActualizarInventarioGUI(gestorTablero.getPartida().getJugadorActual());

	}

	private void mostrarTiposDeCasillasEnTablero(Tablero t) {

		// Clear only the labels we generated in previous calls
		tablero.getChildren().removeIf(node -> TAG_CASILLA_TEXT.equals(node.getUserData()));

		for (int i = 0; i < t.getArrayListCasilla().size(); i++) {
			Casilla casilla = t.getArrayListCasilla().get(i);

			// Skip position 0 and 49 if you want them to be special (start/end)
			if (i > 0 && i < 49) {
				String tipo = casilla.getClass().getSimpleName();

				Text texto = new Text(tipo);
				texto.setUserData(TAG_CASILLA_TEXT);
				texto.getStyleClass().add("cell-type");
				GridPane.setHalignment(texto, HPos.CENTER);

				int row = i / COLUMNS;
				int col = i % COLUMNS;

				GridPane.setRowIndex(texto, row);
				GridPane.setColumnIndex(texto, col);

				tablero.getChildren().add(0, texto);
			}
		}
	}

	// Menu actions
	@FXML
	private void handleNewGame() throws IOException {
		System.out.println("New game.");

		Parent root = FXMLLoader.load(getClass().getResource("/recursos/PantallaJuego.fxml"));

		Stage stage = (Stage) P1.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();

	}

	@FXML
	private void handleSaveGame(ActionEvent event) {
		
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/slots.fxml"));
	        Parent root = loader.load();

	        Slots controller = loader.getController();

	        controller.setModo(false);
	        controller.setPartida(gestorTablero.getPartida());

	        Stage stage = (Stage) P1.getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.show();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	private void handleLoadGame() {
		System.out.println("Loaded game.");
		// TODO
	}

	@FXML
	private void handleQuitGame() {
		System.exit(0);
	}

	// Button actions
	@FXML
	private void handleDado(ActionEvent event) {

		// Evita spam del botón
		dado.setDisable(true);

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		int posInicialSiFoca = jugador.getPos();

		int[] resultadoYPosFinalDado = ProcesarDado(jugador, null);
		int resultado = resultadoYPosFinalDado[0];
		int posFinalDado = resultadoYPosFinalDado[1];

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);

		System.out.println(jugador.getNombre() + " " + jugador.getPos());

		if (jugador instanceof Foca) {
			FocaCompruebaRobo(posInicialSiFoca, posFinalDado, jugador);
		}
	}

	@FXML
	private void handleRapido() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		DadoRapido dadoRapido = (DadoRapido) EncontrarDado(jugador, true); // True == DadoRapido

		if (dadoRapido != null) {

			int posInicialSiFoca = jugador.getPos();

			int[] resultadoYPosFinalDado = ProcesarDado(jugador, dadoRapido);
			int resultado = resultadoYPosFinalDado[0];
			int posFinalDado = resultadoYPosFinalDado[1];

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);

			AddEventoHistorial("Usando dado rápido ha salido: " + resultado);

			if (jugador instanceof Foca) {
				FocaCompruebaRobo(posInicialSiFoca, posFinalDado, jugador);
			}
		}
	}

	@FXML
	private void handleLento() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		DadoLento dadoLento = (DadoLento) EncontrarDado(jugador, false); // False == DadoLento

		if (dadoLento != null) {

			int posInicialSiFoca = jugador.getPos();

			int[] resultadoYPosFinalDado = ProcesarDado(jugador, dadoLento);
			int resultado = resultadoYPosFinalDado[0];
			int posFinalDado = resultadoYPosFinalDado[1];

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);

			AddEventoHistorial("Usando dado lento ha salido: " + resultado);

			if (jugador instanceof Foca) {
				FocaCompruebaRobo(posInicialSiFoca, posFinalDado, jugador);
			}
		}
	}

	@FXML
	private void handleNieve() { // Seleccionamos el estado del juego

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = jugador.getInventario();

		if (inventario.getBolas().isEmpty()) { // Si la lista no es vacia
			return;
		}

		modoBola = true;

		for (Jugador jugadorObj : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugadorObj != jugador)
				jugadores.get(jugadorObj.getTurnoEnArray()).getStyleClass().add("obj-player");

		}
	}

	@FXML
	private void selecP1() {
		selecJugador(gestorTablero.getPartida().getJugador(0));
	}

	@FXML
	private void selecP2() {
		selecJugador(gestorTablero.getPartida().getJugador(1));
	}

	@FXML
	private void selecP3() {
		selecJugador(gestorTablero.getPartida().getJugador(2));
	}

	@FXML
	private void selecP4() {
		selecJugador(gestorTablero.getPartida().getJugador(3));
	}

	@FXML
	private void selecFOCA() {
		selecJugador(gestorTablero.getPartida().getArrayListJugador().getLast());
	}

	private void selecJugador(Jugador objBola) {
		if (!modoBola) {
			return;
		}

		tirarBola(objBola);
		modoBola = false;

		for (Jugador jugadorObj : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugadorObj != gestorTablero.getPartida().getJugadorActual())
				jugadores.get(jugadorObj.getTurnoEnArray()).getStyleClass().remove("obj-player");

		}
	}

	private void tirarBola(Jugador objBola) {

		Jugador jugadorAct = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = jugadorAct.getInventario();

		if (inventario.getBolas().isEmpty()) {
			return;
		} else if (objBola == null || objBola == jugadorAct || objBola.getPos() == 0) {
			AddEventoHistorial("No se puede disparar a ese jugador");
			return;
		}

		efectos_de_sonido.sonidoBola();

		int distancia = CalcularDistancia(jugadorAct, objBola);

		jugadorAct.usarItem(inventario.getBolas().getFirst());

		ActualizarInventarioGUI(jugadorAct);

		if (CalcularExito(distancia)) {

			objBola.moverPosicion(-1);

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugadorAct), objBola.getPos(), objBola);

			AddEventoHistorial("¡¡Has acertado!!");

		} else {

			AddEventoHistorial("Has fallado :(");

		}
	}

	private void animacionMoverJugadores(ArrayList<PairMovimiento> listaMovimientos, int posFinalDado,
			Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada())
			return;

		finalizarTurno.setDisable(true);
		rapido.setDisable(true);
		lento.setDisable(true);
		nieve.setDisable(true);

		listaMovimientos.add(0, new PairMovimiento(jugador.getNombre(), posFinalDado));

		Timeline timeline = new Timeline();

		timeline.setRate(0.5); // Velocidad reducida para mayor visibilidad

		Jugador jugadorActual = null;

		int[] dxTotal = { 0, 0, 0, 0, 0 };
		int[] dyTotal = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < listaMovimientos.size(); i++) {

			PairMovimiento jugadorYMovimiento = listaMovimientos.get(i);

			for (int j = 0; jugadorActual == null; j++) {

				jugadorActual = gestorTablero.getPartida().getJugador(j)
						.devolverSiNombreCoincide(jugadorYMovimiento.jugador);
			}

			int jugadorActualIndice = jugadorActual.getTurnoEnArray();

			int oldPosition = posiciones[jugadorActualIndice];

			int movimiento = jugadorYMovimiento.posicion - oldPosition;

			posiciones[jugadorActualIndice] += movimiento;

			// Bound player
			if (posiciones[jugadorActualIndice] >= 50) {
				posiciones[jugadorActualIndice] = 49;
			}

			if (posiciones[jugadorActualIndice] < 0) {
				posiciones[jugadorActualIndice] = 0;
			}

			// OLD position
			int oldRow = oldPosition / COLUMNS;
			int oldCol = oldPosition % COLUMNS;

			// NEW position
			int newRow = posiciones[jugadorActualIndice] / COLUMNS;
			int newCol = posiciones[jugadorActualIndice] % COLUMNS;

			dxTotal[jugadorActualIndice] += (newCol - oldCol) * cellWidth;
			dyTotal[jugadorActualIndice] += (newRow - oldRow) * cellHeight;

			if (i == 0) {
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.ZERO,
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(), 0),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(), 0)));

				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotal[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotal[jugadorActualIndice], interpolador)));
			} else {
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700 * (i + 1)),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotal[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotal[jugadorActualIndice], interpolador)));
			}

			jugadorActual = null;
		}

		timeline.setOnFinished(e -> {

			for (int i = 0; i < jugadores.size(); i++) {
				// reset translation
				jugadores.get(i).setTranslateX(0);
				jugadores.get(i).setTranslateY(0);

				// set real position in grid
				GridPane.setRowIndex(jugadores.get(i), posiciones[i] / COLUMNS);
				GridPane.setColumnIndex(jugadores.get(i), posiciones[i] % COLUMNS);
			}

			MostrarEventos(listaMovimientos);

			if (!focaJuega)
				finalizarTurno.setDisable(false);

			if (gestorTablero.getPartida().getJugadorActual().getDeudaTurnos() > 0)
				FinalizarTurno();

			ActualizarInventarioGUI(gestorTablero.getPartida().getJugadorActual());
		});

		timeline.play();
	}

	public void MostrarEventos(ArrayList<PairMovimiento> listaMovimientos) {

		ArrayList<String> listaNombres = new ArrayList<>();

		ArrayList<Jugador> jugadores = new ArrayList<>(listaNombres.size());

		for (PairMovimiento nombres : listaMovimientos) {

			String nombre = nombres.jugador;

			Jugador jugadorEncontrado = null;

			for (int i = 0; jugadorEncontrado == null; i++) {

				jugadorEncontrado = gestorTablero.getPartida().getJugador(i).devolverSiNombreCoincide(nombre);

				if (jugadorEncontrado != null && !jugadores.contains(jugadorEncontrado))
					jugadores.add(jugadorEncontrado);
			}
		}

		for (Jugador jugador : jugadores) {

			ArrayList<Integer> listaSaneada = new ArrayList<>();

			for (PairMovimiento pareja : listaMovimientos) {
				if (pareja.jugador.equals(jugador.getNombre()))
					listaSaneada.add(pareja.posicion);
			}

			for (int i = 0; i < listaSaneada.size() || (i < 1 && listaSaneada.size() == 1); i++) {
				int posicion = listaSaneada.get(i);
				int posicionSiguiente;
				if (i == listaSaneada.size() - 1) {
					posicionSiguiente = 49;
				} else {
					posicionSiguiente = listaSaneada.get(i + 1);
				}

				Casilla casilla = gestorTablero.getPartida().getCasilla(posicion);
				Casilla casillaSiguiente = gestorTablero.getPartida().getCasilla(posicionSiguiente);

				switch (casilla) {

				case Agujero a -> {
					if (casillaSiguiente instanceof Agujero)
						AddEventoHistorial(jugador.getNombre() + " ha caido en un agujero");
				}

				case Evento e -> {
					AddEventoHistorial(
							jugador.getNombre() + " ha caido en un evento y el evento ha sido: " + e.getResultado());
				}

				case Trineo t -> {
					if (casillaSiguiente instanceof Trineo)
						AddEventoHistorial(jugador.getNombre() + " ha utilizado un trineo");
				}

				case SueloQuebradizo s -> {
					AddEventoHistorial(s.getResultado());
				}

				case Oso o -> {
					if (casillaSiguiente.getPosicion() == 0) {
						AddEventoHistorial(jugador.getNombre() + " ha sido atacado por un oso");
					} else {
						AddEventoHistorial(jugador.getNombre() + " ha usado un pez para evitar ser atacado por un oso");
					}
				}

				case Normal n -> {
				}

				default -> throw new IllegalArgumentException("Unexpected value: " + casilla);

				}
			}

			ArrayList<String> estadoPeleas = gestorTablero.estadoPeleas();

			if (estadoPeleas != null) {
				for (String pelea : estadoPeleas) {
					AddEventoHistorial(pelea);
				}
			}

			Foca foca = (Foca) gestorTablero.getPartida().getArrayListJugador().getLast();

			if (foca.getDeudaTurnos() == 2 && !focaSobornadaEsteTurno) {
				AddEventoHistorial(foca.getNombre() + " ha sido sobornada para no golpear y no se movera en 2 turnos");
				focaSobornadaEsteTurno = true;
			} else if (foca.getDeudaTurnos() == 0) {
				focaSobornadaEsteTurno = false;
			}
		}
	}

	public void FinalizarTurno() {

		Jugador jugadorActual = gestorTablero.getPartida().getJugador(turno);

		Jugador jugadorSiguiente;

		turno = (turno + 1) % jugadores.size(); // Cambio de turno

		jugadorSiguiente = gestorTablero.getPartida().getJugador(turno);

		while (jugadorSiguiente.getDeudaTurnos() > 0) {

			if (jugadorSiguiente.getDeudaTurnos() > 0) {
				AddEventoHistorial(jugadorSiguiente.getNombre() + " pierde el turno");
				jugadorSiguiente.reducirDeudaTurnos();
			}

			turno = (turno + 1) % jugadores.size(); // Cambio de turno

			jugadorSiguiente = gestorTablero.getPartida().getJugador(turno);

		}

		// Cambiar jugador actual en la lógica
		gestorTablero.getPartida().setJugadorActual(gestorTablero.getPartida().getJugador(turno));

		jugadores.get(jugadorActual.getTurnoEnArray()).getStyleClass().remove("current-player");

		jugadores.get(jugadorSiguiente.getTurnoEnArray()).getStyleClass().add("current-player");

		if (jugadorSiguiente instanceof Foca)
			turnoFoca();
		else
			focaJuega = false;

		resetVariablesDeTurno();

		ActualizarInventarioGUI(jugadorSiguiente);

	}

	private void turnoFoca() {

		focaJuega = true;

		Foca foca = (Foca) gestorTablero.getPartida().getArrayListJugador().getLast();

		ArrayList<ArrayList<Integer>> acciones = gestorTablero.ejecutarTurnoCompleto(foca);

		boolean ArrayDados;

		if (acciones.getFirst().contains(4))
			ArrayDados = false;
		else
			ArrayDados = true;

		for (int i = 0; i < acciones.size(); i++) {

			if (ArrayDados) {

				ArrayList<Integer> tirarDados = acciones.get(i);

				for (int dado : tirarDados) {

					if (foca == gestorTablero.getPartida().getJugadorActual()) {

						switch (dado) {

						case 0:
							System.out.println("FOCA HA USADO NORMAL");
							this.dado.fire();
							break;
						case 1:
							System.out.println("FOCA HA USADO LENTO");
							this.lento.fire();
							break;
						case 2:
							System.out.println("FOCA HA USADO RAPIDO");
							this.rapido.fire();
							break;
						}

					} else {
						return;
					}
				}
			} else {

				ArrayList<Integer> tirarBolas = acciones.get(i);

				for (int objetivo : tirarBolas) {

					this.nieve.fire();

					switch (objetivo) {

					case 0:
						selecP1();
					case 1:
						selecP2();
					case 2:
						selecP3();
					case 3:
						selecP4();

					}
				}
			}

			ArrayDados = (ArrayDados) ? false : true;

		}

		FinalizarTurno();
	}

	// Funciones auxiliares

	private void BorrarFichasSinJugador() {

		ArrayList<Jugador> listaJugadores = gestorTablero.getPartida().getArrayListJugador();

		if (listaJugadores.size() == 4) {

			tablero.getChildren().remove(P4);

		} else if (listaJugadores.size() == 3) {

			tablero.getChildren().remove(P4);
			tablero.getChildren().remove(P3);

		}

		for (Node nodo : tablero.getChildren()) {

			if (nodo instanceof Circle) {

				jugadores.add((Circle) nodo);

			}
		}
	}

	private void FocaCompruebaRobo(int posInicialSiFoca, int PosFinalDado, Jugador jugador) {
		for (Jugador jugadorRobado : gestorTablero.getPartida().getArrayListJugador()) {
			if (jugadorRobado != jugador) {
				if (posInicialSiFoca < jugadorRobado.getPos() && PosFinalDado > jugadorRobado.getPos()) {
					((Foca) jugador).aplastarJugador((Pinguino) jugadorRobado);
					AddEventoHistorial(jugadorRobado.getNombre() + " ha sido robado por la foca");
				}
			}
		}
	}

	private int[] ProcesarDado(Jugador jugador, Dado dado) {

		int oldPos = jugador.getPos();

		int resultado = 0;

		if (dado == null) {
			resultado = gestorTablero.tirarDado(jugador);
		} else {
			if (dado instanceof DadoRapido)
				resultado = gestorTablero.tirarDado(jugador, (DadoRapido) dado);
			else if (dado instanceof DadoLento)
				resultado = gestorTablero.tirarDado(jugador, (DadoLento) dado);
		}

		int PosFinalDado = oldPos + resultado;

		int[] resultadoYPosFinalDado = { resultado, PosFinalDado };

		return resultadoYPosFinalDado;
	}

	private int CalcularDistancia(Jugador j1, Jugador j2) {

		if (j1.getPos() > j2.getPos())
			return j1.getPos() - j2.getPos();

		else
			return j2.getPos() - j1.getPos();
	}

	private boolean CalcularExito(int distancia) {

		int Probabilidad = (int) Math
				.round(Math.pow(RATIO_DESCENSO_PROBABILIDAD * e, -(RATIO_DESCENSO_PROBABILIDAD * distancia)) * 10);

		double resultado = random.nextDouble(10 - Probabilidad + 1) + Probabilidad;

		return (resultado >= 10);
	}

	private void ActualizarInventarioGUI(Jugador jugador) {

		Inventario inventario = jugador.getInventario();

		int dadoRapido = 0;

		int dadoLento = 0;

		int num_peces = inventario.getPez().size();

		int bolasNieve = inventario.getBolas().size();

		for (Dado dado : inventario.getDado()) {

			if (dado instanceof DadoRapido) {

				dadoRapido++;

			} else if (dado instanceof DadoLento) {

				dadoLento++;

			}
		}

		rapido_t.setText("Dado rápido: " + dadoRapido);

		lento_t.setText("Dado lento: " + dadoLento);

		peces_t.setText("Peces: " + num_peces);

		nieve_t.setText("Bolas de nieve: " + bolasNieve);

		if (!focaJuega) {
			if (dadoRapido == 0)
				rapido.setDisable(true);
			else
				rapido.setDisable(false);

			if (dadoLento == 0)
				lento.setDisable(true);
			else
				lento.setDisable(false);

			if (bolasNieve == 0)
				nieve.setDisable(true);
			else
				nieve.setDisable(false);
		}
	}

	private Dado EncontrarDado(Jugador jugador, boolean Rapido_Lento) { // True == DadoRapido, False == DadoLento

		for (Dado dado : jugador.getInventario().getDado()) {

			if (dado instanceof DadoRapido && Rapido_Lento)
				return dado;

			else if (dado instanceof DadoLento && !Rapido_Lento)
				return dado;
		}

		return null;

	}

	private void AddEventoHistorial(String evento) {

		Text texto = new Text(evento);
		texto.getStyleClass().add("events");

		ListaObservable.add(0, texto);

		ListaEventos.setItems(ListaObservable);
	}

	private void resetVariablesDeTurno() {

		modoBola = false;
		// Desactivar finalizar turno
		finalizarTurno.setDisable(true);

		if (!focaJuega)
			dado.setDisable(false);

		for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

			jugadores.get(jugador.getTurnoEnArray()).getStyleClass().remove("obj-player");

		}
	}

	public void ponerTurnoEnArray() {

		for (int i = 0; i < gestorTablero.getPartida().getArrayListJugador().size(); i++) {

			gestorTablero.getPartida().getJugador(i).setTurnoEnArray(i);

		}
	}
}
