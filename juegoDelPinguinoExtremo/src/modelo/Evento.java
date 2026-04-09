package modelo;

import java.util.Random;

public class Evento extends Casilla {

	private String[] eventos = { "Pez", "BNeu", "Dado", "Motos" };
	
	private String resultado;

	public Evento() {
		
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {


		Random rand = new Random();

		switch (eventos[rand.nextInt(eventos.length)]) {

		case "Pez": {
			
			this.resultado = "Obtener pez";

			jugador.anadirItem(new Pez());

			break;
		}

		case "BNeu": {

			int totalBolas = rand.nextInt(3) + 1;
			
			this.resultado = "Obtener " + totalBolas + " bolas de nieve";

			for (int i = 0; i < totalBolas; i++) {
				
				jugador.anadirItem(new BolaDeNieve());
				
			}

			break;
		}

		case "Dado": {

			int valor = rand.nextInt(11);

			if (valor < 8) {
				
				this.resultado = "Obtener dado lento";

				jugador.anadirItem(new DadoLento());

			} else {
				
				this.resultado = "Obtener dado rapido";

				jugador.anadirItem(new DadoRapido());

			}

			break;

		}
		
		case "Motos": {

			this.resultado = "Motos de nieve";
			
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
	
	public String getResultado() {

		return this.resultado;

	}

}
