package vista;

import java.io.IOException;
import java.util.ArrayList;

import modelo.*;
import controlador.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login2 {
	
@FXML
    
    private TextField userField;

    @FXML
    
    private PasswordField passField;

    @FXML
    
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    
    private void onEnter(ActionEvent event) throws IOException {
    	
    	for(int i = 0; i < 2; i++) {
    		
    		Usuario usuario = new Usuario(userField.getText(), passField.getText());
    		
    		if(GestorBBDD.validarUsuario(usuario)) {
    			
    			usuarios.add(usuario);
  	
    		}
    		
    		
    	}


        Parent root = FXMLLoader.load(getClass().getResource("/recursos/Menu.fxml"));
        
        //GestorBBDD.guardarBBDD(null);
        
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        
        window.setTitle("Menú");
        
        window.show();
        
    }
    

}
