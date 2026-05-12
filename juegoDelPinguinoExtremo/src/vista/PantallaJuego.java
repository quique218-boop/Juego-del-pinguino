package vista;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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

	private final Timeline animadorFoca = new Timeline();

	// Crear gestores
	private GestorTablero gestorTablero;
	private GestorBBDD gestorBBDD;

	// Variables modificables
	private ArrayList<Circle> jugadores = new ArrayList<>();
	private int[] posiciones = { 0, 0, 0, 0, 0 };
	int[] dxTotalFoca = { 0, 0, 0, 0, 0 };
	int[] dyTotalFoca = { 0, 0, 0, 0, 0 };
	private int turno = 0;
	private boolean modoBola = false;
	private boolean focaSobornadaEsteTurno = false;
	private boolean focaJuega = false;

	private static boolean sonidosInicializados = true;

	private ArrayList<Usuario> usuarios;

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void inicio(Tablero tablero) {

		animadorFoca.setRate(0.5); // Velocidad reducida para mayor visibilidad

		boolean guardada = false;

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

		finalizarTurno.setDisable(true); // Desactivamos el boton de pasar de turno al principio

		// Creacion gestor tablero y crear un nuevo tablero
		gestorTablero = new GestorTablero();
		
		// Creacion gestor base de datos
		gestorBBDD = new GestorBBDD();

		if (tablero == null) {
			gestorTablero.NuevoTablero();

			for (int i = 0; i < usuarios.size(); i++) {

				gestorBBDD.sumarPartidaJugada(usuarios.get(i).getNombre());

				gestorTablero.añadirJugador(
						new Pinguino(usuarios.get(i).getNombre(), "Azul", new Inventario(), usuarios.get(i)));

			}
			
			gestorTablero.añadirJugador(new Foca("Foca", "Rojo", new Inventario()));
			
			// Asignar el jugador actual como el primero de la lista (Jugador 1)
			gestorTablero.getPartida().setJugadorActual((gestorTablero.getPartida().getArrayListJugador().getFirst()));
		} else {
			
			ArrayList<Usuario> u = new ArrayList<>();
			
			for (int i = 0; i < tablero.getArrayListJugador().size(); i++) {
				
				if(tablero.getArrayListJugador().get(i) instanceof Pinguino) {

				u.add(((Pinguino)tablero.getArrayListJugador().get(i)).getUsuario());
				
				}

			}
			
			usuarios = u;
			
			gestorTablero.setTablero(tablero);
			
			guardada = true;
			
		}

		// Pone el texto del tipo de casilla
		mostrarTiposDeCasillasEnTablero(gestorTablero.getPartida());

		// Definir el atributo del orden de turno de jugadores
		ponerTurnoEnArray();

		// Borramos los circulos cuando no hay jugadores para ellos.
		BorrarFichasSinJugador();

		if (guardada == true) {
			colocarJugadoresCargados();
		}

		int jugadorActualTurno = gestorTablero.getPartida().getJugadorActual().getTurnoEnArray();

		// Pone el efecto del jugador actual al jugador actual
		
		jugadores.get(jugadorActualTurno).getStyleClass().add("current-player"); 
		
		// Inicia el turno en el del jugador actual
		
		turno = jugadorActualTurno;
		
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
	private void handleNewGame() throws IOException { //Cargamos PantallaJuego.fxml y llama a inicio(null)

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/PantallaJuego.fxml"));
		Parent root = loader.load();

		PantallaJuego controller = loader.getController();
		controller.setUsuarios(usuarios);

		controller.inicio(null);

		Stage stage = (Stage) tablero.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("Juego");
		stage.show();
	}

	@FXML
	private void handleSaveGame(ActionEvent event) { //Abre la pestaña de slots en modo guardar

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/slots.fxml"));
			Parent root = loader.load();

			Slots controller = loader.getController();

			controller.setModo(false);
			controller.setPartida(gestorTablero.getPartida());
			controller.setEscenaAnterior(P1.getScene());

			Stage stage = (Stage) P1.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleLoadGame() { //Permite seleccionar un slot y cargar partida

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/slots.fxml"));
			Parent root = loader.load();

			Slots controller = loader.getController();

			controller.setModo(true);
			controller.setPartida(gestorTablero.getPartida());
			controller.setEscenaAnterior(P1.getScene());

			Stage stage = (Stage) P1.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleQuitGame() { //Cerramos el juego
		System.exit(0);
	}

	// Button actions
	@FXML
	private void handleDado(ActionEvent event) {

		// Evita spam del botón
		dado.setDisable(true);

		Jugador jugador = gestorTablero.getPartida().getJugadorActual(); //Obtenemos al jugador al que le toca jugar

		int posInicialSiFoca = jugador.getPos(); //Guarda la pos inicial por si el jugador actual es la foca

		//lanza el dado normal y obtiene el resultado y la posición final
		int[] resultadoYPosFinalDado = ProcesarDado(jugador, null); 
		int resultado = resultadoYPosFinalDado[0];
		int posFinalDado = resultadoYPosFinalDado[1];

		// Mostramos el número que ha salido
		dadoResultText.setText("Ha salido: " + resultado); 

		if (focaJuega) { //Si esta jugandoi la foca se usara su animación especial
			animacionMoverTurnoFoca(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
		} else { //En cambio si el pingüino juega usa la animación normal
			animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
		}

		System.out.println(jugador.getNombre() + " " + jugador.getPos()); //Mostramos por consola el nombre y la posición del jugador

		if (jugador instanceof Foca) { //Si el jugador es la foca comprueba si ha pasado por encima de algún pingüino
			FocaCompruebaRobo(posInicialSiFoca, posFinalDado, jugador);
		}
	}

	@FXML
	private void handleRapido() {

		Jugador jugador = gestorTablero.getPartida().getJugadorActual();

		DadoRapido dadoRapido = (DadoRapido) EncontrarDado(jugador, true); // True == DadoRapido

		if (dadoRapido != null) { //Si tiene dado rápido

			//Guardamos la posición inicial por si es la foca
			int posInicialSiFoca = jugador.getPos();
			//Lanza el dado rápido y obtiene el resultado y la posición final.
			int[] resultadoYPosFinalDado = ProcesarDado(jugador, dadoRapido);
			int resultado = resultadoYPosFinalDado[0];
			int posFinalDado = resultadoYPosFinalDado[1];

			if (focaJuega) { //movemos al jugador con la animación correspondiente
				animacionMoverTurnoFoca(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
			} else {
				animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
			}

			AddEventoHistorial("Usando dado rápido ha salido: " + resultado); //Añadimos al historial el resultado del dado

			if (jugador instanceof Foca) { //// Si es la foca, comprueba si roba/aplasta a algún jugador.
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

			if (focaJuega) {
				animacionMoverTurnoFoca(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
			} else {
				animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugador), posFinalDado, jugador);
			}

			AddEventoHistorial("Usando dado lento ha salido: " + resultado);

			if (jugador instanceof Foca) {
				FocaCompruebaRobo(posInicialSiFoca, posFinalDado, jugador);
			}
		}
	}

	@FXML
	private void handleNieve() { // Seleccionamos el estado del juego

		Jugador jugador = gestorTablero.getPartida().getJugadorActual(); //Obtenemos al jugador actual

		Inventario inventario = jugador.getInventario();

		if (inventario.getBolas().isEmpty()) { // Si la lista no es vacia
			return;
		}

		modoBola = true; //Activamos modo bola
		//Marca visualmente como objetivos a todos los jugadores menos al actual
		for (Jugador jugadorObj : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugadorObj != jugador)
				jugadores.get(jugadorObj.getTurnoEnArray()).getStyleClass().add("obj-player");

		}
	}

	@FXML
	private void selecP1() { //Selecciona al J1 como objetivo
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
		if (!modoBola) { //Si no hay modo bola no se puede seleccionar objetivo
			return;
		}

		tirarBola(objBola); //Lanza la bola de nieve al jugador seleccionado
		modoBola = false;

		for (Jugador jugadorObj : gestorTablero.getPartida().getArrayListJugador()) { //Quita el estilo visual de objetivo

			if (jugadorObj != gestorTablero.getPartida().getJugadorActual())
				jugadores.get(jugadorObj.getTurnoEnArray()).getStyleClass().remove("obj-player");

		}
	}

	private void tirarBola(Jugador objBola) {

		Jugador jugadorAct = gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = jugadorAct.getInventario();

		if (inventario.getBolas().isEmpty()) { //Si no tiene bolas
			return;
		} else if (objBola == null || objBola == jugadorAct || objBola.getPos() == 0) {
			AddEventoHistorial("No se puede disparar a ese jugador");
			return;
		}

		efectos_de_sonido.sonidoBola(); //Reproduce el sonido

		int distancia = CalcularDistancia(jugadorAct, objBola); //Calcula la distancia entre el jugador actual y el objetivo.

		jugadorAct.usarItem(inventario.getBolas().getFirst()); //Gasta una bola de nieve

		ActualizarInventarioGUI(jugadorAct); //Actualiza la interfaz del usuario

		if (CalcularExito(distancia)) { //Calcula si la bola acierta o no

			if (jugadorAct instanceof Pinguino) //Si es un pingüino gana puntos por acertar
				((Pinguino) jugadorAct).sumarPuntos(40);

			objBola.moverPosicion(-1); //Si el objetivo es alcanzado retrocede 1 casilla

			if (focaJuega) { //Animaciones de la foca y el pingüino
				animacionMoverTurnoFoca(gestorTablero.procesarTurnoJugador(jugadorAct), objBola.getPos(), objBola);
			} else {
				animacionMoverJugadores(gestorTablero.procesarTurnoJugador(jugadorAct), objBola.getPos(), objBola);
			}

			AddEventoHistorial("¡¡Has acertado!!");

		} else {

			AddEventoHistorial("Has fallado :(");

		}
	}

	/*
	 * Anima el movimiento de los jugadores normales.
	 * Recibe una lista de movimientos, la posición final del dado
	 * y el jugador que se está moviendo.
	 */
	
	private void animacionMoverJugadores(ArrayList<PairMovimiento> listaMovimientos, int posFinalDado,
			Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada()) // Si la partida ya ha terminado, no se mueve nada.
			return;

		// Desactivamos los botones mientras dura la animación.
		finalizarTurno.setDisable(true);
		rapido.setDisable(true);
		lento.setDisable(true);
		nieve.setDisable(true);

		listaMovimientos.add(0, new PairMovimiento(jugador.getNombre(), posFinalDado)); // Añade el primer movimiento, que es el movimiento causado por el dado.

		Timeline timeline = new Timeline(); //Timeline que controla la animación

		timeline.setRate(0.5); // Velocidad reducida para mayor visibilidad

		Jugador jugadorActual = null;

		// Arrays que acumulan el desplazamiento visual en X e Y.
		
		int[] dxTotal = { 0, 0, 0, 0, 0 };
		int[] dyTotal = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < listaMovimientos.size(); i++) { // Recorremos todos los movimientos que debe hacer cada jugador.

			PairMovimiento jugadorYMovimiento = listaMovimientos.get(i);

			// Buscamos el objeto Jugador que coincide con el nombre guardado en PairMovimiento.
			
			for (int j = 0; jugadorActual == null; j++) {

				jugadorActual = gestorTablero.getPartida().getJugador(j)
						.devolverSiNombreCoincide(jugadorYMovimiento.jugador);
			}

			int jugadorActualIndice = jugadorActual.getTurnoEnArray(); //Obtenemos el indice del jugador dentro del array

			int oldPosition = posiciones[jugadorActualIndice]; //Guardamos la antigua posición

			int movimiento = jugadorYMovimiento.posicion - oldPosition; //Calculamos cuantas casillas se mueven

			posiciones[jugadorActualIndice] += movimiento; //Actualiza la posición lógica dentro del array de posiciones.

			// Limitamos la posición máxima a la casilla final.
			if (posiciones[jugadorActualIndice] >= 50) {
				posiciones[jugadorActualIndice] = 49;
			}

			if (posiciones[jugadorActualIndice] < 0) {
				posiciones[jugadorActualIndice] = 0;
			}

			// Calculamos la fila y columna antiguas.
			int oldRow = oldPosition / COLUMNS;
			int oldCol = oldPosition % COLUMNS;

			//Calculamos fila y columna nuevas.
			int newRow = posiciones[jugadorActualIndice] / COLUMNS;
			int newCol = posiciones[jugadorActualIndice] % COLUMNS;

			// Calculamos cuánto debe desplazarse visualmente la ficha.
			
			dxTotal[jugadorActualIndice] += (newCol - oldCol) * cellWidth;
			dyTotal[jugadorActualIndice] += (newRow - oldRow) * cellHeight;

			if (i == 0) { //Si el primer movimiento empieza desde la posicion visual 0
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.ZERO,
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(), 0),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(), 0)));
				//añadimos el movimiento animado hasta la nueva posición
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotal[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotal[jugadorActualIndice], interpolador)));
			} else {
				//Si no es el primer movimiento, se añade después del anterior
				timeline.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700 * (i + 1)),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotal[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotal[jugadorActualIndice], interpolador)));
			}
			// Reiniciamos las variables para buscar el siguiente jugador.
			jugadorActual = null;
		}
		// Cuando terminamos la animación, actualizamos la posición real de las fichas.
		timeline.setOnFinished(e -> {

			for (int i = 0; i < jugadores.size(); i++) {
				// Reiniciamos el desplazamiento temporal
				jugadores.get(i).setTranslateX(0);
				jugadores.get(i).setTranslateY(0);

				//Colocamos la ficha en su posición real dentro del grid
				GridPane.setRowIndex(jugadores.get(i), posiciones[i] / COLUMNS);
				GridPane.setColumnIndex(jugadores.get(i), posiciones[i] % COLUMNS);
			}

			MostrarEventos(listaMovimientos); //Mostramos en el historial los eventos causados por el movimiento.
			
			//Se permite finalizar el turno después de moverse
			
			finalizarTurno.setDisable(false);

			if (gestorTablero.getPartida().getJugadorActual().getDeudaTurnos() > 0) //Si el jugador tiene turnos perdidos se finaliza automaticamente
				FinalizarTurno();

			ActualizarInventarioGUI(gestorTablero.getPartida().getJugadorActual()); //// Se actualizan los textos y botones del inventario.
		});

		timeline.play(); //Inicia la animación
	}

	/*
	 * Anima los movimientos cuando está jugando la foca.
	 * Es parecido a animacionMoverJugadores, pero usa un Timeline global
	 * porque la foca puede hacer varias acciones seguidas en su turno.
	 */
	
	private void animacionMoverTurnoFoca(ArrayList<PairMovimiento> listaMovimientos, int posFinalDado,
			Jugador jugador) {

		if (gestorTablero.getPartida().getFinalizada())
			return;
		
		//Desactivamos los botones durante la animación
		
		finalizarTurno.setDisable(true);
		rapido.setDisable(true);
		lento.setDisable(true);
		nieve.setDisable(true);

		listaMovimientos.add(0, new PairMovimiento(jugador.getNombre(), posFinalDado)); // Añade el movimiento principal a la lista.

		Jugador jugadorActual = null;

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

			dxTotalFoca[jugadorActualIndice] += (newCol - oldCol) * cellWidth;
			dyTotalFoca[jugadorActualIndice] += (newRow - oldRow) * cellHeight;

			if (i == 0 && animadorFoca.totalDurationProperty().getValue() == Duration.millis(0)) {
				animadorFoca.getKeyFrames()
						.add(new KeyFrame((animadorFoca.getTotalDuration()),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(), 0),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(), 0)));

				animadorFoca.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700).add(animadorFoca.getTotalDuration()),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotalFoca[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotalFoca[jugadorActualIndice], interpolador)));
			} else {
				animadorFoca.getKeyFrames()
						.add(new KeyFrame(Duration.millis(700 * (i + 1)).add(animadorFoca.getTotalDuration()),
								new KeyValue(jugadores.get(jugadorActualIndice).translateXProperty(),
										dxTotalFoca[jugadorActualIndice], interpolador),
								new KeyValue(jugadores.get(jugadorActualIndice).translateYProperty(),
										dyTotalFoca[jugadorActualIndice], interpolador)));
			}

			jugadorActual = null;
		}

		animadorFoca.setOnFinished(e -> {

			for (int i = 0; i < jugadores.size(); i++) {
				// reset translation
				jugadores.get(i).setTranslateX(0);
				jugadores.get(i).setTranslateY(0);

				// set real position in grid
				GridPane.setRowIndex(jugadores.get(i), posiciones[i] / COLUMNS);
				GridPane.setColumnIndex(jugadores.get(i), posiciones[i] % COLUMNS);
			}

			MostrarEventos(listaMovimientos);

			finalizarTurno.setDisable(false);

			if (gestorTablero.getPartida().getJugadorActual().getDeudaTurnos() > 0)
				FinalizarTurno();

			ActualizarInventarioGUI(gestorTablero.getPartida().getJugadorActual());

			System.out.println("Antes de borrado" + animadorFoca.getTotalDuration());

			animadorFoca.getKeyFrames().removeAll(animadorFoca.getKeyFrames());

			if (focaJuega) {
				FinalizarTurno();
			}

			dxTotalFoca = new int[] { 0, 0, 0, 0, 0 };
			dyTotalFoca = new int[] { 0, 0, 0, 0, 0 };

			System.out.println("Despues de borrado" + animadorFoca.getTotalDuration());
		});
	}

	public void MostrarEventos(ArrayList<PairMovimiento> listaMovimientos) {

		ArrayList<String> listaNombres = new ArrayList<>(); // Lista auxiliar para guardar jugadores únicos que se han movido.

		ArrayList<Jugador> jugadores = new ArrayList<>(listaNombres.size()); // Busca los jugadores que aparecen en la lista de movimientos.

		for (PairMovimiento nombres : listaMovimientos) {

			String nombre = nombres.jugador;

			Jugador jugadorEncontrado = null;

			for (int i = 0; jugadorEncontrado == null; i++) { // Busca al jugador por nombre.

				jugadorEncontrado = gestorTablero.getPartida().getJugador(i).devolverSiNombreCoincide(nombre);

				if (jugadorEncontrado != null && !jugadores.contains(jugadorEncontrado))
					jugadores.add(jugadorEncontrado);
			}
		}

		for (Jugador jugador : jugadores) { // Recorre cada jugador que se ha movido.

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

				if (posicion > 49)
					posicion = 49;

				Casilla casilla = gestorTablero.getPartida().getCasilla(posicion);

				if (posicionSiguiente > 49)
					posicionSiguiente = 49;

				Casilla casillaSiguiente = gestorTablero.getPartida().getCasilla(posicionSiguiente);

				getYProcesarTodosLosEstados();

				switch (casilla) {

				case Agujero a -> { // Si sigue en un agujero, significa que ha caído en él.
					if (casillaSiguiente instanceof Agujero)
						AddEventoHistorial(jugador.getNombre() + " ha caido en un agujero");
				}

				case Evento e -> { // Muestra el resultado del evento aleatorio.
					AddEventoHistorial(
							jugador.getNombre() + " ha caido en un evento y el evento ha sido: " + e.getResultado());
				}

				case Trineo t -> { // Si sigue en trineo, muestra que lo ha usado.
					if (casillaSiguiente instanceof Trineo)
						AddEventoHistorial(jugador.getNombre() + " ha utilizado un trineo");
				}

				case SueloQuebradizo s -> { // Muestra el efecto del suelo quebradizo.
					AddEventoHistorial(s.getResultado());
				}

				case Oso o -> { // Si vuelve al inicio, ha sido atacado por el oso.
					if (casillaSiguiente.getPosicion() == 0) {
						AddEventoHistorial(jugador.getNombre() + " ha sido atacado por un oso");
					} else { //Si no vuelve al inicio es porque ha usado un pez
						AddEventoHistorial(jugador.getNombre() + " ha usado un pez para evitar ser atacado por un oso");
					}
				}

				case Normal n -> { //Las casillas normales no generan efectos especiales
				}

				default -> throw new IllegalArgumentException("Unexpected value: " + casilla);

				}
			}

			getYProcesarTodosLosEstados(); // Procesa otra vez posibles estados después de revisar las casillas.

			Foca foca = (Foca) gestorTablero.getPartida().getArrayListJugador().getLast(); // Obtiene la foca, que siempre está al final de la lista.

			if (foca.getDeudaTurnos() == 2 && !focaSobornadaEsteTurno) { //Si la foca es sobornada
				AddEventoHistorial(foca.getNombre() + " ha sido sobornada para no golpear y no se movera en 2 turnos");
				focaSobornadaEsteTurno = true;
			} else if (foca.getDeudaTurnos() == 0) { // Cuando la foca ya no está sobornada, permite volver a mostrar el mensaje en el futuro.
				focaSobornadaEsteTurno = false;
			}
		}
	}

	public void FinalizarTurno() { //Finaliza el turno del jugador actual y pasa al siguiente

		gestorTablero.getPartida().addTurnos(); //Aumenta el contador total de turnos de la partida.

		comprobarGanador(); //Comprobamos si alguien ha ganado antes de pasar de turno

		Jugador jugadorActual = gestorTablero.getPartida().getJugador(turno); //Guardamos el jugador que acaba de jugar

		Jugador jugadorSiguiente;

		turno = (turno + 1) % jugadores.size(); // Cambio de turno

		jugadorSiguiente = gestorTablero.getPartida().getJugador(turno); //Obtenemos al siguiente jugador

		while (jugadorSiguiente.getDeudaTurnos() > 0) { //Si tiene turnos perdidos se le salta

			if (jugadorSiguiente.getDeudaTurnos() > 0) {
				AddEventoHistorial(jugadorSiguiente.getNombre() + " pierde el turno");
				jugadorSiguiente.reducirDeudaTurnos();
			}

			turno = (turno + 1) % jugadores.size(); // Cambio de turno

			jugadorSiguiente = gestorTablero.getPartida().getJugador(turno);

		}

		
		gestorTablero.getPartida().setJugadorActual(gestorTablero.getPartida().getJugador(turno)); //Cambia el jugador actual en la logica del tablero

		jugadores.get(jugadorActual.getTurnoEnArray()).getStyleClass().remove("current-player"); //Quita el estilo visual del jugador anterior.

		jugadores.get(jugadorSiguiente.getTurnoEnArray()).getStyleClass().add("current-player"); //Añade el estilo visual al nuevo jugador

		if (jugadorSiguiente instanceof Foca) // Si el siguiente jugador es la foca, ejecuta su turno automático.
			turnoFoca();
		else
			focaJuega = false;

		resetVariablesDeTurno(); // Reinicia variables temporales del turno.

		ActualizarInventarioGUI(jugadorSiguiente); // Actualiza el inventario mostrado en pantalla.

	}

	private void turnoFoca() {

		focaJuega = true; //Indicamos que esta jugando la foca

		Foca foca = (Foca) gestorTablero.getPartida().getArrayListJugador().getLast(); 	//Obtenemos la foca, que está guardada como último jugador de la lista.

		ArrayList<ArrayList<Integer>> acciones = gestorTablero.ejecutarTurnoCompleto(foca); //Se calculan todas las acciones que hará la foca durante su turno.

		boolean ArrayDados;

		if (acciones.getFirst().contains(9)) // Si la primera acción contiene 9, significa que empieza usando bolas de nieve
			ArrayDados = false;
		else
			ArrayDados = true;

		for (int i = 0; i < acciones.size(); i++) { //Se recorren los grupos de acciones de la foca.

			if (ArrayDados) { //Si tira dados

				ArrayList<Integer> tirarDados = acciones.get(i);

				for (int dado : tirarDados) {

					if (foca == gestorTablero.getPartida().getJugadorActual()) { //Comprobamos que aun sea el turno de la foca

						switch (dado) { //Según el número que salga, la foca usa un dado u otro.

						case 0:
							System.out.println("FOCA HA USADO NORMAL");
							handleDado(null);
							break;
						case 1:
							System.out.println("FOCA HA USADO LENTO");
							handleLento();
							break;
						case 2:
							System.out.println("FOCA HA USADO RAPIDO");
							handleRapido();
							break;
						}

					} else {
						return;
					}
				}
			} else {

				ArrayList<Integer> tirarBolas = acciones.get(i); //Si utiliza bolas de nieve

				for (int objetivo : tirarBolas) {

					if (objetivo != 9) { 

						handleNieve(); //Activa el modo bola de nieve

						switch (objetivo) { //Según el número, selecciona al jugador objetivo.

						case 0:
							selecP1();
							break;
						case 1:
							selecP2();
							break;
						case 2:
							selecP3();
							break;
						case 3:
							selecP4();
							break;

						}
					}
				}
			}

			ArrayDados = (ArrayDados) ? false : true; //La foca alterna entre acciones de dados y acciones de bolas

		}

		System.out.println("DESPUES DE SU TURNO FOCA ESTA EN " + foca.getPos());

		animadorFoca.play(); //Se reproducen todas las animaciones acumuladas de la foca
	}

	private void finalizarPartida(Jugador ganador) {

		if (ganador instanceof Pinguino p) { //Si gana un pingüino, recibe puntos extra y se suma una partida ganada.

			p.sumarPuntos(500);

			gestorBBDD.sumarPartidaGanada(p.getUsuario().getNombre());

		}

		gestorTablero.getPartida().setFinalizada(true); //La partida se marca como finalizada

		for (Jugador j : gestorTablero.getPartida().getArrayListJugador()) {

			if (j instanceof Pinguino p) { //Suma la puntuación de todos los pingüinos a sus usuarios.

				gestorBBDD.sumarPuntuacion(p.getUsuario().getNombre(), p.getPuntuacion());

			}

		}
		
		int slot =
		        gestorTablero.getPartida().getId(); //En caso de que la patida estuviera guardada se obtendría el slot

		    if (slot > 0) { //Si venía de una partida guardada, se borra al terminar.

		        GestorBBDD.borrarPartida(slot);

		    }

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/Victoria.fxml")); //Cargamos la pantalla de victoria
			Parent root = loader.load();

			Victoria controller = loader.getController(); //Obtenemos el controlador de la pantalla de victoria.

			controller.setGanador(ganador); //Se define el ganador y prepara la pantalla
			controller.inicio();

			Stage stage = (Stage) tablero.getScene().getWindow(); //Cambiamos la escena por la pantalla de victoria
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Funciones auxiliares

	private void BorrarFichasSinJugador() { //Eliminamos las fichas visuales que no sirven

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

	private void FocaCompruebaRobo(int posInicialSiFoca, int PosFinalDado, Jugador jugador) { //Comprobamos si la foca ha robado
		for (Jugador jugadorRobado : gestorTablero.getPartida().getArrayListJugador()) {
			if (jugadorRobado != jugador) { //Evita compararse consigo misma
				// Si la foca estaba antes del jugador y termina después, significa que ha pasado por encima.
				if (posInicialSiFoca < jugadorRobado.getPos() && PosFinalDado > jugadorRobado.getPos()) {
					((Foca) jugador).aplastarJugador((Pinguino) jugadorRobado);
					AddEventoHistorial(jugadorRobado.getNombre() + " ha sido robado por la foca");
				}
			}
		}
	}

	/*
	 * Procesa el lanzamiento de un dado.
	 * Puede ser dado normal, dado rápido o dado lento.
	 * Devuelve un array con el resultado del dado y la posición final.
	 */
	
	private int[] ProcesarDado(Jugador jugador, Dado dado) {

		int oldPos = jugador.getPos(); // Guardamos la posición antes de tirar.

		int resultado = 0;

		if (dado == null) { //Si es null se tira el normal
			resultado = gestorTablero.tirarDado(jugador);
		} else {
			if (dado instanceof DadoRapido)
				resultado = gestorTablero.tirarDado(jugador, (DadoRapido) dado);
			else if (dado instanceof DadoLento)
				resultado = gestorTablero.tirarDado(jugador, (DadoLento) dado);
		}

		//Se calcula la posición final antes de aplicar efectos de casilla.
		int PosFinalDado = oldPos + resultado;

		//Si es un pingüino, gana puntos según el resultado del dado.
		if (jugador instanceof Pinguino p) {

			p.sumarPuntos(resultado * 10);

		}

		//Devolvemos el resultado y la posición final
		int[] resultadoYPosFinalDado = { resultado, PosFinalDado };

		return resultadoYPosFinalDado;
	}

	private int CalcularDistancia(Jugador j1, Jugador j2) { //Calculamos la distancia entre dos jugadores

		if (j1.getPos() > j2.getPos())
			return j1.getPos() - j2.getPos();

		else
			return j2.getPos() - j1.getPos();
	}

	private boolean CalcularExito(int distancia) { //Se calcula si una bola de nieve acertara, contra mayor distancia menor probabilidad

		int Probabilidad = (int) Math
				.round(Math.pow(RATIO_DESCENSO_PROBABILIDAD * e, -(RATIO_DESCENSO_PROBABILIDAD * distancia)) * 10);

		double resultado = random.nextDouble(10 - Probabilidad + 1) + Probabilidad;

		return (resultado >= 10);
	}

	/*
	 * Se actualizan en la pantalla los objetos del inventario del jugador actual.
	 * Ademas se activan o desactivan los botones según los objetos disponibles.
	 */
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

		//Se actualizan los textos del inventario.
		
		rapido_t.setText("Dado rápido: " + dadoRapido);

		lento_t.setText("Dado lento: " + dadoLento);

		peces_t.setText("Peces: " + num_peces);

		nieve_t.setText("Bolas de nieve: " + bolasNieve);

		if (!focaJuega) { //Si no juega la foca, se activan o desactivan los botones según el inventario.
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

		return null; //Si no se encuentra

	}

	private void getYProcesarTodosLosEstados() { //Obtiene y procesa todos los estados especiales generales del tablero.
		ProcesarEstado(gestorTablero.estadoPeleas());
		ProcesarEstado(gestorTablero.estadoGolpeos());
	}

	private void ProcesarEstado(ArrayList<String> estados) {  //Recibe una lista de mensajes de estado y los añade al historial.
		if (estados != null) {
			for (String estado : estados) {
				AddEventoHistorial(estado);
			}
		}
	}

	private void AddEventoHistorial(String evento) { //Añade un evento al historial visual de la partida.

		Text texto = new Text(evento);
		texto.getStyleClass().add("events");

		ListaObservable.add(0, texto);

		ListaEventos.setItems(ListaObservable);
	}

	private void resetVariablesDeTurno() { //Reinicia variables temporales al terminar un turno.

		modoBola = false;
		// Desactivar finalizar turno
		finalizarTurno.setDisable(true);

		if (!focaJuega)
			dado.setDisable(false);

		for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

			jugadores.get(jugador.getTurnoEnArray()).getStyleClass().remove("obj-player");

		}
	}

	private void comprobarGanador() { //Comprueba si algún jugador ha llegado a la casilla final.

		ArrayList<Jugador> jugadores = gestorTablero.getPartida().getArrayListJugador();

		for (Jugador jugador : jugadores) {
			if (jugador.getPos() == 49) {
				finalizarPartida(jugador);
				return;
			}
		}
	}

	public void ponerTurnoEnArray() { //Asigna a cada jugador su posición dentro del array de turnos.

		for (int i = 0; i < gestorTablero.getPartida().getArrayListJugador().size(); i++) {

			gestorTablero.getPartida().getJugador(i).setTurnoEnArray(i);

		}
	}

	private void colocarJugadoresCargados() { //Coloca las fichas en el tablero cuando se carga una partida guardada.

		for (int i = 0; i < gestorTablero.getPartida().getArrayListJugador().size(); i++) {

			Jugador jugador = gestorTablero.getPartida().getJugador(i);

			int pos = jugador.getPos(); //Obtiene la posiciones del array

			posiciones[i] = pos; //Guardamos las posiciones en el array

			// Coloca visualmente la ficha en el GridPane.
			GridPane.setRowIndex(jugadores.get(i), pos / COLUMNS);
			GridPane.setColumnIndex(jugadores.get(i), pos % COLUMNS);
		}
	}
}
