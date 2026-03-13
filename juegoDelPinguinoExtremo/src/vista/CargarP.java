package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class CargarP {

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

	private void BHome(ActionEvent event) {

		cambiarVentana(event, "/recursos/Menu.fxml");

	}

	@FXML
	private void c1(ActionEvent event) {

		//TODO
		
	}

	@FXML
	private void c2(ActionEvent event) {

		//TODO

	}

	@FXML
	private void c3(ActionEvent event) {

		//TODO

	}

	@FXML
	private void c4(ActionEvent event) {

		//TODO
		
	}

	@FXML
	private void c5(ActionEvent event) {

		//TODO
		
	}

	@FXML
	private void c6(ActionEvent event) {

		//TODO

	}
	
	@FXML
	private void Cancelar(ActionEvent event) {

		cambiarVentana(event, "/recursos/Menu.fxml");

	}
	
	@FXML
	private void Guardar(ActionEvent event) {

		//TODO

	}
	

}
