package Error_capa8;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class New_Game {

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
	    private void dosf(ActionEvent event) {
	    
	    cambiarVentana(event, "/recursos/Id4j.fxml");
	    
	    }
	    
	    @FXML
	    private void tresf(ActionEvent event) {
	    
	    cambiarVentana(event, "/recursos/Id4j.fxml");
	    
	    }
	    
	    @FXML
	    private void cuatrof(ActionEvent event) {
	    
	    cambiarVentana(event, "/recursos/Id4j.fxml");

	    }
	    
	    @FXML
	    private void Ret(ActionEvent event) {
	    	
	    	cambiarVentana(event, "/recursos/Menu.fxml");
	    	
	    }
}

//TODO