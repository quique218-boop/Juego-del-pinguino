package modelo;

import java.util.Random;

public class SueloQuebradizo extends Casilla {

	public SueloQuebradizo() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		Random ran = new Random();

		int numeroItemsTotal = jugador.getInventario().getBolas().size() + jugador.getInventario().getPez().size()
				+ jugador.getInventario().getDado().size();

		if (numeroItemsTotal > 5) {

			jugador.setPos(0);

		} else if (numeroItemsTotal > 0 && numeroItemsTotal <= 5) {

			jugador.setDeudaTurnos(1);

		}

		if (ran.nextInt(11) < 2) {

			if (ran.nextBoolean()) {
				
				jugador.setDeudaTurnos(1);
			
			} else {

				switch (ran.nextInt(3)) {
				case 0: {

					jugador.getInventario().getBolas().removeFirst();

					break;
				}

				case 1: {

					jugador.getInventario().getPez().removeFirst();

					break;
				}

				case 2: {

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
}
