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
import javafx.stage.Stage;
import controlador.GestorBBDD;

public class Slots {
	
	@FXML private Button slot1;
	@FXML private Button slot2;
	@FXML private Button slot3;
	@FXML private Button slot4;
	@FXML private Button slot5;
	@FXML private Button slot6;
	@FXML private Button slot7;
	@FXML private Button slot8;
	
	@FXML
	public void initialize() {
	    actualizarSlots();
	}

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

        int slot = Integer.parseInt(btn.getId().replace("slot", ""));

        if (modo == Modo.CARGAR) {
            cargar(slot);
        } else {
            guardar(slot);
        }
    }

    private void guardar(int slot) {
        try {

            GestorBBDD.guardar(tableroActual, slot);

            actualizarSlots();

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
    
    private void actualizarSlots() {

        Button[] botones = {
            slot1, slot2, slot3, slot4,
            slot5, slot6, slot7, slot8
        };

        for (int i = 0; i < botones.length; i++) {

            int numSlot = i + 1;

            if (GestorBBDD.existeSlot(numSlot)) {

                String info =
                        GestorBBDD.obtenerInfoSlot(numSlot);

                botones[i].setText(info);

            } else {

                botones[i].setText("Slot " + numSlot);
            }

            botones[i].setWrapText(true);
        }
    }
}
