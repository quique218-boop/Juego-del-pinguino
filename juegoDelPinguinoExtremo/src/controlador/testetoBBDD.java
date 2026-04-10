package controlador;

import modelo.*;

public class testetoBBDD {
	
	public static void main(String[] args) {
		
		 GestorBBDD bases = new GestorBBDD();
		 
		 Tablero tabla;
		 
		 GestorTablero tableta = new GestorTablero();
		 
		 tableta.NuevoTablero();
		 
		 Pinguino a = new Pinguino();
		 Pinguino b = new Pinguino();
		 Pinguino c = new Pinguino();
		
		 tableta.añadirJugador(a);
		 tableta.añadirJugador(b);
		 tableta.añadirJugador(c);
		 
		 bases.guardar(tableta.getPartida());
		 
		Tablero tablero = bases.cargarTablero(11);
		
		System.out.println(tablero.getFecha());
		
		System.out.println(tablero.getTurnos());
		
		System.out.println(tablero.getArrayListJugador().toString());


		
		
	}

}
