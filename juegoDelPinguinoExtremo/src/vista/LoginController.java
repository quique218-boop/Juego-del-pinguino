package vista;

import controlador.*;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    
    private TextField userField;

    @FXML
    
    private PasswordField passField;

    @FXML
    
    private void onEnter(ActionEvent event) throws IOException {

        System.out.println("Usuario: " + userField.getText());
        
        System.out.println("Password: " + passField.getText());

        Parent root = FXMLLoader.load(getClass().getResource("/recursos/Menu.fxml"));
        
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        
        window.setTitle("Menú");
        
        window.show();
        
    }
    
}