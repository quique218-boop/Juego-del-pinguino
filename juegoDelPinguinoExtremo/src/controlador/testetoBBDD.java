package controlador;

import modelo.*;

public class testetoBBDD {
	
	public static void main(String[] args) {
		
		
		
		Tablero tablero = GestorBBDD.cargarTablero(8);
		
		System.out.println(tablero.getTurnos());

	}
}
