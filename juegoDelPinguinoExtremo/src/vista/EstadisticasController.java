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

    // PANELES
    @FXML private VBox panelGlobal;
    @FXML private VBox panelUsuario;

    // GLOBALES
    @FXML private Label mediaLabel;
    @FXML private Label rankingArea;

    @FXML private Label recordGlobalLabel;
    @FXML private Label jugadoresRecordLabel;
    @FXML private Label superioresMediaLabel;
    @FXML private Label mediaGanadasLabel;

    // USUARIO
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private TextArea resultadoUsuario;
    

    // =========================
    // CAMBIO DE VISTAS
    // =========================

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

        panelUsuario.setManaged(true);
        panelUsuario.setVisible(true);

        panelGlobal.setManaged(false);
        panelGlobal.setVisible(false);
    }

    // =========================
    // ESTADÍSTICAS GLOBALES
    // =========================

    private void cargarGlobales() {

        try {

            // MEDIA
            double media = GestorBBDD.obtenerMediaPuntuacion();
            mediaLabel.setText(String.format("%.2f", media));

            // RECORD GLOBAL
            int record = GestorBBDD.obtenerRecordGlobal();
            recordGlobalLabel.setText(String.valueOf(record));

            // JUGADORES RECORD
            jugadoresRecordLabel.setText(
                GestorBBDD.obtenerJugadoresRecord()
            );
            
            mediaGanadasLabel.setText(
            	    String.valueOf(
            	        GestorBBDD.mediaPartidasGanadas()
            	    )
            	);

            // JUGADORES SOBRE MEDIA
            superioresMediaLabel.setText(
                GestorBBDD.obtenerJugadoresSuperiorMedia()
            );

            // RANKING
            rankingArea.setText(
                GestorBBDD.obtenerRankingTexto()
            );

        } catch (Exception e) {

            mediaLabel.setText("Error");
            e.printStackTrace();
        }
    }

    // =========================
    // ESTADÍSTICAS USUARIO
    // =========================

    @FXML
    private void consultarUsuario() {

        try {

            String user = userField.getText();
            String pass = passField.getText();

            Usuario u = new Usuario(user, pass);

            // VALIDAR
            if (!GestorBBDD.validarUsuario(u)) {

                resultadoUsuario.setText(
                    "❌ Usuario incorrecto"
                );

                return;
            }

            int id = GestorBBDD.obtenerIdUsuario(u);

            // DATOS
            int ganadas = GestorBBDD.partidasGanadas(id);
            int jugadas = GestorBBDD.partidasJugadas(id);
            int record = GestorBBDD.recordUsuario(id);

            // NUEVO
            int posicion = GestorBBDD.obtenerPosicionRanking(id);

            double porcentaje =
                GestorBBDD.obtenerPorcentajeInferior(ganadas);

            // MOSTRAR
            resultadoUsuario.setText(

                "👤 Usuario: " + user +

                "\n\n🎮 Partidas jugadas: " + jugadas +

                "\n🏆 Partidas ganadas: " + ganadas +

                "\n⭐ Récord usuario: " + record +

                "\n📈 Posición ranking: #" + posicion +

                "\n📊 Mejor que el "
                + String.format("%.2f", porcentaje)
                + "% de jugadores"

            );

        } catch (Exception e) {

            resultadoUsuario.setText(
                "Error al consultar usuario"
            );

            e.printStackTrace();
        }
    }

    // =========================
    // VOLVER
    // =========================

    @FXML
    private void volver(ActionEvent event) {

        try {

            FXMLLoader loader =
                new FXMLLoader(
                    getClass().getResource(
                        "/recursos/Menu.fxml"
                    )
                );

            Parent root = loader.load();

            Stage stage = (Stage)
                ((Node) event.getSource())
                .getScene()
                .getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}