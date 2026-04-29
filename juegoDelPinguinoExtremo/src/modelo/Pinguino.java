package modelo;

import java.util.ArrayList;

public class Pinguino extends Jugador {


	public Pinguino(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

	}
	
	public Pinguino() {
		super();
	}
	
	public Pinguino(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos, int partidasTotales, int turnoEnArray) {
		super( posicion, nombre, color, inventario, deudaTurnos, partidasTotales, turnoEnArray);
	}

	public void gestionarBatalla(Pinguino jugador) {

		if (jugador == null || (this.getPos() != jugador.getPos()))
			return;

		int misBolas = this.contarBolasdeNieve();

		int susBolas = jugador.contarBolasdeNieve();

		if (misBolas > susBolas) {

			jugador.moverPosicion(susBolas - misBolas);
			

		} else if (susBolas > misBolas) {

			this.moverPosicion(misBolas - susBolas);

		}
		
		this.ponerBolasdeNieveACero();

		jugador.ponerBolasdeNieveACero();

	}

	
}