package vista;

import controlador.*;
import javafx.fxml.FXML;
import modelo.Pinguino;
import modelo.*;
public class Play {
	
	private GestorTablero gestorTablero;
	
	@FXML
	private void initialize() {
		
		//TODO
		
		//GestorTablero = new GestorTablero();
	}

	@FXML
	private void lanzar() {

		//TODO
		
	}
	
	@FXML
	private void usarBola() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();
		
		Inventario inventario = pinguino.getInventario();

			if (!inventario.getBolas().isEmpty()) { // Si la lista no es vacia

				jugador.usarItem(inventario.getBolas().get(0)); // El jugador coge la primera bola de nieve de la lista
				
				System.out.println("A que jugador quieres lanzar-le la bola de nieve?");
				
				String el = scan.nextLine();
				
				gestorJugador.getPartida().getArrayList
				
				if(int skill = random.nextInt() + 1) {
					
					if(skill <= 5) {
						
						System.out.println();
					}
				}
			}
		
		
		
		
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
