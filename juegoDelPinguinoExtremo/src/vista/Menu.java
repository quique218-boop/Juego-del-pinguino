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
import vista.Slots.Modo;

public class Menu {

	private boolean sAmbiental = false;
	private double volumen = 0.5; // 50%

	public static MediaPlayer musicaAmbiente;

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
		cambiarVentana(event, "/recursos/seleccionJugadores.fxml");
	}

	@FXML
	private void irEstadisticas(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/recursos/estadisticas.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void cargarPartida(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/slots.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setX(200);
			stage.setY(40);
			stage.show();

			Slots controller = loader.getController();
			controller.setModo(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void Personalizar(ActionEvent event) {
		cambiarVentana(event, "/recursos/personalizar.fxml");
	}

	@FXML
	private void Configuracion(ActionEvent event) {
		cambiarVentana(event, "/recursos/Configuracion.fxml");
	}

	@FXML
	private void Salir() {
		System.exit(0);
	}
}