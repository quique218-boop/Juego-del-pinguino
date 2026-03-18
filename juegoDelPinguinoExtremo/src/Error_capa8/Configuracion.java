package Error_capa8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Configuracion {

	private boolean sAmbiental = true;

	private double volumen = 0.5; // 50%

	private static boolean sObjetos = true;

	private static boolean sFoca = true;

	private static boolean mVictory = true;


	private void cambiarVentana(ActionEvent event, String fxml) {

		try {

			Parent root = FXMLLoader.load(getClass().getResource(fxml));

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.setScene(new Scene(root));

			stage.show();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@FXML
	
	private void BMenu(ActionEvent event) {

		cambiarVentana(event, "/recursos/Menu.fxml");

	}

	@FXML

	private void actAmbiental() {

		sAmbiental = true;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);

			Menu.musicaAmbiente.play();

			System.out.println("Sonido Ambiental activado");
			
		}

	}

	
	@FXML

	private void desAmbiental() {

		sAmbiental = false;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.stop();
			
			System.out.println("Sonido Ambiental desactivado");

		}

	}

	@FXML

	private void actSonidosObjetos() {

		sObjetos = true;

		System.out.println("Sonidos de Objetos activados");
		
	}

	@FXML

	private void desSonidosObjetos() {

		sObjetos = false;

		System.out.println("Sonidos de Objetos desactivados");
	}

	@FXML

	private void actSonidosFoca() {

		sFoca = true;

		System.out.println("Sonidos de Foca activados");
	}

	@FXML

	private void desSonidosFoca() {

		sFoca = false;

		System.out.println("Sonidos de Foca desactivados");
	}

	@FXML

	private void MVictory() {

		mVictory = true;

		System.out.println("Musica de Victoria activada");
		
	}

	@FXML

	private void desMVictory() {

		mVictory = false;

		System.out.println("Musica de Victoria desactivada");
		
	}

	@FXML

	private void volumen0() {

		volumen = 0.0;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);

			System.out.println("Volumen del juego silenciado");
		}

	}

	@FXML

	private void volumen25() {

		volumen = 0.25;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);

			System.out.println("Volumen del juego ajustado al 25%");
			
		}

	}

	@FXML

	private void volumen50() {

		volumen = 0.5;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);

			System.out.println("Volumen del juego ajustado al 50%");
			
		}

	}

	@FXML

	private void volumen75() {

		volumen = 0.75;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);

			System.out.println("Volumen del juego ajustado al 75%");
			
		}

	}

	@FXML

	private void volumen100() {

		volumen = 1.0;

		if (Menu.musicaAmbiente != null) {

			Menu.musicaAmbiente.setVolume(volumen);
			
			System.out.println("Volumen del juego ajustado al máximo");

		}

	}

}