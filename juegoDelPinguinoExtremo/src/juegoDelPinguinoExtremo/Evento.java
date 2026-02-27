package juegoDelPinguinoExtremo;

import java.util.Random;

public class Evento extends Casilla {

	String[] eventos = { "Pez", "BNeu", "Dado"};

	public Evento() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		Random ran = new Random();

		switch (eventos[ran.nextInt(eventos.length)]) {
		
		case "Pez": {
			// dar pez
		}

		case "BNeu": {
			//bolas de nieve += ran.nextInt(3) + 1;
		}

		case "Dado": {
			
			int valor = ran.nextInt(11);
			
			if(valor < 8) {
				
				// Dado rapido
				
			} else {
				
				// dado lento
				
			}
			
		}
		
		default:
			System.out.println("Caso no valido");
		}
	}

	public void setEventos(String[] eventos) {

		this.eventos = eventos;

	}

	public String[] getEventos() {

		return this.eventos;

	}

}
