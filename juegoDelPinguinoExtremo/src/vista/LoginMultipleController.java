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

	    public void setNumeroJugadores(int n) { //Guarda el n. total de jugadores que deben iniciar sesión
	        this.totalJugadores = n;
	    }

	    /*
		 * Método que se ejecuta automáticamente al cargar la vista.
		 * Inicializa el gestor de base de datos y actualiza el texto del jugador.
		 */
	    
	    @FXML
	    public void initialize() {
	    	gestorBBDD = new GestorBBDD();
	        actualizarTexto();
	    }

	    @FXML
	    private void onEnter(ActionEvent event) { //Se ejecutara al pulsar enter o el boton de login

	        try { 
	        	//Obtenemos el usuario y la contraseña escritos
	            String user = userField.getText().trim();
	            String pass = passField.getText().trim();
 
	            Usuario u = new Usuario(user); //Creamos objeto usuario

	            if (!gestorBBDD.validarUsuario(u, pass)) { //Comprobamos si son correctos
	                textoJugador.setText("Usuario incorrecto");
	                return;
	            }

	            if (usuarios.stream().anyMatch(x -> x.getNombre().equals(user))) { //Comprobamos que el mismo usuario no entre dos veces en la misma partida
	                textoJugador.setText("Ese usuario ya está en la partida");
	                return;
	            }

	            usuarios.add(u); //Añadimos el usuario validado a la lista de jugadores
	            actual++;


		        /*
		         * Si todavía faltan jugadores por iniciar sesión,
		         * limpiamos los campos y actualizamos el texto.
		         */
	            
	            if (actual < totalJugadores) {
	                userField.clear();
	                passField.clear();
	                actualizarTexto();
	            } else {
	                irAlJuego(event); //Si ya han iniciado sesión todos, entran al juego
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private void actualizarTexto() {
	        textoJugador.setText("Jugador " + (actual + 1)); //Actualizamos texto para mostrar quien inicia sesión
	    }

	    private void irAlJuego(ActionEvent event) throws IOException { //Carganis oestala de juego
	    	
	    	

	       
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

	    /*
		 * Carga la pantalla de creación de usuario.
		 * Además, le pasa el estado actual del login para no perder el progreso.
		 */
	    
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
	    //Restauramos el estado del login al volver desde la pantalla de crear usuario.
	    public void setEstado(int actual, ArrayList<Usuario> usuarios) {
	        this.actual = actual;
	        this.usuarios = usuarios;
	        actualizarTexto();
	    }
}
