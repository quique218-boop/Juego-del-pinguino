package Error_capa8;



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
	    	
	    	//TODO
	    	
	    }
	    
	    @FXML
	    private void J2(ActionEvent event) {
	    	
	    	
	    	//TODO
	    }
	    
	    @FXML
	    private void J3(ActionEvent event) {
	    	
	    	
	    	//TODO
	    }
	    
	    @FXML
	    private void J4(ActionEvent event) {
	    	
	    	//TODO
	    	
	    }
	    
	    @FXML
	    private void pnegro(MouseEvent event) {
	        System.out.println("Negro");
	        
	      //TODO
	    }

	    @FXML
	    private void pmorado(MouseEvent event) {
	        System.out.println("Morado");
	        
	      //TODO
	    }

	    @FXML
	    private void pamarillo(MouseEvent event) {
	        System.out.println("Amarillo");
	        
	      //TODO
	    }

	    @FXML
	    private void prosa(MouseEvent event) {
	        System.out.println("Rosa");
	        
	      //TODO
	    }

	    @FXML
	    private void pespecial(MouseEvent event) {
	        System.out.println("Especial");
	        
	      //TODO
	    }

	    @FXML
	    private void f1(MouseEvent event) {
	        System.out.println("Foca 1");
	        
	      //TODO
	    }

	    @FXML
	    private void f2(MouseEvent event) {
	        System.out.println("Foca 2");
	        
	      //TODO
	    }

	    @FXML
	    private void f3(MouseEvent event) {
	        System.out.println("Foca 3");
	        
	      //TODO
	    }

	    @FXML
	    private void f4(MouseEvent event) {
	        System.out.println("Foca 4");
	        
	      //TODO
	    }
	}

