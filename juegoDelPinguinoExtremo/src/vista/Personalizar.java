package vista;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Personalizar {

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
	    private void BMenu(ActionEvent event) {
	    
	    cambiarVentana(event, "/recursos/Menu.fxml");

	    }
	    
	    @FXML
	    private void J1(ActionEvent event) {
	    	
	    	
	    	
	    }
	    
	    @FXML
	    private void J2(ActionEvent event) {
	    	
	    	
	    	
	    }
	    
	    @FXML
	    private void J3(ActionEvent event) {
	    	
	    	
	    	
	    }
	    
	    @FXML
	    private void J4(ActionEvent event) {
	    	
	    	
	    	
	    }
	    
	    @FXML
	    private void pnegro(MouseEvent event) {
	        System.out.println("Negro");
	    }

	    @FXML
	    private void pmorado(MouseEvent event) {
	        System.out.println("Morado");
	    }

	    @FXML
	    private void pamarillo(MouseEvent event) {
	        System.out.println("Amarillo");
	    }

	    @FXML
	    private void prosa(MouseEvent event) {
	        System.out.println("Rosa");
	    }

	    @FXML
	    private void pespecial(MouseEvent event) {
	        System.out.println("Especial");
	    }

	    @FXML
	    private void f1(MouseEvent event) {
	        System.out.println("Foca 1");
	    }

	    @FXML
	    private void f2(MouseEvent event) {
	        System.out.println("Foca 2");
	    }

	    @FXML
	    private void f3(MouseEvent event) {
	        System.out.println("Foca 3");
	    }

	    @FXML
	    private void f4(MouseEvent event) {
	        System.out.println("Foca 4");
	    }
	}

