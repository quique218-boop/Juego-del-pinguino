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
	
	@FXML private TextField userField; //Campo donde los usuarios escriben su nombre
    @FXML private PasswordField passField; //Campo donde los usuarios escriben su contraseña
    @FXML private PasswordField confirmField; //Campo donde los usuarios vuelven a escribir su contraseña para confirmar
    @FXML private Label mensajeError; //Label donde se muestra mensaje de error o confirmacion
    
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
    private void crearUsuario(ActionEvent event) { //El usuario rellena cada Field para rellenar y que el usuario pueda crear si Usuario

        String user = userField.getText();
        String pass = passField.getText();
        String confirm = confirmField.getText();
        Usuario usuario = new Usuario(user);

        if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) { //Si algun espacio esta vacio
            mensajeError.setText("Rellena todos los campos");
            return;
        }

        if (!pass.equals(confirm)) { //Si las contraseñas no coinciden
            mensajeError.setText("Las contraseñas no coinciden");
            return;
        }

        if (gestorBBDD.validarUsuario(usuario, pass)) { //Si el nombre de usuario ya existiera
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

    	//Cargamos el archivo FXML de la pantalla de login
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/login.fxml"));
    	Parent root = loader.load();

    	//Obtenemos el controlador de login para pasar todos los datos necessarios
    	
    	LoginMultipleController controller = loader.getController();
    	
    	//Devolvemos al login el num. de jugadores y el estado actual
    	
    	controller.setNumeroJugadores(totalJugadores);
    	controller.setEstado(actual, usuarios);
    	
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Obtenemos la ventana actual desde el evento del botón
        stage.setScene(new Scene(root)); //Cambiamos la escena actual por la del login
        stage.setTitle("Login"); //Cambiamos el titulo de la ventana
        stage.show(); //Mostramos la ventana actualizada
    }

}
