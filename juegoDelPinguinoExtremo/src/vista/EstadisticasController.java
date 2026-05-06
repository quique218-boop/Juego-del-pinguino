package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Usuario;
import controlador.GestorBBDD;

public class EstadisticasController {

    @FXML private VBox panelGlobal;
    @FXML private VBox panelUsuario;

    @FXML private Label mediaLabel;
    @FXML private Label rankingArea;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label resultadoUsuario;

    // 🔘 CAMBIAR VISTAS
   
    
    
    @FXML
    private void mostrarGlobales() {
    	panelGlobal.setManaged(true);
    	panelGlobal.setVisible(true);

    	panelUsuario.setManaged(false);
    	panelUsuario.setVisible(false);

        cargarGlobales();
    }

    @FXML
    private void mostrarUsuario() {
        panelGlobal.setVisible(false);
        panelUsuario.setVisible(true);
    }

    private void cargarGlobales() {
        try {
            double media = GestorBBDD.obtenerMediaPuntuacion();
            mediaLabel.setText(String.valueOf(media));

            rankingArea.setText(GestorBBDD.obtenerRankingTexto());

        } catch (Exception e) {
            mediaLabel.setText("Error");
        }
    }

    // 👤 USUARIO
    @FXML
    private void consultarUsuario() {

        String user = userField.getText();
        String pass = passField.getText();

        Usuario u = new Usuario(user, pass);

        if (!GestorBBDD.validarUsuario(u)) {
            resultadoUsuario.setText("Usuario incorrecto");
            return;
        }

        int id = GestorBBDD.obtenerIdUsuario(u);

        int ganadas = GestorBBDD.partidasGanadas(id);
        int jugadas = GestorBBDD.partidasJugadas(id);
        int record = GestorBBDD.recordUsuario(id);

        resultadoUsuario.setText(
            "Jugadas: " + jugadas + "\nGanadas: " + ganadas + "\nRecord: " + record
        );
    }

    // 🔙 VOLVER
    @FXML
    private void volver(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/Menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}