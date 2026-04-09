package modelo;

import java.util.Random;

public class SueloQuebradizo extends Casilla {

	private String resultado;

	public SueloQuebradizo() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		Random ran = new Random();

		int numeroItemsTotal = jugador.getInventario().getBolas().size() + jugador.getInventario().getPez().size()
				+ jugador.getInventario().getDado().size();

		if (numeroItemsTotal > 5) {

			this.resultado = "Volver al inicio";

			jugador.setPos(0);

		} else if (numeroItemsTotal > 0 && numeroItemsTotal <= 5) {

			this.resultado = "Perder un turno";

			jugador.setDeudaTurnos(1);

		} else if (numeroItemsTotal == 0) {
			
			this.resultado = "Nada";
			
		}

		if (ran.nextInt(11) < 2) {

			if (ran.nextBoolean()) {

				this.resultado = this.resultado + " y perder un turno";

				jugador.setDeudaTurnos(1);

			} else {

				switch (ran.nextInt(3)) {
				case 0: {

					this.resultado = this.resultado + " y perder una bola de nieve";

					jugador.getInventario().getBolas().removeFirst();

					break;
				}

				case 1: {

					this.resultado = this.resultado + " y perder un pez";

					jugador.getInventario().getPez().removeFirst();

					break;
				}

				case 2: {

					this.resultado = this.resultado + " y perder un dado";

					jugador.getInventario().getDado().remove(ran.nextInt(jugador.getInventario().getDado().size()));

					break;
				}

				default: {

					System.out.println("Borrar inventario fuera de rango");

				}

				}
			}
		}
	}

	public String getResultado() {

		return this.resultado;

	}
}
