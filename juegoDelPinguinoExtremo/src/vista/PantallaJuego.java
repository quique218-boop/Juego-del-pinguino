package vista;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import controlador.GestorBBDD;
import controlador.GestorTablero;
import modelo.*;

public class PantallaJuego {

	// Menu items
	@FXML
	private MenuItem newGame;
	@FXML
	private MenuItem saveGame;
	@FXML
	private MenuItem loadGame;
	@FXML
	private MenuItem quitGame;

	// Buttons
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

	// Texts
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

	// Game board and player pieces
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

	@FXML
	private ListView<Text> ListaEventos;

	private ObservableList<Text> ListaObservable = FXCollections.observableArrayList();

	private boolean modoBola = false;

	private GestorTablero gestorTablero;
	private GestorBBDD gestorBBDD;
	// ONLY FOR TESTING!!!
	private static final int COLUMNS = 5;
	private int p1Position = 0;
	private int p2Position = 0;
	private int p3Position = 0;
	private int p4Position = 0;// Tracks current position (from 0 to 49 in a 5x10 grid)
	private int focaPosition = 0;
	private int turno = 0;

	private ArrayList<Circle> jugadores = new ArrayList<>();

	private int[] posiciones = { p1Position, p2Position, p3Position, p4Position, focaPosition };

	private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";
	private final Random random = new Random();
	private final double RATIO_DESCENSO_PROBABILIDAD = 0.39d;
	private final double e = 2.718281828459045235360d;
	private final int cellWidth = 120;
	private final int cellHeight = 80;
	// Relentiza al inicio y final para un poco mas de visibilidad
	private final Interpolator interpolador = Interpolator.EASE_BOTH;
	private boolean viajando = false;

