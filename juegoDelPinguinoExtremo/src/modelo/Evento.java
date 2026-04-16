package modelo;

import java.util.Random;

public class Evento extends Casilla {

	private final String[] eventos = { "Pez", "BNeu", "Dado", "PerderTurno", "PerderObjeto", "Motos" };

	private String resultado;

	public Evento() {

	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		Random rand = new Random();

		switch (eventos[rand.nextInt(eventos.length)]) {

		case "Pez":

			efectos_de_sonido.sonidoEvento();
			
			this.resultado = "Obtener pez";

			jugador.anadirItem(new Pez());

			System.out.println(this.resultado);
			break;

		case "BNeu":

			efectos_de_sonido.sonidoEvento();
			
			int totalBolas = rand.nextInt(3) + 1;

			this.resultado = "Obtener " + totalBolas + " bolas de nieve";

			for (int i = 0; i < totalBolas; i++) {

				jugador.anadirItem(new BolaDeNieve());

			}
			System.out.println(this.resultado);
			break;

		case "Dado":

			efectos_de_sonido.sonidoEvento();
			
			int valor = rand.nextInt(11);

			if (valor < 8) {

				this.resultado = "Obtener dado lento";

				jugador.anadirItem(new DadoLento());

			} else {

				this.resultado = "Obtener dado rapido";

				jugador.anadirItem(new DadoRapido());

			}
			System.out.println(this.resultado);
			break;

		case "PerderTurno":

			efectos_de_sonido.sonidoPerdida();
			
			this.resultado = "Perder un turno";

			jugador.setDeudaTurnos(1);
			System.out.println(this.resultado);
			break;

		case "PerderObjeto":
			
			this.resultado = "Perder un objeto, ";
			
			switch (rand.nextInt(3)) {

			case 0: {

				efectos_de_sonido.sonidoPerdida();
				
				this.resultado += "has perdido bola de nieve";

				jugador.getInventario().getBolas().removeFirst();
				System.out.println(this.resultado);
				break;
			}

			case 1: {

				efectos_de_sonido.sonidoPerdida();
				
				this.resultado += "has perdido un pez";

				jugador.getInventario().getPez().removeFirst();
				System.out.println(this.resultado);
				break;
			}

			case 2: {

				efectos_de_sonido.sonidoPerdida();
				
				this.resultado += "has perdido un dado";

				jugador.getInventario().getDado().remove(rand.nextInt(jugador.getInventario().getDado().size()));
				System.out.println(this.resultado);
				break;
			}

			default:

				System.out.println("Borrar inventario fuera de rango");

			}
			System.out.println(this.resultado);
			break;

		case "Motos":
			
			efectos_de_sonido.sonidoEvento();

			this.resultado = "Motos de nieve";

			new Trineo().realizarAccion(tablero, jugador);
			System.out.println(this.resultado);
			break;
		}
	}

	public String[] getEventos() {

		return this.eventos;

	}

	public String getResultado() {

		return this.resultado;

	}

}
