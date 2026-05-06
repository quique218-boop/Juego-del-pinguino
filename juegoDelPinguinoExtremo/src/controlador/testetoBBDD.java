package controlador;

import modelo.*;

public class testetoBBDD {
	
	public static void main(String[] args) {
		
		Tablero t = GestorBBDD.cargarTablero(1);
		
		System.out.println(t.getFecha());
		System.out.println(t.getJugadorActual());
		System.out.println(t.getJugador(2));
		System.out.println(t.getTurnos());

	}
}
