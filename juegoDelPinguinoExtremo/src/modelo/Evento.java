package modelo;

import java.util.Random;

public class Evento extends Casilla {

	String[] eventos = { "Pez", "BNeu", "Dado", "Motos" };

	public Evento() {
		
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {


		Random rand = new Random();

		switch (eventos[rand.nextInt(eventos.length)]) {

		case "Pez": {

			jugador.anadirItem(new Pez());

			break;
		}

		case "BNeu": {

			int totalBolas = rand.nextInt(3) + 1;

			for (int i = 0; i < totalBolas; i++) {
				
				jugador.anadirItem(new BolaDeNieve());
				
			}

			break;
		}

		case "Dado": {

			int valor = rand.nextInt(11);

			if (valor < 8) {

				jugador.anadirItem(new Dado_lento());

			} else {

				jugador.anadirItem(new Dado_rapido());

			}

			break;

		}
		
		case "Motos": {

			new Trineo().realizarAccion(tablero, jugador);

			break;

		}

		
		}
	}

	public void setEventos(String[] eventos) {

		this.eventos = eventos;

	}

	public String[] getEventos() {

		return this.eventos;

	}

}
