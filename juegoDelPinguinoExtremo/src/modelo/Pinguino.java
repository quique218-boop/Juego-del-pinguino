package modelo;

import java.util.ArrayList;

public class Pinguino extends Jugador {


	public Pinguino(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

	}
	
	public Pinguino() {
		super();
	}
	
	public Pinguino(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos, int turnoEnArray, int puntuacion, Usuario usuario) {
		super( posicion, nombre, color, inventario, deudaTurnos, turnoEnArray, puntuacion, usuario);
	}

	public void gestionarBatalla(Pinguino jugador) {

		if (jugador == null || (this.getPos() != jugador.getPos()))
			return;

		int misBolas = this.contarBolasdeNieve();

		int susBolas = jugador.contarBolasdeNieve();

		if (misBolas > susBolas) {

			this.moverPosicion(misBolas - susBolas);

		} else if (susBolas > misBolas) {

			jugador.moverPosicion(susBolas - misBolas);

		}
		
		this.ponerBolasdeNieveACero();

		jugador.ponerBolasdeNieveACero();

	}

	
}