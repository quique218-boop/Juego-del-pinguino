package juegoDelPinguinoExtremo;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private TextField userField;

	@FXML
	private PasswordField passField;

	@FXML
	private void onEnter(ActionEvent event) throws IOException {

		System.out.println("Usuario: " + userField.getText());
		System.out.println("Password: " + passField.getText());

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("test.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		// This line gets the stage information

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.setTitle("Partida");
		window.show();
		
		GestorBBDD.cargarTablero(Integer.parseInt(userField.getText()));
		
		
	}
}