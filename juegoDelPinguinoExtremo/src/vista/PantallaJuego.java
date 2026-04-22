package vista;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
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
import java.util.Set;

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
	private Button peces;
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
	private final Interpolator interpolador = Interpolator.EASE_BOTH; // Relentiza al inicio y final para un poco mas de
																		// visibilidad

	private boolean peligro = false;

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

		int oldPos = jugador.getPos();

		int resultado = gestorTablero.tirarDado(jugador);

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		int movimiento = oldPos + resultado;

		ProcesarMovimiento(gestorTablero.procesarTurnoJugador(jugador), movimiento);

		System.out.println(jugador.getNombre() + " " + jugador.getPos());

		finalizarTurno.setDisable(false);
	}

	private void ProcesarMovimiento(HashMap<String, ArrayList<Integer>> posicionesConJugador, int movimiento) {

		Jugador jugadorActual = gestorTablero.getPartida().getJugadorActual();

		Jugador jugador2 = null;
		Jugador jugador = null;

	//	if (posicionesConJugador != null) {
			posicionesConJugador.get(jugadorActual.getNombre()).add(0, movimiento);
/*		} else {
			ArrayList<Integer> soloMovimiento = new ArrayList<>();

			soloMovimiento.add(movimiento);

			animacionMoverJugadores(soloMovimiento, jugador);
		}*/

		for (int i = 0; i < posicionesConJugador.size(); i++) {

			ArrayList<Integer> listaPosiciones = new ArrayList<>();

			if (i == 0) {
				jugador = jugadorActual;
			} else {
				for (Jugador jugadorBusqueda : gestorTablero.getPartida().getArrayListJugador()) {
					if (posicionesConJugador.keySet().contains(jugadorBusqueda.getNombre())
							&& jugadorBusqueda != jugadorActual) {
						jugador = jugador2;
					} else {
						return;
					}
				}
			}

			if (jugador == null) {

				System.err.println("JUGADOR ES NULL EN PROCESAR MOVIMIENTO");
				return;
			} else {
				listaPosiciones = posicionesConJugador.get(jugador.getNombre());

				animacionMoverJugadores(listaPosiciones, jugador);
			}
		}
	}

	private void movimiento(Jugador jugador) {

		HashMap<String, ArrayList<Integer>> listaPosiciones = gestorTablero.procesarTurnoJugador(jugador);

		ArrayList<Integer> PosicionesJugador = listaPosiciones.get(jugador.getNombre());

		animacionMoverJugadores(PosicionesJugador, jugador);

		listaPosiciones.remove(jugador.getNombre());

		/*
		 * if (listaPosiciones.size() != 0) {
		 * 
		 * ArrayList<Integer> PosicionesJugador2 = null;
		 * 
		 * Jugador jugador2 = null;
		 * 
		 * for (Jugador jugador2Buscar :
		 * gestorTablero.getPartida().getArrayListJugador()) { try { PosicionesJugador2
		 * = listaPosiciones.get(jugador2Buscar.getNombre()); jugador2 = jugador2Buscar;
		 * } catch (Exception e) { System.out.println("Hashmap no tiene ese jugador"); }
		 * }
		 * 
		 * animacionMoverJugadores(PosicionesJugador2, jugador2);
		 * 
		 * }
		 */
	}

	@FXML
	private void handleRapido() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		DadoRapido dadoRapido = (DadoRapido) EncontrarDado(pinguino, true); // True == DadoRapido

		if (dadoRapido != null) {

			int resultado = gestorTablero.tirarDado(pinguino, dadoRapido);

			AddEventoHistorial("Usando dado rápido ha salido: " + resultado);

			movePlayers(resultado, pinguino);

		}

		ActualizarInventarioGUI(pinguino);

	}

	@FXML
	private void handleLento() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		DadoLento dadoLento = (DadoLento) EncontrarDado(pinguino, false); // False == DadoLento

		if (dadoLento != null) {

			int resultado = gestorTablero.tirarDado(pinguino, dadoLento);

			AddEventoHistorial("Usando dado lento ha salido: " + resultado);

			movePlayers(resultado, pinguino);
		}

		ActualizarInventarioGUI(pinguino);

	}

	@FXML
	private void handlePeces() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		jugador.usarItem(jugador.getInventario().getPez().getFirst());

		efectos_de_sonido.sonidoPez();

		if (gestorTablero.getPartida().getCasilla(jugador.getPos()) instanceof Oso) {

			peligro = false;

		} else if (!(jugador instanceof Foca)) {

			Foca foca = (Foca) gestorTablero.getPartida().getArrayListJugador().getLast();

			if (jugador.getPos() == foca.getPos()) { // Si estamos en la misma casilla de foca

				foca.esSobornado();

				peligro = false;

			}
		}

		AddEventoHistorial(jugador.getNombre() + " le has dado un pez");
		FinalizarTurno();
	}

	@FXML
	private void handleNieve() { // Seleccionamos el estado del juego

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

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

		Jugador pinguinoAct = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguinoAct.getInventario();

		if (inventario.getBolas().isEmpty()) {
			return;
		} else if (objBola == null || objBola == pinguinoAct || objBola.getPos() == 0) {
			return;
		}

		efectos_de_sonido.sonidoBola();

		int distancia = CalcularDistancia(pinguinoAct, objBola);

		pinguinoAct.usarItem(inventario.getBolas().getFirst());

		ActualizarInventarioGUI(pinguinoAct);

		if (CalcularExito(distancia)) {

			objBola.moverPosicion(-1);

			movePlayers(-1, objBola);

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

		peces.setDisable(true);
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

	private void animacionMoverJugadores(ArrayList<Integer> Posiciones, Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada()) {
			return;
		}

		Timeline timeline = new Timeline();

		timeline.setRate(0.5); // Velocidad reducida para mayor visibilidad

		int jugadorActual = jugador.getTurnoEnArray();

		int dxTotal = 0;
		int dyTotal = 0;

		for (int i = 0; i < Posiciones.size(); i++) {

			int oldPosition = posiciones[jugadorActual];

			int movimiento = Posiciones.get(i) - oldPosition;

			posiciones[jugadorActual] += movimiento;

			// Bound player
			if (posiciones[jugadorActual] >= 50) {
				posiciones[jugadorActual] = 49;
			}

			if (posiciones[jugadorActual] < 0) {
				posiciones[jugadorActual] = 0;
			}

			// OLD position
			int oldRow = oldPosition / COLUMNS;
			int oldCol = oldPosition % COLUMNS;

			// NEW position
			int newRow = posiciones[jugadorActual] / COLUMNS;
			int newCol = posiciones[jugadorActual] % COLUMNS;

			dxTotal += (newCol - oldCol) * cellWidth;
			dyTotal += (newRow - oldRow) * cellHeight;

			if (i == 0) {
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.ZERO,
								new KeyValue(jugadores.get(jugadorActual).translateXProperty(), 0),
								new KeyValue(jugadores.get(jugadorActual).translateYProperty(), 0)));

				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(700),
						new KeyValue(jugadores.get(jugadorActual).translateXProperty(), dxTotal, interpolador),
						new KeyValue(jugadores.get(jugadorActual).translateYProperty(), dyTotal, interpolador)));

			} else {
				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(700 * (i + 1)),
						new KeyValue(jugadores.get(jugadorActual).translateXProperty(), dxTotal, interpolador),
						new KeyValue(jugadores.get(jugadorActual).translateYProperty(), dyTotal, interpolador)));

			}
		}

		timeline.setOnFinished(e -> {

			// reset translation
			jugadores.get(jugadorActual).setTranslateX(0);
			jugadores.get(jugadorActual).setTranslateY(0);

			// set real position in grid
			GridPane.setRowIndex(jugadores.get(jugadorActual), posiciones[jugadorActual] / COLUMNS);
			GridPane.setColumnIndex(jugadores.get(jugadorActual), posiciones[jugadorActual] % COLUMNS);
		});

		timeline.play();

	}

	private void movePlayers(int steps, Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada()) {
			return;
		}

		int jugadorActual = jugador.getTurnoEnArray();

		int oldPosition = posiciones[jugadorActual];

		posiciones[jugadorActual] += steps;

		// Bound player
		if (posiciones[jugadorActual] >= 50) {
			posiciones[jugadorActual] = 49;
		}

		if (posiciones[jugadorActual] < 0) {
			posiciones[jugadorActual] = 0;
		}

		// OLD position
		int oldRow = oldPosition / COLUMNS;
		int oldCol = oldPosition % COLUMNS;

		// NEW position
		int newRow = posiciones[jugadorActual] / COLUMNS;
		int newCol = posiciones[jugadorActual] % COLUMNS;

		double dx = (newCol - oldCol) * cellWidth;
		double dy = (newRow - oldRow) * cellHeight;

		TranslateTransition slide = new TranslateTransition(Duration.millis(350), jugadores.get(jugadorActual));

		slide.setByX(dx);
		slide.setByY(dy);

		slide.setOnFinished(e -> {

			// reset translation
			jugadores.get(jugadorActual).setTranslateX(0);
			jugadores.get(jugadorActual).setTranslateY(0);

			// set real position in grid
			GridPane.setRowIndex(jugadores.get(jugadorActual), newRow);
			GridPane.setColumnIndex(jugadores.get(jugadorActual), newCol);
		});

		slide.play();

	}

	public void FinalizarTurno() {

		Jugador jugadorActual = gestorTablero.getPartida().getJugador(turno);

		if (peligro == true) {

			AddEventoHistorial(jugadorActual.getNombre() + " no le has dado un pez asi que caes al principio");

			int PosInicial = jugadorActual.getPos();

			efectos_de_sonido.sonidoMuerte();

			new Oso().realizarAccion(gestorTablero.getPartida(), jugadorActual);

			int movimiento = 0 - PosInicial;

			movePlayers(movimiento, jugadorActual);

			peligro = false;
		}

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

		// volver a activar el botón
		dado.setDisable(false);

		// Desactivar finalizar turno
		finalizarTurno.setDisable(true);

		ActualizarInventarioGUI(jugadorSiguiente);
	}

	public void ModoPeligro(Jugador jugador) {

		if (jugador.getInventario().getPez().size() <= 0) {
			peligro = true;
			FinalizarTurno();
			return;
		}

		dado.setDisable(true);
		rapido.setDisable(true);
		lento.setDisable(true);
		nieve.setDisable(true);
		peces.setDisable(false);
		finalizarTurno.setDisable(false);

		AddEventoHistorial(jugador.getNombre() + "usa un pez para salvarte");

		peligro = true;

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
