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
	
	GestorBBDD gestorBBDD;
	
	@FXML
	public void initialize() {
		gestorBBDD = new GestorBBDD();
	    actualizarSlots();
	}

    public enum Modo {
        GUARDAR,
        CARGAR
    }
    
    private Scene escenaAnterior;
    
    public void setEscenaAnterior(Scene escena) { //Recibe y guarda la escena anterior
        this.escenaAnterior = escena;
    }

    private Modo modo; //Modo actual de la pantalla para ver si guarda o carga
    private Tablero tableroActual; //tablero que se guardara si el modo es guardar
    

    @FXML private Label mensaje;
    
    /*
     * Definimos en que modo de la pantalla se encuentra.
     * Si su estado es true, se usara para cargar.
     * Si su estado es false, se usara para guardar.
     */
    
    public void setModo(Boolean estado) {
        if(estado) {
        	this.modo = Modo.CARGAR;
        }
        else {
        	
        	this.modo = Modo.GUARDAR;
        }
    }
    
   

    public void setPartida(Tablero tablero) { //Se recibe el tablero actual para poder guardarlo en un slot
        this.tableroActual = tablero;
    }

    // 🎯 CLICK EN SLOT
    @FXML
    private void handleSlot(ActionEvent event) {

        Button btn = (Button) event.getSource(); //Obtiene el boton que ha estado pulsado

        int slot = Integer.parseInt(btn.getId().replace("slot", ""));

        if (modo == Modo.CARGAR) { //Segun el modo en el que se encuentre cargara o guardara
            cargar(slot);
        } else {
            guardar(slot);
        }
    }

    private void guardar(int slot) {
        try {

            gestorBBDD.guardar(tableroActual, slot); //Guardamos el tablero actual en la BBDD

            actualizarSlots(); //Actualizamos los botones para mostrar la nueva información del slot.

            mensaje.setText("Partida guardada en slot " + slot); //Mostramos un mensaje de confirmación

        } catch (Exception e) {

            mensaje.setText("Error al guardar");
            e.printStackTrace();
        }
    }

    private void cargar(int slot) {
        try {
            if (!gestorBBDD.existeSlot(slot)) { //Comprobamos que exista una partida guardada en ese slot
                mensaje.setText("No hay partida en este slot");
                return;
            }

            Tablero p = gestorBBDD.cargarTablero(slot); //Cargamos el tablero guardado en ese slot

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/PantallaJuego.fxml")); //Cargamos la pantalla de juego
	        Parent root = loader.load();
	        
	        PantallaJuego controller = loader.getController(); //Obtenemos el controlador de la pantalla de juego
	        
	        controller.inicio(p); //Inicia la pantalla de juego con la partida cargada


	        Stage stage = (Stage) mensaje.getScene().getWindow();	 //Obtenemos la ventana actual        
	        stage.setScene(new Scene(root)); //Cambia la escena actual por la pantalla
	        stage.setTitle("Juego"); //Cambiamos el titulo
	        stage.show();; //Mostramos la ventana actualizada

        } catch (Exception e) {
            mensaje.setText("Error al cargar");
            e.printStackTrace();
        }
    }

    @FXML
    private void volver(ActionEvent event) throws Exception { //Volvemos a la escena anterior si existe y si no al menú principal

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
    
    private void actualizarSlots() {//Actualizamos los textos de los botones por si tienen una partida o no

        Button[] botones = {
            slot1, slot2, slot3, slot4,
            slot5, slot6, slot7, slot8
        };

        for (int i = 0; i < botones.length; i++) { //Recorremos todos los botones

            int numSlot = i + 1; //El slot empieza en 1

            if (gestorBBDD.existeSlot(numSlot)) { //Si existe partida en ese slot muestra la información

                String info =
                        gestorBBDD.obtenerInfoSlot(numSlot);

                botones[i].setText(info);

            } else { //Si no hay partidas entonces muestra el nombre del slot

                botones[i].setText("Slot " + numSlot);
            }

            botones[i].setWrapText(true);
        }
    }
}
