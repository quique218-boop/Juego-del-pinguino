package vista;

import controlador.*;
import modelo.*;
import javafx.fxml.FXML;


public class Play {
	
	private GestorTablero gestorTablero;
	
	@FXML
	private void initialize() {
		
		this.gestorTablero = new GestorTablero();
	}

	@FXML
	private void lanzar() {
		
		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();
		Dado dado = (Dado) pinguino.getInventario().getDado().getFirst();
		
		int resultado = gestorTablero.tirarDado(pinguino, dado);
		
	}
	
	@FXML
	private void usarBola() {

		//TODO
		
	}
	
	@FXML
	private void usarPez() {

		//TODO
		
	}

	@FXML
	private void usarDadoR() {

		//TODO
		
	}

	@FXML
	private void usarDadoL() {
		
		//TODO

	}

}
