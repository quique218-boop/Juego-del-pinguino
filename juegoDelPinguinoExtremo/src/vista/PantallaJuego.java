package vista;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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

	private final Double RATIO_DESCENSO_PROBABILIDAD = 0.45d;
	private final Double e = 2.718281828459045235360d;

	private boolean recursiva = false;
	
	// Cell size (aproximado) TODO
	double cellWidth = tablero.getWidth() / COLUMNS;
	double cellHeight = tablero.getHeight() / 10;

	@FXML
	private void initialize() {

		AddEventoHistorial("¡El juego ha comenzado!");

		gestorTablero = new GestorTablero();

		gestorTablero.NuevoTablero();

		gestorTablero.añadirJugador(new Pinguino("Jugador1", "Azul", new Inventario()));

		gestorTablero.añadirJugador(new Pinguino("Jugador2", "Verde", new Inventario()));

		gestorTablero.añadirJugador(new Foca("Foca", "Amarillo", new Inventario()));

		// TEMPORALMENTE DAMOS LOS OBJETOS AL PRINCIPIO
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoLento());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoRapido());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaBolas(new BolaDeNieve());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaPez(new Pez());

		gestorTablero.getPartida().getJugador(1).getInventario().addListaPez(new Pez());
		gestorTablero.getPartida().getJugador(1).getInventario().addListaPez(new Pez());

		// Show board info
		mostrarTiposDeCasillasEnTablero(gestorTablero.getPartida());

		for (int i = 0; i < gestorTablero.getPartida().getArrayListJugador().size(); i++) {

			gestorTablero.getPartida().getArrayListJugador().get(i).setTurnoEnArray(i);

		}

		BorrarFichasSinJugador();

		P1.getStyleClass().add("current-player");

		finalizarTurno.setDisable(true);

		ponerTurnoEnArray();

		gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getArrayListJugador().getFirst()));

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
		// TODO
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

		int resultado = gestorTablero.tirarDado(jugador);

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		// Update the position
		movePlayers(resultado, jugador);

		finalizarTurno.setDisable(false);
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

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		Foca foca = null;

		for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugador instanceof Foca) {

				foca = (Foca) jugador;

			}
		}

		if (inventario.getPez().isEmpty()) { // Si la lista no es vacia

			return;

		} else {

			Casilla casillaActual = gestorTablero.getPartida().getCasilla(pinguino.getPos()); // Casilla donde se
																								// encuentra

			if (casillaActual instanceof Oso) {

				pinguino.usarItem(pinguino.getInventario().getPez().getFirst());

			} else if (pinguino.getPos() == foca.getPos()) { // Si estamos en la misma casilla de foca

				pinguino.usarItem(pinguino.getInventario().getPez().getFirst());

				foca.esSobornado();

			}
		}

		ActualizarInventarioGUI(pinguino);
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

		}

		if (listaJugadores.size() == 3) {

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
		} else if (objBola == null || objBola == pinguinoAct) {
			return;
		}

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

		int peces = inventario.getPez().size();

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

		peces_t.setText("Peces: " + peces);

		nieve_t.setText("Bolas de nieve: " + bolasNieve);

		if (dadoRapido == 0)
			rapido.setDisable(true);
		else
			rapido.setDisable(false);

		if (dadoLento == 0)
			lento.setDisable(true);
		else
			lento.setDisable(false);

		if (peces == 0)
			this.peces.setDisable(true);
		else
			this.peces.setDisable(false);

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

			if (!recursiva) {
				caerEnCasilla(jugador);
				recursiva = true;
			} else {

				recursiva = false;
			}

		});

		slide.play();

	}

	public void FinalizarTurno() {

		if (!dado.isDisabled())
			return;

		Jugador jugadorActual = gestorTablero.getPartida().getJugador(turno);

		turno = (turno + 1) % jugadores.size(); // Cambio de turno

		Jugador jugadorSiguiente = gestorTablero.getPartida().getJugador(turno);

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

	public void caerEnCasilla(Jugador jugador) {

		int PosInicial = jugador.getPos();

		System.out.println(jugador.getNombre() + "En posicion inicial " + PosInicial); // TODO DEBUG TEXT

		Casilla casilla = gestorTablero.getPartida().getCasilla(PosInicial);

		if (casilla instanceof Normal) {
			recursiva = false;
			return;
		}

		casilla.realizarAccion(gestorTablero.getPartida(), jugador);

		int PosFinal = jugador.getPos();

		int movimiento = PosFinal - PosInicial;

		System.out.println(jugador.getNombre() + "movido a posicion final " + PosFinal); // TODO DEBUG TEXT

		AddEventoHistorial(jugador.getNombre() + " ha caido en " + casilla.getClass().getSimpleName());

		movePlayers(movimiento, jugador);

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