	@FXML
	private void initialize() {

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
		gestorTablero.NuevoTablero();

		// Pone el texto del tipo de casilla
		mostrarTiposDeCasillasEnTablero(gestorTablero.getPartida());

		// Añadir jugadores hardcodeados temporalmente
		gestorTablero.añadirJugador(new Pinguino("Jugador1", "Azul", new Inventario()));
		gestorTablero.añadirJugador(new Pinguino("Jugador2", "Verde", new Inventario()));
		gestorTablero.añadirJugador(new Foca("Foca", "Amarillo", new Inventario()));

		// Asignar el jugador actual como el primero de la lista (Jugador 1)
		gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getArrayListJugador().getFirst()));

		// Definir el atributo del orden de turno de jugadores
		ponerTurnoEnArray();

		// Borramos los circulos cuando no hay jugadores para ellos.
		BorrarFichasSinJugador();

		// TEMPORALMENTE DAMOS LOS OBJETOS AL PRINCIPIO
		// Jugador 1
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoLento());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoRapido());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaBolas(new BolaDeNieve());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaPez(new Pez());
		// Jugador 2
		gestorTablero.getPartida().getJugador(1).getInventario().addListaPez(new Pez());
		gestorTablero.getPartida().getJugador(1).getInventario().addListaPez(new Pez());

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
	private void handleSaveGame() {
		System.out.println("Saved game.");
		gestorBBDD.guardar(gestorTablero.getPartida());
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

		Jugador jugador = (Jugador) gestorTablero.getPartida().getJugadorActual();

		int[] resultadoYPosFinalDado = ProcesarDado(jugador, null);
		int resultado = resultadoYPosFinalDado[0];
		int PosFinalDado = resultadoYPosFinalDado[1];

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), PosFinalDado, jugador);

		System.out.println(jugador.getNombre() + " " + jugador.getPos());
	}

	@FXML
	private void handleRapido() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		DadoRapido dadoRapido = (DadoRapido) EncontrarDado(jugador, true); // True == DadoRapido

		if (dadoRapido != null) {

			int[] resultadoYPosFinalDado = ProcesarDado(jugador, dadoRapido);
			int resultado = resultadoYPosFinalDado[0];
			int PosFinalDado = resultadoYPosFinalDado[1];

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), PosFinalDado, jugador);

			AddEventoHistorial("Usando dado rápido ha salido: " + resultado);
		}
	}

	@FXML
	private void handleLento() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		DadoLento dadoLento = (DadoLento) EncontrarDado(jugador, false); // False == DadoLento

		if (dadoLento != null) {

			int[] resultadoYPosFinalDado = ProcesarDado(jugador, dadoLento);
			int resultado = resultadoYPosFinalDado[0];
			int PosFinalDado = resultadoYPosFinalDado[1];

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), PosFinalDado, jugador);

			AddEventoHistorial("Usando dado lento ha salido: " + resultado);
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

	@FXML
	private void handleNieve() { // Seleccionamos el estado del juego

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = jugador.getInventario();

		if (inventario.getBolas().isEmpty()) { // Si la lista no es vacia
			return;
		}

		modoBola = true;
	}

	@FXML
	private void selecP1() {
		selecPingu(gestorTablero.getPartida().getJugador(0));
	}

	@FXML
	private void selecP2() {
		selecPingu(gestorTablero.getPartida().getJugador(1));
	}

	@FXML
	private void selecP3() {
		selecPingu(gestorTablero.getPartida().getJugador(2));
	}

	@FXML
	private void selecP4() {
		selecPingu(gestorTablero.getPartida().getJugador(3));
	}

	@FXML
	private void selecFOCA() {
		selecPingu(gestorTablero.getPartida().getArrayListJugador().getLast());
	}

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

	private void selecPingu(Jugador objBola) {
		if (!modoBola) {
			return;
		}

		tirarBola(objBola);
		modoBola = false;
	}

	private void tirarBola(Jugador objBola) {

		Jugador jugadorAct = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = jugadorAct.getInventario();

		if (inventario.getBolas().isEmpty()) {
			return;
		} else if (objBola == null || objBola == jugadorAct || objBola.getPos() == 0) {
			return;
		}

		efectos_de_sonido.sonidoBola();

		int distancia = CalcularDistancia(jugadorAct, objBola);

		jugadorAct.usarItem(inventario.getBolas().getFirst());

		ActualizarInventarioGUI(jugadorAct);

		if (CalcularExito(distancia)) {

			objBola.moverPosicion(-1);

			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugadorAct), -1, jugadorAct);

			AddEventoHistorial("¡¡Has acertado!!");

		} else {

			AddEventoHistorial("Has fallado :(");

		}
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

	private void animacionMoverJugadores(ArrayList<PairMovimiento> listaMovimientos, int PosFinalDado,
			Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada())
			return;

		finalizarTurno.setDisable(true);
		rapido.setDisable(true);
		lento.setDisable(true);
		nieve.setDisable(true);

		listaMovimientos.add(0, new PairMovimiento(jugador.getNombre(), PosFinalDado));

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

			Jugador jugadorEvento = jugadorActual;

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

				if (listaMovimientos.size() == 1)
					timeline.getKeyFrames().add(new KeyFrame(Duration.millis(700), e -> {

						MostrarEventosEnKeyFrame(posiciones[jugadorActualIndice], jugadorEvento);

					}));

			} else {
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700 * (i + 1)),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotal[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotal[jugadorActualIndice], interpolador)));

				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(700 * (i + 1)), e -> {

					MostrarEventosEnKeyFrame(posiciones[jugadorActualIndice], jugadorEvento);
				}));
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

			finalizarTurno.setDisable(false);

			ActualizarInventarioGUI(jugador);
		});

		timeline.play();
	}

	private void MostrarEventosEnKeyFrame(ArrayList<PairMovimiento> listaMovimientos, int posicionJugador,
			Jugador jugador) {

		Casilla casillaActual = gestorTablero.getPartida().getCasilla(posicionJugador);

		int posicionAnterior = -1;

		for (int i = 0; i < listaMovimientos.size(); i++) {
			if (listaMovimientos.get(i).posicion == posicionJugador) {
				if (listaMovimientos.size() != 1) {
					for (int j = i - 1; j >= 0; i--) {
						if (listaMovimientos.get(j).jugador.equals(jugador.getNombre())) {
							posicionAnterior = listaMovimientos.get(i - 1).posicion;
						}
					}
					
					if (posicionAnterior == -1)
						posicionAnterior = posicionJugador;

				} else {
					posicionAnterior = posicionJugador;
				}
			}
		}

		switch (casillaActual) {

		case Agujero a -> {
			if (posicionJugador == 0)
				AddEventoHistorial(jugador.getNombre() + " ha caido hasta el inicio");
			else if (!viajando)
				AddEventoHistorial(jugador.getNombre() + " ha caido en un agujero");

			viajandoToggle();
		}

		case Evento e -> {

			if (e.getResultado() == "Motos de nieve")
				viajandoToggle();
			else if (e.getResultado() == "Perder un turno")
				FinalizarTurno();

			AddEventoHistorial(jugador.getNombre() + " ha caido en un evento y el evento ha sido: " + e.getResultado());
		}

		case Trineo t -> {
			if (!viajando)
				AddEventoHistorial(jugador.getNombre() + " ha utilizado un trineo");

			viajandoToggle();
		}

		case SueloQuebradizo s -> {

			AddEventoHistorial(s.getResultado());

			if (s.getResultado() == "El suelo se ha partido un poco y te has quedado atascado")
				FinalizarTurno();
		}

		case Oso o -> {
			if (posicionJugador == 0) {
				AddEventoHistorial(jugador.getNombre() + " ha sido atacado por un oso");
			} else {
				AddEventoHistorial(jugador.getNombre() + " ha usado un pez para evitar ser atacado por un oso");
			}
		}

		case Normal n -> {
			return;
		}

		default -> throw new IllegalArgumentException("Unexpected value: " + casillaActual);

		}

		ActualizarInventarioGUI(jugador);
	}

	private void viajandoToggle() {

		viajando = (viajando) ? false : true;
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

		// Desactivar finalizar turno
		finalizarTurno.setDisable(true);
		dado.setDisable(false);

		ActualizarInventarioGUI(jugadorSiguiente);
	}

	public void ponerTurnoEnArray() {

		for (int i = 0; i < gestorTablero.getPartida().getArrayListJugador().size(); i++) {

			gestorTablero.getPartida().getJugador(i).setTurnoEnArray(i);

		}
	}

	public void setGestorPartida(GestorTablero gestorTablero) {
		this.gestorTablero = gestorTablero;
	}
}
