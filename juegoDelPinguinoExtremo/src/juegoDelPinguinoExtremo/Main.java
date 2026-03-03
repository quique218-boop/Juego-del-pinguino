package juegoDelPinguinoExtremo;

import java.awt.TextField;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		
		
		Label l = new Label("LOGIN");

	    PasswordField passwordfield = new PasswordField();//fx:id="passField" promptText="Password"

	    Button button = new Button("ENTER");
	    
	    StackPane stack = new StackPane();
	    
	    stack.getChildren().add(l);
	    stack.getChildren().add(passwordfield);
	    stack.getChildren().add(button);
	    
	    stack.setAlignment(stack, null);
	    

	    Scene scene = new Scene(stack, 640, 480);

		stage.setScene(scene);
		
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}