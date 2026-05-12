package vista;

import javafx.event.ActionEvent;
import javafx.scene.Node;import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class seleccionJugador {
	
	  /*
     * Método auxiliar que cambia a la pantalla de login.
     * Recibimos el número de jugadores seleccionados y  lo pasamos al controlador del login.
     */
	
    private void cambiar(ActionEvent event, int numJugadores) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/login.fxml")); //Cargamos el archivo FXML
        Parent root = loader.load(); 

        LoginMultipleController controller = loader.getController(); //Obtenemos el controlador de la pantalla de login

        controller.setNumeroJugadores(numJugadores); //Envía a login el número de jugadores que deben iniciar sesión

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Obtenemos la escena actual desde el botón pulsado
        stage.setScene(new Scene(root)); //cambiamos la escena por la pantalla de login
        stage.show(); //Mostramos la ventana
    }


    @FXML
    private void jugar2(ActionEvent event) throws IOException { //Metodo que se ejecuta al elegir 2 jugadores
        cambiar(event, 2);
    }

    @FXML
    private void jugar3(ActionEvent event) throws IOException {
        cambiar(event, 3);
    }

    @FXML
    private void jugar4(ActionEvent event) throws IOException {
        cambiar(event, 4);
    }

}
