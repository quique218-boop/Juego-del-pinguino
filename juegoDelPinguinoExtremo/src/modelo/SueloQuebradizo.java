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
			
			this.resultado = "El suelo se ha roto, buen viaje!";

			efectos_de_sonido.sonidoMuerte();
			
			jugador.setPos(0);

			
		} else if (numeroItemsTotal > 0 && numeroItemsTotal <= 5) {

			this.resultado = "El suelo se ha partido un poco y te has quedado atascado";

			jugador.setDeudaTurnos(1);

		} else if (numeroItemsTotal == 0) {

			this.resultado = "El suelo ha aguantado";

		}
		
		System.out.println(this.resultado);
	}

	public String getResultado() {

		return this.resultado;

	}
}
