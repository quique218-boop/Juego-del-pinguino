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
    
    private GestorBBDD gestorBBDD;
    
    @FXML
    private void initialize() {
    	gestorBBDD = new GestorBBDD();
    }
    
    
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


    private void cargarGlobales() {

        try {

            double media = gestorBBDD.obtenerMediaPuntuacion();
            mediaLabel.setText(String.format("%.2f", media));

            int record = gestorBBDD.obtenerRecordGlobal();
            recordGlobalLabel.setText(String.valueOf(record));

            jugadoresRecordLabel.setText(
                gestorBBDD.obtenerJugadoresRecord()
            );
            
            mediaGanadasLabel.setText(
            	    String.valueOf(
            	        gestorBBDD.mediaPartidasGanadas()
            	    )
            	);

            superioresMediaLabel.setText(
                gestorBBDD.obtenerJugadoresSuperiorMedia()
            );

            rankingArea.setText(
                gestorBBDD.obtenerRankingTexto()
            );

        } catch (Exception e) {

            mediaLabel.setText("Error");
            e.printStackTrace();
        }
    }

 

    @FXML
    private void consultarUsuario() {

        try {

            String user = userField.getText();
            String pass = passField.getText();

            Usuario u = new Usuario(user);

            if (!gestorBBDD.validarUsuario(u, pass)) {

                resultadoUsuario.setText(
                    "❌ Usuario incorrecto"
                );

                return;
            }

            int id = gestorBBDD.obtenerIdUsuario(u);

            int ganadas = gestorBBDD.partidasGanadas(id);
            int jugadas = gestorBBDD.partidasJugadas(id);
            int record = gestorBBDD.recordUsuario(id);

            int posicion = gestorBBDD.obtenerPosicionRanking(id);

            double porcentaje =
                gestorBBDD.obtenerPorcentajeInferior(ganadas);

            resultadoUsuario.setText(

                "👤 Usuario: " + user +

                "\n\n🎮 Partidas jugadas: " + jugadas +

                "\n🏆 Partidas ganadas: " + ganadas +

                "\n⭐ Récord usuario: " + record + " Pts" +

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