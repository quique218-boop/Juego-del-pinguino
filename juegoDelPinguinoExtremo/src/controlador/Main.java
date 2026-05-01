package controlador;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/recursos/Menu.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Login");
		stage.setScene(scene);

		stage.initStyle(StageStyle.UNIFIED);
		
		stage.getIcons().add(new Image("/recursos/pinguino.png"));
		
		stage.show();
    }

    public static void main(String[] args) {
    	
        launch();
        
    }

}