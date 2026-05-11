package vista;


import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import controlador.GestorBBDD;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Usuario;
import javafx.scene.Node;

public class CrearUsuarioController {
	
	@FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmField;
    @FXML private Label mensajeError;
    
    private GestorBBDD gestorBBDD;
    
    private int totalJugadores;
    private int actual;
    private ArrayList<Usuario> usuarios;

    @FXML
    private void initialize() {
    	gestorBBDD = new GestorBBDD();
    }
    
    public void setContextoLogin(int total, int act, ArrayList<Usuario> users) {
        this.totalJugadores = total;
        this.actual = act;
        this.usuarios = users;
    }

    @FXML
    private void crearUsuario(ActionEvent event) {

        String user = userField.getText();
        String pass = passField.getText();
        String confirm = confirmField.getText();
        Usuario usuario = new Usuario(user);

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            mensajeError.setText("Rellena todos los campos");
            return;
        }

        if (!pass.equals(confirm)) {
            mensajeError.setText("Las contraseñas no coinciden");
            return;
        }

        if (gestorBBDD.validarUsuario(usuario, pass)) {
            mensajeError.setText("El usuario ya existe");
            return;
        }

        Usuario u = new Usuario(user);

        if (gestorBBDD.crearUsuario(u, pass)) {
            mensajeError.setText("Usuario creado correctamente");
        } else {
            mensajeError.setText("Error al crear usuario");
        }
    }

    @FXML
    private void volverLogin(ActionEvent event) throws IOException {

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/login.fxml"));
    	Parent root = loader.load();

    	LoginMultipleController controller = loader.getController();
    	controller.setNumeroJugadores(totalJugadores);
    	controller.setEstado(actual, usuarios);
    	
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

}
