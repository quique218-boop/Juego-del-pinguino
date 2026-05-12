package vista;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Menu {

	private boolean sAmbiental = false; //Indicamos si el sonido ambientar está activo o no
	private double volumen = 0.5; // 50% //Volumen inicial

	public static MediaPlayer musicaAmbiente; //Reproductor estatico

	private void cambiarVentana(ActionEvent event, String fxml) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxml));

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setX(200);
			stage.setY(40);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		try {
			if (musicaAmbiente == null) {
				String ruta = getClass().getResource("/recursos/Ambiental.mp3").toExternalForm();

				Media media = new Media(ruta);
				musicaAmbiente = new MediaPlayer(media);

				musicaAmbiente.setCycleCount(MediaPlayer.INDEFINITE);
				musicaAmbiente.setVolume(volumen);

				if (sAmbiental) {
					musicaAmbiente.play();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void nuevaPartida(ActionEvent event) {
		cambiarVentana(event, "/recursos/seleccionJugadores.fxml"); //Cambiamos a la ventana de Seleccion de Jugadores
	}

	@FXML
	private void irEstadisticas(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/recursos/estadisticas.fxml")); //Carga el FXML de estadísticas
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Obtiene la ventana actual
			stage.setScene(new Scene(root)); //Cambia la escena actual por la de estadística
		} catch (Exception e) {
			e.printStackTrace(); //En caso de que haya un error se muestra por consola
		}
	}

	@FXML
	private void cargarPartida(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/slots.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setX(200); //Colocamos la pantalla en una posición concreta
			stage.setY(40);
			stage.show();

			Slots controller = loader.getController(); //Obtenemos el controlador de slots
			controller.setModo(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	private void Salir() { //Cerramos el juego
		System.exit(0);
	}
}