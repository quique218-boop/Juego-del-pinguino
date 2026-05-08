package vista;

import javafx.event.ActionEvent;
import javafx.scene.Node;import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class seleccionJugador {
	
    private void cambiar(ActionEvent event, int numJugadores) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/login.fxml"));
        Parent root = loader.load();

        LoginMultipleController controller = loader.getController();

        controller.setNumeroJugadores(numJugadores);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void jugar2(ActionEvent event) throws IOException {
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
