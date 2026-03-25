package vista;

import java.util.Random;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private int p1Position = 0;
	private int p2Position = 0;
	private int p3Position = 0;
	private int p4Position = 0;// Tracks current position (from 0 to 49 in a 5x10 grid)
	private int turno = 0;
	private Circle[] jugadores = new Circle[]{P1, P2, P3, P4};
	private int[] posiciones = new int[]{p1Position, p2Position, p3Position, p4Position};
	private static final int COLUMNS = 5;

	private static final String TAG_CASILLA_TEXT = "CASILLA_TEXT";
	private final Random rand = new Random();
	private boolean modoBola = false;
	@FXML
	private void initialize() {

		AddEventoHistorial("¡El juego ha comenzado!");

		gestorTablero = new GestorTablero();

		Inventario inventario = new Inventario();

		gestorTablero.NuevoTablero();

		gestorTablero.añadirJugador(new Pinguino("Jugador1", "Azul", inventario));

		gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoLento());

		gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getJugador(0)));

		lento_t.setText("Dado lento: " + gestorTablero.getPartida().getJugador(0).getInventario().getDado().size());

		gestorTablero.getPartida().getJugador(0).getInventario().addListaBolas(new BolaDeNieve());
		
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

				tablero.getChildren().add(texto);
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
		System.out.println("Exit...");
		// TODO
	}

	// Button actions
	@FXML
	private void handleDado(ActionEvent event) {

		Jugador pingu = (Jugador) gestorTablero.getPartida().getJugadorActual();

		System.out.println("Pos pingu previa:" + pingu.getPos());

		int resultado = gestorTablero.tirarDado(pingu);

		System.out.println("Pos pingu actual:" + pingu.getPos());

		// Update the Text
		dadoResultText.setText("Ha salido: " + resultado);

		// Update the position
		moveP1(resultado);
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
		
		
		if(gestorTablero.getPartida().getFinalizada()) {return;}
			
			

		// Evita spam del botón
	    dado.setDisable(true);
	    
	    int jugadorActual = turno;

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

	    // Cell size (aproximado)
	    double cellWidth = tablero.getWidth() / COLUMNS;
	    double cellHeight = tablero.getHeight() / 10;

	    double dx = (newCol - oldCol) * cellWidth;
	    double dy = (newRow - oldRow) * cellHeight;

	    TranslateTransition slide = new TranslateTransition(Duration.millis(350), jugadores[jugadorActual]);

	    slide.setByX(dx);
	    slide.setByY(dy);

	    slide.setOnFinished(e -> {

	        // reset translation
	    	jugadores[jugadorActual].setTranslateX(0);
	    	jugadores[jugadorActual].setTranslateY(0);

	        // set real position in grid
	        GridPane.setRowIndex(jugadores[jugadorActual], newRow);
	        GridPane.setColumnIndex(jugadores[jugadorActual], newCol);
	        
	        turno = (turno + 1) % jugadores.length;

	        // Cambiar jugador actual en la lógica
	        gestorTablero.getPartida().setJugadorActual(
	            gestorTablero.getPartida().getJugador(turno)
	            );

	        // volver a activar el botón
	        dado.setDisable(false);
	    });

	    slide.play();
	    
	    
	}
	

	@FXML
	private void handleRapido() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if (inventario.getDado().size() < 0) {

			System.out.println("No tienes ningún dado especial");

		} else {

			for (Dado d : pinguino.getInventario().getDado()) {

				if (d instanceof DadoRapido) {

					d.tirarDado(); // Tiramos el primer dado rápido que encontramos

				}
			}
		}
	}

	@FXML
	private void handleLento() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if (inventario.getDado().size() < 0) {

			System.out.println("No tienes ningún dado especial");

		} else {

			for (Dado d : pinguino.getInventario().getDado()) {

				if (d instanceof DadoLento) {

					System.out.println("Pos pingu previa:" + pinguino.getPos());

					int resultado = gestorTablero.tirarDado(pinguino, d);

					gestorTablero.getPartida().getJugador(0).getInventario().addListaDado(new DadoLento());

					System.out.println("Pos pingu actual:" + pinguino.getPos());

					AddEventoHistorial("Usando dado lento ha salido: " + resultado);

					lento_t.setText(
							"Dado lento: " + gestorTablero.getPartida().getJugador(0).getInventario().getDado().size());

					moveP1(resultado);

					return;
				}
			}
		}
	}

	@FXML
	private void handlePeces() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		Foca foca = null;

		for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugador.getNombre().equalsIgnoreCase("foca")) {

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

			} else if (pinguino.getPos() == foca.getPos() && pinguino.getInventario().getPez().isEmpty()) {

				foca.golpearJugador(pinguino, gestorTablero.getPartida()); // Te empuja al anterior agujero

			} else if (casillaActual instanceof Oso) {

				foca.usarItem(foca.getInventario().getPez().getFirst());

			}

		}

	}

	@FXML
	private void handleNieve() { //Seleccionamos el estado del juego
		
		
		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if (inventario.getBolas().isEmpty()) { // Si la lista no es vacia

			return;
		
		}
		
		modoBola = true;
		
	}
	
	
	@FXML
	
	private void selecPingu(Pinguino objBola) {
		
		if(!modoBola) {
						
			return;
			
		}
		
		tirarBola(objBola);
		
		modoBola = false;		
		
	}
	
	
	private void tirarBola(Pinguino objBola) {
		
		Random random = new Random();
		
		Pinguino pinguinoact = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguinoact.getInventario();
		
		if(inventario.getBolas().isEmpty()) {
			
			return;
			
		}else if(objBola == null || objBola == pinguinoact) {
			
			return;
			
		}
		
		pinguinoact.usarItem(inventario.getBolas().getFirst());
		
		int skill = random .nextInt(10) + 1;
		
		if(skill > 5 ) {
			
			objBola.moverPosicion(-1);
		}
		
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
