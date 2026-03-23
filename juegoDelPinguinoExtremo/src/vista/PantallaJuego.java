package vista;

import java.text.NumberFormat.Style;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.StyleClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
	private ListView<Text> ListaEventos;

	private ObservableList<Text> ListaObservable = FXCollections.observableArrayList();

	private GestorTablero gestorTablero;
	// ONLY FOR TESTING!!!
	private int p1Position = 0; // Tracks current position (from 0 to 49 in a 5x10 grid)
	private static final int COLUMNS = 5;

	private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";
	private final Random rand = new Random();

	@FXML
	private void initialize() {

		AddEventoHistorial("¡El juego ha comenzado!");

		gestorTablero = new GestorTablero();

		Inventario inventario = new Inventario();

		gestorTablero.NuevoTablero();

		gestorTablero.añadirJugador(new Pinguino("Jugador1", "Azul", inventario));

		// TEMPORALMENTE DAMOS TODOS LOS OBJETOS AL PRINCIPIO
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoLento());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoRapido());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaBolas(new BolaDeNieve());
		gestorTablero.getPartida().getJugador(0).getInventario().addListaPez(new Pez());

		gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getJugador(0)));

		ActualizarInventarioGUI(gestorTablero.getPartida().getJugadorActual());

		// Show board info
		mostrarTiposDeCasillasEnTablero(gestorTablero.getPartida());
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
	private void handleNewGame() {
		System.out.println("New game.");
		// TODO
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

		Jugador jugador = (Jugador) gestorTablero.getPartida().getJugadorActual();

		System.out.println("Pos pingu previa:" + jugador.getPos());

		int resultado = gestorTablero.tirarDado(jugador);

		System.out.println("Pos pingu actual:" + jugador.getPos());

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		// Update the position
		moveP1(resultado);

		ActualizarInventarioGUI(jugador);
	}

	/*
	 * Old simple version private void moveP1(int steps) { p1Position += steps;
	 * 
	 * // Bound player if (p1Position >= 50) { p1Position = 49; // 5 columns * 10
	 * rows = 50 cells (index 0 to 49) }
	 * 
	 * if (p1Position < 0) { p1Position = 0; }
	 * 
	 * // Check row and column int row = p1Position / COLUMNS; int col = p1Position
	 * % COLUMNS;
	 * 
	 * // Change P1 property to match row and column GridPane.setRowIndex(P1, row);
	 * GridPane.setColumnIndex(P1, col); }
	 */

	private void moveP1(int steps) {

		// Evita spam del botón
		dado.setDisable(true);

		int oldPosition = p1Position;

		p1Position += steps;

		// Bound player
		if (p1Position >= 50) {
			p1Position = 49;
		}

		if (p1Position < 0) {
			p1Position = 0;
		}

		// OLD position
		int oldRow = oldPosition / COLUMNS;
		int oldCol = oldPosition % COLUMNS;

		// NEW position
		int newRow = p1Position / COLUMNS;
		int newCol = p1Position % COLUMNS;

		// Cell size (aproximado)
		double cellWidth = tablero.getWidth() / COLUMNS;
		double cellHeight = tablero.getHeight() / 10;

		double dx = (newCol - oldCol) * cellWidth;
		double dy = (newRow - oldRow) * cellHeight;

		TranslateTransition slide = new TranslateTransition(Duration.millis(350), P1);

		slide.setByX(dx);
		slide.setByY(dy);

		slide.setOnFinished(e -> {

			// reset translation
			P1.setTranslateX(0);
			P1.setTranslateY(0);

			// set real position in grid
			GridPane.setRowIndex(P1, newRow);
			GridPane.setColumnIndex(P1, newCol);

			// volver a activar el botón
			dado.setDisable(false);
		});

		slide.play();
	}

	@FXML
	private void handleRapido() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		DadoRapido dadoRapido = (DadoRapido) EncontrarDado(pinguino, true); // True == DadoRapido

		if (dadoRapido != null) {

			int resultado = gestorTablero.tirarDado(pinguino, dadoRapido);

			AddEventoHistorial("Usando dado rápido ha salido: " + resultado);

			moveP1(resultado);
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

			moveP1(resultado);
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
	private void handleNieve() {
		System.out.println("Snow.");

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		ActualizarInventarioGUI(pinguino);
		// TODO
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

	public void setGestorPartida(GestorTablero gestorTablero) {
		this.gestorTablero = gestorTablero;
	}
}
