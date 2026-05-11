package vista;

import controlador.GestorBBDD;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Usuario;

import java.io.IOException;
import java.util.ArrayList;

public class LoginMultipleController {

	 @FXML private TextField userField;
	    @FXML private PasswordField passField;
	    @FXML private Label textoJugador;
	    
	    private GestorBBDD gestorBBDD;

	    private int totalJugadores;
	    private int actual = 0;

	    private ArrayList<Usuario> usuarios = new ArrayList<>();

	    public void setNumeroJugadores(int n) {
	        this.totalJugadores = n;
	    }

	    @FXML
	    public void initialize() {
	    	gestorBBDD = new GestorBBDD();
	        actualizarTexto();
	    }

	    @FXML
	    private void onEnter(ActionEvent event) {

	        try {
	            String user = userField.getText().trim();
	            String pass = passField.getText().trim();

	            Usuario u = new Usuario(user);

	            if (!gestorBBDD.validarUsuario(u, pass)) {
	                textoJugador.setText("Usuario incorrecto");
	                return;
	            }

	            if (usuarios.stream().anyMatch(x -> x.getNombre().equals(user))) {
	                textoJugador.setText("Ese usuario ya está en la partida");
	                return;
	            }

	            usuarios.add(u);
	            actual++;

	            if (actual < totalJugadores) {
	                userField.clear();
	                passField.clear();
	                actualizarTexto();
	            } else {
	                irAlJuego(event);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private void actualizarTexto() {
	        textoJugador.setText("Jugador " + (actual + 1));
	    }

	    private void irAlJuego(ActionEvent event) throws IOException {
	    	
	    	

	       
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/PantallaJuego.fxml"));
	        Parent root = loader.load();
	        
	        PantallaJuego controller = loader.getController();
	        controller.setUsuarios(usuarios);
	        
	        controller.inicio(null);


	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Juego");
	        stage.show();
	    }

	    @FXML
	    private void crearUsuario(ActionEvent event) {
	        try {
	        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/recursos/crearUsuario.fxml"));
	        	Parent root = loader.load();

	        	CrearUsuarioController controller = loader.getController();
	        	controller.setContextoLogin(totalJugadores, actual, usuarios);

	            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            stage.setScene(new Scene(root));

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void setEstado(int actual, ArrayList<Usuario> usuarios) {
	        this.actual = actual;
	        this.usuarios = usuarios;
	        actualizarTexto();
	    }
}
