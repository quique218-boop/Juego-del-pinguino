package vista;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Id4j {

	private void cambiarVentana(ActionEvent event, String fxml) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.setScene(new Scene(root));

			stage.show();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@FXML private TextField jugador1;

	@FXML private TextField jugador2;

	@FXML private TextField jugador3;

	@FXML private TextField jugador4;

	@FXML

	private void Start(ActionEvent event) {

		cambiarVentana(event, "/recursos/tablero.fxml");

	}

	@FXML

	private void Return(ActionEvent event) {

		cambiarVentana(event, "/recursos/New_Game.fxml");

	}

}