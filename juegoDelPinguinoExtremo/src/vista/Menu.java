package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Menu {

    private void cambiarVentana(ActionEvent event, String fxml) {
    	
        try {
        	
            Parent root = FXMLLoader.load(getClass().getResource(fxml));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            
            stage.show();

        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
        
    }

    @FXML
    
    private void nuevaPartida(ActionEvent event) {
    	
        cambiarVentana(event, "New_Game.fxml");
        
    }

    @FXML
    
    private void cargarPartida(ActionEvent event) {
    	
        cambiarVentana(event, "CargarP.fxml");
        
    }

    @FXML
    
    private void Personalizar(ActionEvent event) {
    	
        cambiarVentana(event, "Personalizacion.fxml");
        
    }

    @FXML
    
    private void Configuracion(ActionEvent event) {
    	
        cambiarVentana(event, "Configuracion.fxml");
        
    }

    @FXML
    
    private void Salir() {
    	
        System.exit(0);
        
    }
    
}