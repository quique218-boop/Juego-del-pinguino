
package modelo;

import java.util.ArrayList;

public class Pinguino extends Jugador {
	
	private int puntuacion;
	private Usuario usuario;
	


	public Pinguino(String nombre, String color, Inventario inventario, Usuario usuario) {

		super(nombre, color, inventario);
		this.usuario = usuario;

	}
	
	public Pinguino() {
		super();
	}
	
	public Pinguino(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos, int turnoEnArray, int puntuacion, Usuario usuario) {
		super( posicion, nombre, color, inventario, deudaTurnos, turnoEnArray);
		this.usuario = usuario;
		this.puntuacion = puntuacion;
				
	}
	
	public Usuario getUsuario() {
		
		return usuario;
		
	}
	
	public void setUsuario(Usuario usuario) {
		
		this.usuario = usuario; 
		
	}
	
	public int getPuntuacion() {
		return puntuacion;
	}
	
	public void setPuntuacion(int puntuacion) {
		
		this.puntuacion = puntuacion;
	}
	
	public void sumarPuntos(int puntuacion) {
		
		this.puntuacion += puntuacion;
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