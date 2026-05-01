package modelo;

public class Foca extends Jugador {

	public Foca(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

	}
	
	public Foca(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos, int partidasTotales, int turnoEnArray, Usuario usuario) {
		super( posicion, nombre, color, inventario, deudaTurnos, partidasTotales, turnoEnArray, usuario);
	}


	public void aplastarJugador(Pinguino jugador) {

		efectos_de_sonido.sonidoFoca();
		
		jugador.getInventario().RobarInventario(); // La foca roba la mitad del inventario del jugador

	}

	public int golpearJugador(Pinguino jugador, Tablero tablero) {

		if (this.getPos() == jugador.getPos())
			new Agujero().realizarAccion(tablero, jugador);
		
		return jugador.getPos();

	}

	public void esSobornado() {
		
		this.setDeudaTurnos(2);

	}

}
