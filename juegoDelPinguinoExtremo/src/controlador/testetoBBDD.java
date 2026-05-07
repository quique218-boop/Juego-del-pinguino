package controlador;

import modelo.*;

public class testetoBBDD {
	
	public static void main(String[] args) {
		
		
		
		Usuario usuario = new Usuario("34", "34");
		
		System.out.println(GestorBBDD.obtenerJugadoresRecord());

	}
}
