package juegoDelPinguinoExtremo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
public class GuardarP {
	
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
		private void s1(ActionEvent event) {


		}
		
		@FXML
		private void s2(ActionEvent event) {


		}
		
		@FXML
		private void s3(ActionEvent event) {


		}
		
		@FXML
		private void s4(ActionEvent event) {


		}
		
		@FXML
		private void s5(ActionEvent event) {


		}
		
		@FXML
		private void s6(ActionEvent event) {


		}
		
		@FXML
		private void Cancelar(ActionEvent event) {

			cambiarVentana(event, "tablero.fxml");

		}
		@FXML
		private void Guardar(ActionEvent event) {


		}
		
	}