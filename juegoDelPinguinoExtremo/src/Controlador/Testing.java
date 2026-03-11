package juegoDelPinguinoExtremo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Testing extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Login");
		stage.setScene(scene);

		
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}