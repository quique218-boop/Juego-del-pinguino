package vista;


import modelo.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controlador.GestorBBDD;

public class Slots {

    public enum Modo {
        GUARDAR,
        CARGAR
    }
    
    private Scene escenaAnterior;
    
    public void setEscenaAnterior(Scene escena) {
        this.escenaAnterior = escena;
    }

    private Modo modo;
    private Tablero tableroActual;
    

    @FXML private Label mensaje;
    

    public void setModo(Boolean estado) {
        if(estado) {
        	this.modo = Modo.CARGAR;
        }
        else {
        	
        	this.modo = Modo.GUARDAR;
        }
    }
    
   

    public void setPartida(Tablero tablero) {
        this.tableroActual = tablero;
    }

    // 🎯 CLICK EN SLOT
    @FXML
    private void handleSlot(ActionEvent event) {

        Button btn = (Button) event.getSource();
        int slot = Integer.parseInt(btn.getText().split(" ")[1]);

        if (modo == Modo.CARGAR) {
            cargar(slot);
        } else {
            guardar(slot);
        }
    }

    private void guardar(int slot) {
        try {
            GestorBBDD.guardar(tableroActual, slot);
            mensaje.setText("Partida guardada en slot " + slot);
        } catch (Exception e) {
            mensaje.setText("Error al guardar");
            e.printStackTrace();
        }
    }

    private void cargar(int slot) {
        try {
            if (!GestorBBDD.existeSlot(slot)) {
                mensaje.setText("No hay partida en este slot");
                return;
            }

            Tablero p = GestorBBDD.cargarTablero(slot);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/PantallaJuego.fxml"));
	        Parent root = loader.load();
	        
	        PantallaJuego controller = loader.getController();
	        
	        controller.inicio(p);


	        Stage stage = (Stage) mensaje.getScene().getWindow();	        
	        stage.setScene(new Scene(root));
	        stage.setTitle("Juego");
	        stage.show();;

        } catch (Exception e) {
            mensaje.setText("Error al cargar");
            e.printStackTrace();
        }
    }

    @FXML
    private void volver(ActionEvent event) throws Exception {

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        if (escenaAnterior != null) {

            stage.setScene(escenaAnterior);

        } else {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/recursos/Menu.fxml"));

            stage.setScene(new Scene(root));
        }

        stage.show();
    }
}
