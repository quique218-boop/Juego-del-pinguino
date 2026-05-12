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
    private void mostrarGlobales() { //Mostramos el panel de estadísticas globales y oculta el panel del usuario

        panelGlobal.setManaged(true);
        panelGlobal.setVisible(true);

        panelUsuario.setManaged(false);
        panelUsuario.setVisible(false);

        cargarGlobales();
    }

    @FXML
    private void mostrarUsuario() { //Muestra el panel de estadísticas de usuario y oculta el panel global.

        panelUsuario.setManaged(true);
        panelUsuario.setVisible(true);

        panelGlobal.setManaged(false);
        panelGlobal.setVisible(false);
    }


    private void cargarGlobales() { //Cargamos todas las estadísticas globales desde la BBDD

        try {

            double media = gestorBBDD.obtenerMediaPuntuacion(); //Obtiene y muestra la media de puntuacion
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

        } catch (Exception e) { //En caso de que hubiera un error se muestra por pantalla y por consola

            mediaLabel.setText("Error");
            e.printStackTrace();
        }
    }

 

    @FXML
    private void consultarUsuario() { //Consultamos las estadisticas de un usuario en concreto

        try {

        	//Obtenemos el nombre y la contraseña escritos por el usuario.
            String user = userField.getText();
            String pass = passField.getText();

            Usuario u = new Usuario(user); //Creamos un objeto Usuario con el nombre introducido

            if (!gestorBBDD.validarUsuario(u, pass)) { //Comprobamos si el usuario y la contraseña son correctas

                resultadoUsuario.setText(
                    "❌ Usuario incorrecto"
                );

                return;
            }

            int id = gestorBBDD.obtenerIdUsuario(u); //Obtenemos el ID para poder consultar sus estadisticas

            int ganadas = gestorBBDD.partidasGanadas(id); 
            int jugadas = gestorBBDD.partidasJugadas(id);
            int record = gestorBBDD.recordUsuario(id);

            int posicion = gestorBBDD.obtenerPosicionRanking(id);

            double porcentaje =
                gestorBBDD.obtenerPorcentajeInferior(ganadas); //Calculamos el porcentaje que están por debajo de sus victorias

            resultadoUsuario.setText( //Muestra todas las estadísticas del usuario

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
    private void volver(ActionEvent event) { //Carganis el mení cpn el archivo Menu.fxml

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