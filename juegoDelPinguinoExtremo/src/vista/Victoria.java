package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.Jugador;

public class Victoria {

	@FXML
	private Text tituloVictoria;
	@FXML
	private Button volver;

	private Jugador ganador;

	public void setGanador(Jugador ganador) {
		this.ganador = ganador;
	}

	public void inicio() {
		tituloVictoria.setText("Victoria de " + ganador.getNombre());
	}

	@FXML
	private void volver() {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/Menu.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) tituloVictoria.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
