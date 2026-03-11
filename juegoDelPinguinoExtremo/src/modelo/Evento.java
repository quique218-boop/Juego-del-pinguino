package modelo;

import java.util.Random;

public class Evento extends Casilla {

	String[] eventos = { "Pez", "BNeu", "Dado", "Motos" };

	public Evento() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		if (!(jugador instanceof Pinguino))
			return;

		Pinguino pinguino = (Pinguino) jugador; // Convertir jugador a pinguino.

		Random ran = new Random();

		switch (eventos[ran.nextInt(eventos.length)]) {

		case "Pez": {

			pinguino.anadirItem(new Pez("Pez"));

			break;
		}

		case "BNeu": {

			int totalBolas = ran.nextInt(3) + 1;

			for (int i = 0; i < totalBolas; i++) {
				pinguino.anadirItem(new BolaDeNieve("Bola de nieve"));
			}

			break;
		}

		case "Dado": {

			int valor = ran.nextInt(11);

			if (valor < 8) {

				pinguino.anadirItem(new Dado_lento("Dado Lento"));

			} else {

				pinguino.anadirItem(new Dado_rapido("Dado Rapido"));

			}

			break;

		}
		
		case "Motos": {

			new Trineo().realizarAccion(tablero, jugador);

			break;

		}

		default:
			System.out.println("Caso no valido");

			break;
		}
	}

	public void setEventos(String[] eventos) {

		this.eventos = eventos;

	}

	public String[] getEventos() {

		return this.eventos;

	}

}
