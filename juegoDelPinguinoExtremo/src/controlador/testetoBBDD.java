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
		 
		 GestorBBDD.guardar(tableta.getPartida(), 1);
		 
		/*Tablero tablero = bases.cargarTablero(1);
		
		System.out.println(tablero.getFecha());
		
		System.out.println(tablero.getTurnos());
		
		System.out.println(tablero.getArrayListJugador().toString());
		
		for (int i = 0; i < tablero.getArrayListCasilla().size(); i++) {
			
			System.out.println(tablero.getArrayListCasilla().get(i));
			
			if(i % 5 == 0 && i != 0) {
				
				System.out.println();
				
			}
		}*/
	
	}
}
