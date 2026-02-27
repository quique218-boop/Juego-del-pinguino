package application;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private void onEnter() {

        System.out.println("Usuario: " + userField.getText());
        System.out.println("Password: " + passField.getText());
    }
}