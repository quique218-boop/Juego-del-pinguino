package modelo;

public class SueloQuebradizo extends Casilla {

	private String resultado;

	public SueloQuebradizo() {
	}

	@Override
	public void realizarAccion(Tablero tablero, Jugador jugador) {

		int numeroItemsTotal = jugador.getInventario().getBolas().size() + jugador.getInventario().getPez().size()
				+ jugador.getInventario().getDado().size();

		if (numeroItemsTotal > 5) {

			efectos_de_sonido.sonidoHielo();
			
			this.resultado = "Volver al inicio";

			efectos_de_sonido.sonidoMuerte();
			
			jugador.setPos(0);

			
		} else if (numeroItemsTotal > 0 && numeroItemsTotal <= 5) {

			this.resultado = "Perder un turno";

			jugador.setDeudaTurnos(1);

		} else if (numeroItemsTotal == 0) {

			this.resultado = "Nada";

		}
	}

	public String getResultado() {

		return this.resultado;

	}
}
