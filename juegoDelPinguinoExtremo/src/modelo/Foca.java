package modelo;

import java.util.ArrayList;

public class Foca extends Jugador {

	private ArrayList<String> golpeados;

	public Foca(String nombre, String color, Inventario inventario) {
		super(nombre, color, inventario);
		golpeados = new ArrayList<>();
	}

	public Foca(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos, int turnoEnArray) {
		super(posicion, nombre, color, inventario, deudaTurnos, turnoEnArray);
		golpeados = new ArrayList<>();
	}

	public void aplastarJugador(Pinguino jugador) {

		efectos_de_sonido.sonidoFoca();

		jugador.getInventario().RobarInventario(); // La foca roba la mitad del inventario del jugador

	}

	public int golpearJugador(Pinguino pinguino, Tablero tablero) {

		if (this.getPos() == pinguino.getPos())
			new Agujero().realizarAccion(tablero, pinguino);

		addGolpeados(pinguino);

		return pinguino.getPos();

	}

	public void esSobornado() {

		this.setDeudaTurnos(2);

	}

	public ArrayList<String> getGolpeados() {
		return golpeados;
	}

	public void addGolpeados(Pinguino pinguino) {
		golpeados.add(pinguino.getNombre() + " ha sido golpeado por " + this.getNombre());
	}

	public void resetGolpeados() {
		golpeados = new ArrayList<>();
	}

}
